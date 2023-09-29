package sawfowl.clearlag.configure.config.sections.performance;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Max {

	public Max(){}
	public Max(String world, int value) {
		this.world = world;
		this.value = value;
	}

	@Setting("World")
	private String world;
	@Setting("Limit")
	private int value = 10;

	public String getWorld() {
		return world;
	}

	public int getValue() {
		return value;
	}

}
