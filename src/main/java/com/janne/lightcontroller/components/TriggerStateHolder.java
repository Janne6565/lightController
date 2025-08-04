package com.janne.lightcontroller.components;

import com.janne.lightcontroller.entities.Trigger;
import com.janne.lightcontroller.entities.internal.TriggerState;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Getter
@Component
public class TriggerStateHolder {

	private final List<TriggerState> activeTriggers = new LinkedList<>();

	public void addActiveTrigger(Trigger trigger) {
		activeTriggers.add(TriggerState.builder().trigger(trigger).startedOn(System.currentTimeMillis()).build());
	}

	public void removeActiveTrigger(TriggerState triggerState) {
		activeTriggers.remove(triggerState);
	}

	public void clearActiveTriggers() {
		activeTriggers.clear();
	}
}
