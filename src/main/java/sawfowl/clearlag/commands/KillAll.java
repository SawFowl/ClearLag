package sawfowl.clearlag.commands;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.Permissions;
import sawfowl.clearlag.configure.config.locale.LocalePath;
import sawfowl.clearlag.utils.Placeholders;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.localeapi.api.TextUtils;

public class KillAll extends PluginCommand {

	public KillAll(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		async(() -> {
			long size = 0;
			for(ServerWorld world : Sponge.server().worldManager().worlds()) size += killMobs(world);
			src.sendMessage(getPrefix(locale).append(TextUtils.replace(getText(locale, LocalePath.COMMAND_KILL_ALL), Placeholders.SIZE, text(size))));
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public Settings getCommandSettings() {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.KILL_ALL;
	}

	@Override
	public String command() {
		return "all";
	}

	@Override
	public List<ParameterSettings> getArgs() {
		return null;
	}

	private long killMobs(ServerWorld world) {
		Stream<? extends Entity> stream = world.entities().stream().filter(entity -> !entity.get(Keys.DISPLAY_NAME).isPresent() && !entity.get(Keys.CUSTOM_NAME).isPresent());
		long size = stream.count();
		stream.forEach(entity -> {
			entity.offer(Keys.HEALTH, 0d);
			entity.remove();
		});
		stream.close();
		stream = null;
		return size;
	}

}
