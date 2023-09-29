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

public class Kill extends PluginCommand {

	public Kill(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Component title = getText(locale, LocalePath.COMMAND_HELP_TITLE);
		sendPaginationList(src, title, text("=").color(title.color()), 10, getListTexts(locale, LocalePath.COMMAND_KILL_HELP));
	}

	@Override
	public List<ParameterSettings> getArgs() {
		return null;
	}

	@Override
	public Parameterized build() {
		return builder()
				.addChild(new KillAll(plugin).build(), "all")
				.addChild(new KillAmbient(plugin).build(), "ambient")
				.addChild(new KillCreature(plugin).build(), "creature")
				.addChild(new KillMisc(plugin).build(), "misc")
				.addChild(new KillMonsters(plugin).build(), "monsters")
				.addChild(new KillWaterAmbient(plugin).build(), "waterambient")
				.addChild(new KillWaterCreature(plugin).build(), "watercreature")
				.build();
	}

	@Override
	public Settings getCommandSettings() {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.HELP;
	}

	@Override
	public String command() {
		return "kill";
	}

}
