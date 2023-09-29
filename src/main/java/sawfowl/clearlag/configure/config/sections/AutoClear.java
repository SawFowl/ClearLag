package sawfowl.clearlag.configure.config.sections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class AutoClear {

	@Setting("Enable")
	private boolean enable = true;
	@Setting("Debug")
	private boolean debug = true;
	@Setting("Interval")
	@Comment("Time between cleanups of items thrown on the ground.\nTime is indicated in minutes.")
	private int interval = 5;
	@Setting("BlackListItems")
	@Comment("A blacklist of items that the plugin should ignore.")
	private List<String> blackList = new ArrayList<String>(Arrays.asList("minecraft:netherite_pickaxe"));
	@Setting("BlackListWorlds")
	private List<String> blackListWorlds = new ArrayList<String>();
	@Setting("LimitMonsters")
	@Comment("If the number of monsters exceeds this value, they will all be killed.\nA value less than 1 will disable this functionality.")
	private long limitMonsters = 15000;

	public boolean isEnable() {
		return enable;
	}

	public boolean isDebug() {
		return debug;
	}

	public int getClearInterval() {
		return interval * 60;
	}

	public List<String> getBlackList() {
		return blackList;
	}

	public boolean isBlackList(ItemStackSnapshot snapshot) {
		return blackList.contains(ItemTypes.registry().valueKey(snapshot.type()).asString());
	}

	public boolean isBlackList(ServerWorld world) {
		return blackListWorlds.contains(world.key().asString());
	}

	public long getLimitMonsters() {
		return limitMonsters;
	}
}
