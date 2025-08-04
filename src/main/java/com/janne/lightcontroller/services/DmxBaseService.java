package com.janne.lightcontroller.services;

import com.janne.lightcontroller.entities.DmxBaseSettings;
import com.janne.lightcontroller.repositories.DmxBaseSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DmxBaseService {
	private final DmxBaseSettingsRepository dmxBaseSettingsRepository;

	public void setDmxBaseSettings(int universe, Map<Integer, Integer> dmxData) {
		dmxBaseSettingsRepository.save(
				DmxBaseSettings.builder()
						.universe(universe)
						.dmxData(dmxData)
						.build()
		);
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

}
