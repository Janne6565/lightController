package com.janne.lightcontroller.repositories;

import com.janne.lightcontroller.entities.actions.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, String> {
}
