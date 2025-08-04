package com.janne.lightcontroller.services;

import com.janne.lightcontroller.entities.actions.Action;
import com.janne.lightcontroller.repositories.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionService {
	private final ActionRepository actionRepository;

	public List<Action> getActions() {
		return actionRepository.findAll();
	}

	public Action getAction(String uuid) {
		return actionRepository.findById(uuid).orElse(null);
	}

	public Action saveAction(Action action) {
		return actionRepository.save(action);
	}

}
