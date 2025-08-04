package com.janne.lightcontroller.components;

import ch.bildspur.artnet.ArtNetClient;
import com.janne.lightcontroller.entities.Trigger;
import com.janne.lightcontroller.entities.actions.Action;
import com.janne.lightcontroller.entities.internal.DmxState;
import com.janne.lightcontroller.entities.internal.TriggerState;
import com.janne.lightcontroller.services.DmxBaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtNetBroadcaster {
	private final ArtNetClient artNetClient;
	private final TriggerStateHolder triggerStateHolder;
	private final DmxBaseService dmxBaseService;
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private Map<Integer, Map<Integer, Integer>> cachedBaseDmxData;

	@Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
	public void refreshCachedDmxData() {
		Map<Integer, Map<Integer, Integer>> newCache = new HashMap<>();

		for (int universe : dmxBaseService.getReservedUniverses()) {
			Map<Integer, Integer> dmxData = dmxBaseService.getDmxData(universe);
			if (dmxData != null) {
				newCache.put(universe, dmxData);
			}
		}
		cachedBaseDmxData = newCache;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void start() {
		scheduler.scheduleAtFixedRate(() -> {
			try {
				DmxState dmxState = DmxState.fromMap(cachedBaseDmxData);
				new LinkedList<>(triggerStateHolder.getActiveTriggers()).forEach(trigger -> processTrigger(trigger, dmxState));
				dmxState.getAllUniverses().forEach(this::sendDmxData);
			} catch (Exception e) {
				log.error("Error while sending DMX data", e);
			}
		}, 0, 10, TimeUnit.MILLISECONDS);
	}

	@SneakyThrows
	private void sendDmxData(int universe, byte[] dmxData) {
		String broadcastIP = "192.168.178.255";
		if (dmxData == null) {
			return;
		}
		InetAddress broadcastAddress = InetAddress.getByName(broadcastIP);
		artNetClient.unicastDmx(broadcastAddress, 0, universe, dmxData);
	}

	private void processTrigger(TriggerState triggerState, DmxState dmxState) {
		long startedOn = triggerState.getStartedOn();
		long runningSince = System.currentTimeMillis() - startedOn;
		Trigger trigger = triggerState.getTrigger();
		List<Action> triggerActions = trigger.getActions();
		long timeCountingUp = 0;
		Action lastActiveAction = null;
		for (Action action : triggerActions) {
			if (timeCountingUp <= runningSince) {
				lastActiveAction = action;
			} else {
				break;
			}
			timeCountingUp += (int) (action.getDuration() * 1000f);
		}
		if (lastActiveAction == null) {
			triggerStateHolder.removeActiveTrigger(triggerState);
			return;
		}
		long timeInAction = runningSince - (timeCountingUp - (int) (lastActiveAction.getDuration() * 1000f));
		if (timeInAction > lastActiveAction.getDuration() * 1000f) {
			lastActiveAction.execute(dmxState, 1f);
			triggerStateHolder.removeActiveTrigger(triggerState);
			return;
		}
		float progress = (float) timeInAction / 1000 / lastActiveAction.getDuration();
		lastActiveAction.execute(dmxState, progress);
	}
}
