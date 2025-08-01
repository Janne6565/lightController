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
		Device device = deviceRepository.findByMacAddress(mac);
		if (device == null) {
			log.warn("Received execute message for device with mac address {} which does not exist", mac);
			return;
		}
		List<Trigger> triggers = device.getTriggers();
		if (triggers == null) {
			log.warn("Received execute message for device with mac address {} which has no triggers setup", mac);
			return;
		}
		List<Trigger> filteredTriggers = triggers.stream().filter(trigger -> trigger.getIdentifier().equals(String.valueOf(pin))).toList();
		if (filteredTriggers.isEmpty()) {
			log.warn("Received execute message for device with mac address {} which has no triggers setup for pin {}", mac, pin);
			return;
		}
		triggerService.handleTriggerExecution(filteredTriggers.get(0).getUuid());
	}
}
