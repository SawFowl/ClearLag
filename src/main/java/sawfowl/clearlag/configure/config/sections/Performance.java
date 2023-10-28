package sawfowl.clearlag.configure.config.sections;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.api.world.server.ServerWorld;
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
	@Setting("HaltedWorlds")
	private Set<String> haltedWorlds = new HashSet<String>();

	public TickSpeed getTickSpeed() {
		return tickSpeed;
	}

	public ViewingRadius getViewingRadius() {
		return viewingRadius;
	}

	public boolean halt(ServerWorld world) {
		if(haltedWorlds.contains(world.key().asString())) {
			haltedWorlds.remove(world.key().asString());
		} else haltedWorlds.add(world.key().asString());
		return haltedWorlds.contains(world.key().asString());
	}

}
