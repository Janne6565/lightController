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
	@Column(nullable = false)
	private Float debounceTime = 0.5f;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Action> actions = new ArrayList<>();
}
