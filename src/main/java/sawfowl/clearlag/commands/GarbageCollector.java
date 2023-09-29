package sawfowl.clearlag.commands;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.audience.Audience;
import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.Permissions;
import sawfowl.clearlag.configure.config.locale.LocalePath;
import sawfowl.clearlag.utils.Placeholders;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.localeapi.api.TextUtils;

public class GarbageCollector extends PluginCommand {

	public GarbageCollector(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		long free = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		System.gc();
		free = (Runtime.getRuntime().freeMemory() / 1024 / 1024) - free;
		src.sendMessage(getPrefix(locale).append(TextUtils.replace(getText(locale, LocalePath.COMMAND_GARBAGECOLLECTOR), Placeholders.SIZE, text(free))));
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
		return Permissions.GARBAGECOLLECTOR;
	}

	@Override
	public String command() {
		return "garbagecollector";
	}

	@Override
	public List<ParameterSettings> getArgs() {
		return null;
	}

}
