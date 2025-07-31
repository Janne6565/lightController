package com.janne.lightcontroller.entities.actions;

import ch.bildspur.artnet.ArtNetClient;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.InetAddress;

@Getter
@Setter
@Entity
@DiscriminatorValue("ARTNET")
public class ArtNetSignalAction extends Action {
	private int subnet = 0;
	private int universe = 0;
	private byte[] dmxData = new byte[512];

	@SneakyThrows
	@Override
	public void execute(ArtNetClient artnet) {
		String broadcastIP = "192.168.178.255";
		InetAddress broadcastAddress = InetAddress.getByName(broadcastIP);
		System.out.println("📡 Using broadcast IP: " + broadcastIP);

		// Initialize ArtNet controller
		artnet.unicastDmx(broadcastAddress, subnet, universe, dmxData);
	}
}
