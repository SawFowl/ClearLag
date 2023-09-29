package sawfowl.clearlag.configure.config.sections.performance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ViewingRadius {

	public ViewingRadius(){}

	@Setting("Enable")
	private boolean enable = true;
	@Setting("Debug")
	@Comment("If true, there will be a message in the console when the world setting is changed.")
	private boolean debug = false;
	@Setting("BlackListWorlds")
	@Comment("A list of worlds to which this setting will not apply.")
	private List<String> blackListWorlds = new ArrayList<String>();
	@Setting("Maximum")
	private List<Max> max = Arrays.asList(new Max("minecraft:overworld", 10), new Max("minecraft:the_nether", 10), new Max("minecraft:the_end", 10));
	@Setting("Ticks")
	@Comment("Time thresholds of the tick of the world.\nThe lower bound determines when the world parameter should start approaching the upper bound.\nThe upper limit determines when the world parameter should start to decrease to maintain a high TPS.\nDo not swap the parameters. This will break the logic of the plugin.")
	private TicksSection ticks = new TicksSection(20d, 5d);

	public boolean isEnable() {
		return enable;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isBlackList(ServerWorld world) {
		return blackListWorlds.contains(world.key().asString());
	}

	public int getMax(ServerWorld world) {
		return max.stream().filter(max -> max.getWorld().equals(world.key().asString())).findFirst().map(Max::getValue).orElse(max.stream().filter(max -> max.getWorld().equals("minecraft:overworld")).findFirst().map(Max::getValue).orElse(10));
	}

	public TicksSection getTicks() {
		return ticks;
	}

}
