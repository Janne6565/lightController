package com.janne.lightcontroller.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	@Column(unique = true, nullable = false)
	private String macAddress;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Trigger> triggers = new ArrayList<>();
}
