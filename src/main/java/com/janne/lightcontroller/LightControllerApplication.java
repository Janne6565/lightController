package com.janne.lightcontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LightControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LightControllerApplication.class, args);
	}

}
