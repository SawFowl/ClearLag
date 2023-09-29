package sawfowl.clearlag.configure.config.sections;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.clearlag.configure.config.sections.performance.TickSpeed;
import sawfowl.clearlag.configure.config.sections.performance.ViewingRadius;

@ConfigSerializable
public class Performance {

	public Performance(){}

	@Setting("TickSpeed")
	private TickSpeed tickSpeed = new TickSpeed();
	@Setting("ViewingRadius")
	private ViewingRadius viewingRadius = new ViewingRadius();

	public TickSpeed getTickSpeed() {
		return tickSpeed;
	}

	public ViewingRadius getViewingRadius() {
		return viewingRadius;
	}

}
