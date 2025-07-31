package com.janne.lightcontroller.config;

import ch.bildspur.artnet.ArtNetClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtNetConfig {
	@Bean
	public ArtNetClient artnet() {
		ArtNetClient artnet = new ArtNetClient();
		artnet.start();
		return artnet;
	}
}
