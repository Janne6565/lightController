package com.janne.lightcontroller.services;

import com.janne.lightcontroller.entities.Trigger;
import com.janne.lightcontroller.repositories.TriggerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TriggerService {

	private final TriggerRepository triggerRepository;

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
		triggerRepository.deleteById(uuid);
	}
}
