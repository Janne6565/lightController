package com.janne.lightcontroller.controllers;

import com.janne.lightcontroller.entities.Device;
import com.janne.lightcontroller.services.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/devices")
public class DeviceController {

	private final DeviceService deviceService;

	@GetMapping
	public ResponseEntity<List<Device>> getAllDevices() {
		return ResponseEntity.ok(deviceService.getDevices());
	}

	@GetMapping("/{device_uuid}")
	public ResponseEntity<Device> getDevice(@PathVariable String device_uuid) {
		return ResponseEntity.ok(deviceService.getDevice(device_uuid));
	}

	@PostMapping
	public ResponseEntity<Device> createDevice(@RequestBody Device device) {
		device.setUuid(null);
		return ResponseEntity.ok(deviceService.saveDevice(device));
	}

	@DeleteMapping("/{device_uuid}")
	public ResponseEntity<Void> deleteDevice(@PathVariable String device_uuid) {
		deviceService.deleteDevice(device_uuid);
		return ResponseEntity.accepted().build();
	}
}
