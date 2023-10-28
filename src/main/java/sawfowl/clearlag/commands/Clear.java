package sawfowl.clearlag.commands;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.Permissions;
import sawfowl.clearlag.configure.config.locale.LocalePath;
import sawfowl.clearlag.utils.Placeholders;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;

public class Clear extends PluginCommand {

	public Clear(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		async(() -> {
			src.sendMessage(getPrefix(locale).append(getText(locale, LocalePath.REMOVE_ITEMS).replace(Placeholders.SIZE, plugin.removeItems()).get()));
		});
	}

	@Override
	public List<ParameterSettings> getArgs() {
		return null;
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
		return Permissions.CLEAR;
	}

	@Override
	public String command() {
		return "clear";
	}

	@Override
	public Component getComponent(Object[] path) {
		return null;
	}

}
