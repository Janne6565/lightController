package com.janne.lightcontroller.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DmxBaseSettings {
	@Id
	private int universe;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "dmx_data_map",
			joinColumns = @JoinColumn(name = "action_id")
	)
	@MapKeyColumn(name = "dmx_address")
	@Column(name = "dmx_data")
	private Map<Integer, Integer> dmxData;
}
