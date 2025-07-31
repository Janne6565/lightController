package com.janne.lightcontroller.controllers;

import com.janne.lightcontroller.entities.Device;
import com.janne.lightcontroller.entities.Trigger;
import com.janne.lightcontroller.repositories.TriggerRepository;
import com.janne.lightcontroller.services.DeviceService;
import com.janne.lightcontroller.services.TriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TriggerController {

	private final TriggerService triggerService;
	private final DeviceService deviceService;

	@GetMapping("/devices/{device_uuid}/triggers")
	public ResponseEntity<List<Trigger>> getDevice(@PathVariable("device_uuid") String device_uuid) {
		Device device = deviceService.getDevice(device_uuid);
		if (device == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(device.getTriggers());
	}

	@PostMapping("/devices/{device_uuid}/triggers")
	public ResponseEntity<List<Trigger>> addTrigger(@PathVariable("device_uuid") String device_uuid, @RequestBody Trigger trigger) {
		Device device = deviceService.getDevice(device_uuid);
		if (device == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device with UUID" + device_uuid + " does not exist");
		}
		List<Trigger> triggers = device.getTriggers();
		if (triggers == null) {
			triggers = new ArrayList<>();
		}
		Trigger buildTrigger = triggerService.saveTrigger(trigger);
		triggers.add(buildTrigger);
		deviceService.updateDevice(device);
		return ResponseEntity.ok(device.getTriggers());
	}

	@PutMapping("/triggers/{trigger_uuid}")
	public ResponseEntity<Trigger> updateTrigger(@PathVariable("trigger_uuid") String trigger_uuid, @RequestBody Trigger newTrigger) {
		newTrigger.setUuid(trigger_uuid);
		return ResponseEntity.ok(triggerService.saveTrigger(newTrigger));
	}

	@DeleteMapping("/triggers/{trigger_uuid}")
	public ResponseEntity<Void> deleteTrigger(@PathVariable("trigger_uuid") String trigger_uuid) {
		triggerService.deleteTrigger(trigger_uuid);
		return ResponseEntity.accepted().build();
	}
}
