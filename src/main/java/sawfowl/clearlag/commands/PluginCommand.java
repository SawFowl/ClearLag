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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.configure.locale.config.Commands;
import sawfowl.clearlag.configure.locale.config.Messages;
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
	public PluginContainer getContainer() {
		return plugin.getContainer();
	}

	@Override
	public Map<String, ParameterSettings> getSettingsMap() {
		return args;
	}

	@Override
	public Component getComponent(Object[] path) {
		return null;
	}

	public abstract List<ParameterSettings> getArgs();

	protected void async(Runnable runnable) {
		Sponge.asyncScheduler().executor(getContainer()).execute(runnable);
	}

	protected void sync(Runnable runnable) {
		Sponge.server().scheduler().executor(getContainer()).execute(runnable);
	}

	protected long killMobs(ServerWorld world, EntityCategory category) {
		List<? extends Entity> list = world.entities().stream().filter(entity -> !entity.type().equals(EntityTypes.PLAYER.get()) && (category == null || entity.type().category().equals(category)) && !entity.get(Keys.CUSTOM_NAME).isPresent()).collect(Collectors.toList());
		long size = list.size();
		list.forEach(entity -> {
			entity.offer(Keys.HEALTH, 1d);
			entity.damage(10000, plugin.getDamageSource());
			//entity.remove();
		});
		list.clear();;
		list = null;
		return size;
	}

	public Commands getCommands(Locale locale) {
		return plugin.getLocales().getAsReferenced(locale).getCommands();
	}

	public Commands getCommands(ServerPlayer player) {
		return getCommands(player.locale());
	}

	public Messages getMessages(Locale locale) {
		return plugin.getLocales().getAsReferenced(locale).getMessages();
	}

	public Messages getMessages(ServerPlayer player) {
		return getMessages(player.locale());
	}

	public Component getPrefix(Locale locale) {
		 return getMessages(locale).getPrefix();
	}

	public Component getPrefix(ServerPlayer player) {
		 return getMessages(player).getPrefix();
	}

}
