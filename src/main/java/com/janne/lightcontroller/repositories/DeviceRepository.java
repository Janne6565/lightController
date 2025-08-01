package com.janne.lightcontroller.repositories;

import com.janne.lightcontroller.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
	boolean existsByMacAddress(String macAddress);

	Device findByMacAddress(String macAddress);
}
