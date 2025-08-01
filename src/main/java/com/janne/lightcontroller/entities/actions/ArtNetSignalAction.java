package com.janne.lightcontroller.entities.actions;

import ch.bildspur.artnet.ArtNetClient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@DiscriminatorValue("ARTNET")
public class ArtNetSignalAction extends Action {
	@Column
	private int subnet = 0;
	@Column
	private int universe = 0;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "dmx_data_map",
			joinColumns = @JoinColumn(name = "action_id")
	)
	@MapKeyColumn(name = "dmx_address")
	@Column(name = "dmx_value")
	private Map<Integer, Integer> dmxData = new HashMap<>();

	@SneakyThrows
	@Override
	public void execute(ArtNetClient artnet) {
		String broadcastIP = "192.168.178.255";
		InetAddress broadcastAddress = InetAddress.getByName(broadcastIP);
		artnet.unicastDmx(broadcastAddress, subnet, universe, convertMapToByteArray(dmxData));
	}

	private byte[] convertMapToByteArray(Map<Integer, Integer> data) {
		byte[] dmx = new byte[512];
		for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
			int address = entry.getKey();
			int value = entry.getValue();
			if (address >= 1 && address <= 512) {
				dmx[address - 1] = (byte) value;
			}
		}
		return dmx;
	}
}
