package com.janne.lightcontroller.services;

import com.janne.lightcontroller.entities.DmxBaseSettings;
import com.janne.lightcontroller.repositories.DmxBaseSettingsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DmxBaseService {
	private final DmxBaseSettingsRepository dmxBaseSettingsRepository;

	public void setDmxBaseSettings(int universe, Map<Integer, Integer> dmxData) {
		DmxBaseSettings existingSettings = dmxBaseSettingsRepository.findById(universe).orElse(DmxBaseSettings.builder().universe(universe).build());
		existingSettings.setDmxData(dmxData);
		dmxBaseSettingsRepository.save(existingSettings);
	}

	public Optional<DmxBaseSettings> getDmxBaseSettings(int universe) {
		return dmxBaseSettingsRepository.findById(universe);
	}

	public Map<Integer, Integer> getDmxData(int universe) {
		Optional<DmxBaseSettings> dmxBaseSettings = getDmxBaseSettings(universe);
		return dmxBaseSettings.map(DmxBaseSettings::getDmxData).orElse(null);
	}

	public List<Integer> getReservedUniverses() {
		return dmxBaseSettingsRepository.findAll().stream().map(DmxBaseSettings::getUniverse).toList();
	}

	@Transactional
	public void deleteUniverse(int universe) {
		dmxBaseSettingsRepository.deleteAllByUniverse(universe);
	}
}
