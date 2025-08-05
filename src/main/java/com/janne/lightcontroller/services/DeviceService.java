package com.janne.lightcontroller.services;

import com.janne.lightcontroller.entities.Device;
import com.janne.lightcontroller.entities.Trigger;
import com.janne.lightcontroller.repositories.DeviceRepository;
import com.janne.lightcontroller.repositories.TriggerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {
	private final DeviceRepository deviceRepository;
	private final TriggerRepository triggerRepository;
	private final TriggerService triggerService;

	public List<Device> getDevices() {
		return deviceRepository.findAll();
	}

	public Device getDevice(String uuid) {
		return deviceRepository.findById(uuid).orElse(null);
	}

	public Device saveDevice(Device device) {
		if (deviceRepository.existsByMacAddress(device.getMacAddress())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Device with Mac Address " + device.getMacAddress() + " already exists");
		}
		if (!checkIfStringIsMacAddress(device.getMacAddress())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Mac Address");
		}

		return deviceRepository.save(device);
	}

	public Device updateDevice(Device device) {
		if (!deviceRepository.existsById(device.getUuid())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device with UUID" + device.getUuid() + " does not exist");
		}
		return deviceRepository.save(device);
	}

	public void deleteDevice(String uuid) {
		deviceRepository.deleteById(uuid);
	}

	private boolean checkIfStringIsMacAddress(String macAddress) {
		return macAddress.matches("^(?:[0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$");
	}

	public void handleExecute(String mac, int pin) {
		Trigger trigger = createTriggerIfNotExisting(mac, pin);
		triggerService.handleTriggerExecution(trigger.getUuid());
	}

	private Trigger createTriggerIfNotExisting(String mac, int pin) {
		Device device = Optional.ofNullable(deviceRepository.findByMacAddress(mac)).orElse(Device.builder().macAddress(mac).build());
		List<Trigger> triggers = device.getTriggers();
		if (triggers == null) {
			triggers = List.of(Trigger.builder().identifier(String.valueOf(pin)).lastTimeExecuted(System.currentTimeMillis()).build());
			device.setTriggers(triggers);
		} else {
			Optional<Trigger> trigger = triggers.stream().filter(t -> t.getIdentifier().equals(String.valueOf(pin))).findFirst();
			if (trigger.isEmpty()) {
				triggers.add(Trigger.builder().identifier(String.valueOf(pin)).lastTimeExecuted(System.currentTimeMillis()).build());
			}
		}
		return deviceRepository.save(device).getTriggers().get(0);
	}
}
