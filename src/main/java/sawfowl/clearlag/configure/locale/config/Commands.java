package sawfowl.clearlag.configure.locale.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.clearlag.configure.locale.config.commands.Halt;
import sawfowl.clearlag.configure.locale.config.commands.Help;
import sawfowl.clearlag.configure.locale.config.commands.Kill;
import sawfowl.clearlag.utils.Placeholders;
import sawfowl.localeapi.api.LocaleReference;

@ConfigSerializable
public class Commands implements LocaleReference {

	@Setting("Halt")
	private Halt halt = new Halt();
	@Setting("Help")
	private Help help = new Help();
	@Setting("Kill")
	private Kill kill = new Kill();
	@Setting("GarbageCollector")
	private Component garbageCollector = deserialize("&eRAM cleared&f: &b" + Placeholders.SIZE + "&eMb.");
	public Commands() {}

	public Halt getHalt() {
		return halt;
	}

	public Help getHelp() {
		return help;
	}

	public Kill getKill() {
		return kill;
	}

	public Component getGarbageCollector(long size) {
		return replace(garbageCollector, Placeholders.SIZE, size);
	}

	public static Commands createRu() {
		Commands ru = new Commands();
		ru.halt = Halt.createRu();
		ru.help = Help.createRu();
		ru.kill = Kill.createRu();
		ru.garbageCollector = ru.deserialize("&eОчищенно памяти&f: &b" + Placeholders.SIZE + "&eМб.");
		return ru;
	}

}
