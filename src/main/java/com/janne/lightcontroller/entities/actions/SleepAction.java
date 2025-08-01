package com.janne.lightcontroller.entities.actions;

import ch.bildspur.artnet.ArtNetClient;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("SLEEP")
public class SleepAction extends Action {
	@Column
	private float duration = 1;

	@Override
	public void execute(ArtNetClient artnet) {
		try {
			Thread.sleep((long) (duration * 1000f));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
