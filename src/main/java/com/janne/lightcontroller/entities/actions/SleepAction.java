package com.janne.lightcontroller.entities.actions;

import com.janne.lightcontroller.entities.internal.DmxState;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("Sleep")
public class SleepAction extends Action {
	@Override
	public void execute(DmxState dmxState, float progress) {
	}
}
