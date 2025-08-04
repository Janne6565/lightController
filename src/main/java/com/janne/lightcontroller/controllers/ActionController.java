package com.janne.lightcontroller.controllers;

import com.janne.lightcontroller.entities.Trigger;
import com.janne.lightcontroller.entities.actions.Action;
import com.janne.lightcontroller.services.ActionService;
import com.janne.lightcontroller.services.TriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ActionController {

	private final ActionService actionService;
	private final TriggerService triggerService;

	@GetMapping("/actions")
	public ResponseEntity<List<Action>> getActions() {
		return ResponseEntity.ok(actionService.getActions());
	}

	@PutMapping("/trigger/{trigger_uuid}/actions")
	public ResponseEntity<List<Action>> saveActions(@PathVariable("trigger_uuid") String trigger_uuid, @RequestBody List<Action> actions) {
		Trigger trigger = triggerService.getTrigger(trigger_uuid);
		if (trigger == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trigger with UUID" + trigger_uuid + " does not exist");
		}
		trigger.getActions().clear();
		triggerService.saveTrigger(trigger);
		actions.forEach(action -> {
			action.setUuid(null);
			actionService.saveAction(action);
			trigger.getActions().add(action);
		});
		triggerService.saveTrigger(trigger);
		return ResponseEntity.ok(trigger.getActions());
	}
}
