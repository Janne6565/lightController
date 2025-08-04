package com.janne.lightcontroller.entities.actions;

import com.janne.lightcontroller.entities.internal.DmxState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@DiscriminatorValue("ArtNetHold")
public class ArtNetHoldAction extends Action {
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "dmx_data_map_1",
			joinColumns = @JoinColumn(name = "action_id")
	)
	@MapKeyColumn(name = "dmx_address")
	@Column(name = "dmx_value")
	private Map<Integer, Integer> dmxData = new HashMap<>();
	@Column
	private int universe;

	@Override
	public void execute(DmxState dmxState, float progress) {
		dmxData.forEach((key, value) -> dmxState.setChannel(universe, key, value));
	}
}
