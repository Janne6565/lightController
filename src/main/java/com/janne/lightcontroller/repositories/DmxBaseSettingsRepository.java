package com.janne.lightcontroller.repositories;

import com.janne.lightcontroller.entities.DmxBaseSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DmxBaseSettingsRepository extends JpaRepository<DmxBaseSettings, Integer> {
	@Query("DELETE FROM DmxBaseSettings WHERE universe = ?1")
	@Modifying
	void deleteAllByUniverse(int universe);
}
