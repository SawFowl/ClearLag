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
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;

public class MainCommand extends PluginCommand {

	public MainCommand(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Component title = getComponent(locale, LocalePath.COMMAND_HELP_TITLE);
		sendPaginationList(src, title, text("=").color(title.color()), 10, getListTexts(locale, LocalePath.COMMAND_HELP_LIST));
	}

	@Override
	public List<ParameterSettings> getArgs() {
		return null;
	}

	@Override
	public Parameterized build() {
		return builder()
				.addChild(new Clear(plugin).build(), "clear")
				.addChild(new GarbageCollector(plugin).build(), "garbagecollector", "gc")
				.addChild(new Halt(plugin).build(), "halt")
				.addChild(new Kill(plugin).build(), "kill")
				.build();
	}

	@Override
	public Settings getCommandSettings() {
		return Settings.builder().setAliases(new String[] {"lagg"}).build();
	}

	@Override
	public String permission() {
		return Permissions.HELP;
	}

	@Override
	public String command() {
		return "clearlag";
	}

}
