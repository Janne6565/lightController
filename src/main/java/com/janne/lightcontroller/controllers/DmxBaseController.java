package com.janne.lightcontroller.controllers;

import com.janne.lightcontroller.services.DmxBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DmxBaseController {

	private final DmxBaseService dmxBaseService;

	@GetMapping("/dmxBaseConfig")
	public ResponseEntity<Map<Integer, Map<Integer, Integer>>> getDmxBaseConfig() {
		Map<Integer, Map<Integer, Integer>> dmxBaseConfigs = new HashMap<>();
		dmxBaseService.getReservedUniverses().forEach(universe -> dmxBaseConfigs.put(universe, dmxBaseService.getDmxData(universe)));
		return ResponseEntity.ok(dmxBaseConfigs);
	}

	@PostMapping("/dmxBaseConfig/{universe}")
	public ResponseEntity<Map<Integer, Integer>> setDmxBaseConfig(@PathVariable int universe, @RequestBody Map<Integer, Integer> dmxData) {
		dmxBaseService.setDmxBaseSettings(universe, dmxData);
		return ResponseEntity.ok(dmxData);
	}

	@DeleteMapping("/dmxBaseConfig/{universe}")
	public ResponseEntity<Void> deleteDmxBaseConfig(@PathVariable int universe) {
		dmxBaseService.deleteUniverse(universe);
		return ResponseEntity.accepted().build();
	}

}
