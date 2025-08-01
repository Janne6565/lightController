package com.janne.lightcontroller.services;

import ch.bildspur.artnet.ArtNetClient;
import com.janne.lightcontroller.entities.Device;
import com.janne.lightcontroller.entities.Trigger;
import com.janne.lightcontroller.repositories.DeviceRepository;
import com.janne.lightcontroller.repositories.TriggerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TriggerService {

	private final TriggerRepository triggerRepository;
	private final ArtNetClient artnet;
	private final DeviceRepository deviceRepository;

	public List<Trigger> getTriggers() {
		return triggerRepository.findAll();
	}

	public Trigger getTrigger(String uuid) {
		return triggerRepository.findById(uuid).orElse(null);
	}

	public Trigger saveTrigger(Trigger trigger) {
		return triggerRepository.save(trigger);
	}

	public void deleteTrigger(String uuid) {
		if (!triggerRepository.existsById(uuid)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trigger with UUID" + uuid + " does not exist");
		}
		List<Device> devices = deviceRepository.findAll();
		devices.forEach(device -> device.getTriggers().removeIf(trigger -> trigger.getUuid().equals(uuid)));
		deviceRepository.saveAll(devices);
		triggerRepository.deleteById(uuid);
	}

	@Async
	public void handleTriggerExecution(String triggerUuid) {
		Trigger trigger = triggerRepository.findById(triggerUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trigger not found"));
		if (trigger.getLastTimeExecuted() + (long) (trigger.getDebounceTime() * 1000f) > System.currentTimeMillis()) {
			log.warn("Trigger [{}] has ran into debounce", triggerUuid);
			return;
		}
		trigger.setLastTimeExecuted(System.currentTimeMillis());
		triggerRepository.save(trigger);
		trigger.getActions().forEach(action -> action.execute(artnet));
	}

}
