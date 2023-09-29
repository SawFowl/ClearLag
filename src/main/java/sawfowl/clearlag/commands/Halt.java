package sawfowl.clearlag.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.world.gamerule.GameRules;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.Permissions;
import sawfowl.clearlag.configure.config.locale.LocalePath;
import sawfowl.clearlag.utils.Placeholders;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.localeapi.api.TextUtils;

public class Halt extends PluginCommand {

	private Value<ServerWorld> value;
	public Halt(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		ServerWorld world = getArgument(context, value).get();
		boolean halt = world.properties().gameRule(GameRules.DO_MOB_SPAWNING.get()) || world.properties().gameRule(GameRules.DO_FIRE_TICK.get()) || world.properties().gameRule(GameRules.MOB_GRIEFING.get()) || world.properties().gameRule(GameRules.RANDOM_TICK_SPEED.get()) > 0;
		if(halt) {
			world.properties().setGameRule(GameRules.RANDOM_TICK_SPEED.get(), 0);
		} else world.properties().setGameRule(GameRules.RANDOM_TICK_SPEED.get(), 3);
		world.properties().setGameRule(GameRules.DO_MOB_SPAWNING.get(), !halt);
		world.properties().setGameRule(GameRules.DO_FIRE_TICK.get(), !halt);
		world.properties().setGameRule(GameRules.MOB_GRIEFING.get(), !halt);
		src.sendMessage(getPrefix(locale).append(TextUtils.replace(getText(locale, halt ? LocalePath.COMMAND_HALT_ENABLE : LocalePath.COMMAND_HALT_DISABLE), Placeholders.WORLD, world.key().asString())));
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
		return Arrays.asList(ParameterSettings.of(value, false, new Object[] {}));
	}

}
