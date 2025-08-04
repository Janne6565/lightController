package com.janne.lightcontroller.repositories;

import com.janne.lightcontroller.entities.DmxBaseSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DmxBaseSettingsRepository extends JpaRepository<DmxBaseSettings, Integer> {
}
