package com.janne.lightcontroller.services;

import com.janne.lightcontroller.entities.Device;
import com.janne.lightcontroller.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
	private final DeviceRepository deviceRepository;

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
}
