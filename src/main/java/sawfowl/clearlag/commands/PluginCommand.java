package sawfowl.clearlag.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityCategory;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.configure.config.locale.LocalePath;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.commands.parameterized.ParameterizedCommand;

public abstract class PluginCommand implements ParameterizedCommand {

	protected final ClearLag plugin;
	private Map<String, ParameterSettings> args;
	public PluginCommand(ClearLag plugin) {
		this.plugin = plugin;
		if(getArgs() != null) {
			args = new HashMap<String, ParameterSettings>();
			getArgs().forEach(arg -> args.put(arg.getKey(), arg));
		}
	}

	@Override
	public Component getText(Object[] path) {
		return null;
	}

	@Override
	public PluginContainer getContainer() {
		return plugin.getContainer();
	}

	@Override
	public Map<String, ParameterSettings> getSettingsMap() {
		return args;
	}

	public abstract List<ParameterSettings> getArgs();

	protected void async(Runnable runnable) {
		Sponge.asyncScheduler().executor(getContainer()).execute(runnable);
	}

	protected void sync(Runnable runnable) {
		Sponge.server().scheduler().executor(getContainer()).execute(runnable);
	}

	protected long killMobs(ServerWorld world, EntityCategory category) {
		List<? extends Entity> list = world.entities().stream().filter(entity -> !entity.type().equals(EntityTypes.PLAYER.get()) && entity.type().category().equals(category) && !entity.get(Keys.CUSTOM_NAME).isPresent()).collect(Collectors.toList());
		long size = list.stream().count();
		list.forEach(entity -> {
			entity.offer(Keys.HEALTH, 0d);
			entity.remove();
		});
		list.clear();;
		list = null;
		return size;
	}

	protected Component getPrefix(Locale locale) {
		return getText(locale, LocalePath.PREFIX);
	}

}
