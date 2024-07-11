package sawfowl.clearlag.commands;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.EntityCategories;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;

import sawfowl.clearlag.ClearLag;
import sawfowl.clearlag.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;

public class KillMisc extends PluginCommand {

	public KillMisc(ClearLag plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		async(() -> {
			long size = 0;
			for(ServerWorld world : Sponge.server().worldManager().worlds()) size += killMobs(world, EntityCategories.MISC.get());
			src.sendMessage(getPrefix(locale).append(getCommands(locale).getKill().getMisc(size)));
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
		return Permissions.KILL_MISC;
	}

	@Override
	public String command() {
		return "misc";
	}

}
