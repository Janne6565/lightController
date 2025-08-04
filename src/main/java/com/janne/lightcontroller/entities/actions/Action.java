package com.janne.lightcontroller.entities.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.janne.lightcontroller.entities.internal.DmxState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = ArtNetFadeAction.class, name = "ArtNetFade"),
		@JsonSubTypes.Type(value = ArtNetHoldAction.class, name = "ArtNetHold"),
		@JsonSubTypes.Type(value = SleepAction.class, name = "Sleep")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "action_type")
@Getter
@Setter
public abstract class Action {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column
	private float duration = 1;

	public abstract void execute(DmxState dmxState, float progress);
}
