package com.janne.lightcontroller.entities.actions;

import ch.bildspur.artnet.ArtNetClient;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = ArtNetSignalAction.class, name = "ARTNET"),
		@JsonSubTypes.Type(value = SleepAction.class, name = "SLEEP")
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

	public abstract void execute(ArtNetClient artnet);
}
