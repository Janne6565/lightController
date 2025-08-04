package com.janne.lightcontroller.entities.internal;

import com.janne.lightcontroller.entities.Trigger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TriggerState {
	private Trigger trigger;
	private long startedOn;
}
