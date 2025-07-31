package com.janne.lightcontroller.repositories;

import com.janne.lightcontroller.entities.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriggerRepository extends JpaRepository<Trigger, String> {
}
