package com.janne.lightcontroller.entities.actions;

import com.janne.lightcontroller.entities.internal.DmxState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
@Entity
@DiscriminatorValue("ArtNetFade")
public class ArtNetFadeAction extends Action {
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "dmx_data_map_fade_from", joinColumns = @JoinColumn(name = "action_id"))
	@MapKeyColumn(name = "dmx_address")
	@Column(name = "dmx_value")
	private Map<Integer, Integer> dmxDataFrom = new HashMap<>();
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "dmx_data_map_fade_to", joinColumns = @JoinColumn(name = "action_id"))
	@MapKeyColumn(name = "dmx_address")
	@Column(name = "dmx_value")
	private Map<Integer, Integer> dmxDataTo = new HashMap<>();
	@Column
	private int universe;

	@Override
	public void execute(DmxState dmxState, float progress) {
		dmxDataTo.forEach((key, value) -> dmxState.setChannel(universe, key, (int) (dmxDataFrom.getOrDefault(key, 0) + (value - dmxDataFrom.getOrDefault(key, 0)) * progress)));
	}
}
