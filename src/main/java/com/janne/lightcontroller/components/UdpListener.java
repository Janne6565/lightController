package com.janne.lightcontroller.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.lightcontroller.entities.internal.ESPMessage;
import com.janne.lightcontroller.services.DeviceService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Slf4j
@Component
@RequiredArgsConstructor
public class UdpListener {

	private final DeviceService deviceService;
	private final ObjectMapper objectMapper;

	@PostConstruct
	public void startUdpListener() {
		Thread thread = new Thread(this::listen);
		thread.setDaemon(true);
		thread.start();
	}

	private void listen() {
		int port = 5005;
		byte[] buffer = new byte[1024];

		try (DatagramSocket socket = new DatagramSocket(port)) {
			log.info("🔊 UDP Listener started on port {}", port);

			while (true) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				String message = new String(packet.getData(), 0, packet.getLength()).trim();
				try {
					ESPMessage parsedMessage = objectMapper.readValue(message, ESPMessage.class);

					log.info("Received message: {}", parsedMessage);
					deviceService.handleExecute(parsedMessage.getMac(), parsedMessage.getPin());
				} catch (JsonProcessingException e) {
					log.warn("Read bad message: {}", message);
					log.error("Error while parsing message", e);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
