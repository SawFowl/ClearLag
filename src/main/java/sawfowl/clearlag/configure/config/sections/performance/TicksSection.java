package sawfowl.clearlag.configure.config.sections.performance;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class TicksSection {

	public TicksSection(){}

	public TicksSection(double... values) {
		beforeDecrease = values[0];
		beforeIncrease = values[1];
	}

	@Setting("BeforeDecrease")
	private double beforeDecrease;
	@Setting("BeforeIncrease")
	private double beforeIncrease;

	public double getBeforeDecrease() {
		return beforeDecrease;
	}

	public double getBeforeIncrease() {
		return beforeIncrease;
	}

}
