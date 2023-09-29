package sawfowl.clearlag.configure.config;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.clearlag.configure.config.sections.AutoClear;
import sawfowl.clearlag.configure.config.sections.Performance;

@ConfigSerializable
public class Config {

	public Config(){}

	@Setting("JsonLocales")
	private boolean jsonLocales = true;
	@Setting("AutoClear")
	private AutoClear autoClear = new AutoClear();
	@Setting("EntityBlacklist")
	private List<String> entityBlacklist = Arrays.asList("sponge:human", "minecraft:boat", "minecraft:chest_minecart", "minecraft:command_block_minecart", "minecraft:furnace_minecart", "minecraft:hopper_minecart", "minecraft:spawner_minecart", "minecraft:minecart", "minecraft:tnt_minecart", "minecraft:minecart", "minecraft:bee");
	@Setting("Performance")
	private Performance performance = new Performance();
	@Setting("CollisionLimit")
	@Comment("Entities collision limit after which they will start taking choking damage.\nDoes not apply to players.\nA value less than 2 will disable this functionality.")
	private int collisionLimit = 20;

	public boolean isJsonLocales() {
		return jsonLocales;
	}

	public AutoClear getAutoClear() {
		return autoClear;
	}

	public List<String> getEntityBlacklist() {
		return entityBlacklist;
	}

	public Performance getPerformance() {
		return performance;
	}

	public int getCollisionLimit() {
		return collisionLimit;
	}

}
