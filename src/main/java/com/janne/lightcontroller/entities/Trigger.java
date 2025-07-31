package com.janne.lightcontroller.entities;

import com.janne.lightcontroller.entities.actions.Action;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Trigger {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	@Column(nullable = false)
	private String identifier;
	@Column
	private long lastTimeExecuted;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Action> actions = new ArrayList<>();
}
