package sawfowl.clearlag.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;

import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.api.game.server.CPServerWorld;

public class Halt extends PluginCommand {

	private Value<ServerWorld> value;
	public Halt(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		CPServerWorld world = CPServerWorld.cast(getArgument(context, value).get());
		boolean halt = plugin.getConfig().getPerformance().halt(world);
		world.setFreezeTicks(halt);
		src.sendMessage(getPrefix(locale).append(getCommands(locale).getHalt().getMessage(world, halt)));
		plugin.saveConfig();
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
		return Permissions.HALT;
	}

	@Override
	public String command() {
		return "halt";
	}

	@Override
	public List<ParameterSettings> getArgs() {
		value = Parameter.world().key("World").build();
		return Arrays.asList(ParameterSettings.of(value, false, null));
	}

}
