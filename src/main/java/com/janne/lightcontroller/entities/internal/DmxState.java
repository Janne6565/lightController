package com.janne.lightcontroller.entities.internal;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DmxState {

	private final Map<Integer, byte[]> universes = new ConcurrentHashMap<>();

	public static DmxState fromMap(Map<Integer, Map<Integer, Integer>> dmxData) {
		DmxState dmxState = new DmxState();
		if (dmxData == null) {
			return dmxState;
		}
		dmxData.keySet().forEach(
				universe -> dmxData.get(universe).forEach((key, value) -> dmxState.setChannel(universe, key, value))
		);
		return dmxState;
	}

	public byte[] getUniverse(int universe) {
		return universes.getOrDefault(universe, new byte[512]);
	}

	public void setChannel(int universe, int channel, int value) {
		if (channel < 1 || channel > 512) {
			log.warn("Tried to set channel {} in universe {} which is out of bounds", channel, universe);
			return;
		}
		byte[] data = getUniverse(universe);
		data[channel - 1] = (byte) value;
		universes.put(universe, data);
	}

	public byte[] getUniverseCopy(int universe) {
		return Arrays.copyOf(getUniverse(universe), 512);
	}

	public Map<Integer, byte[]> getAllUniverses() {
		return universes;
	}
}
