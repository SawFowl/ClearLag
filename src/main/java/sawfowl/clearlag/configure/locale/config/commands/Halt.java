package sawfowl.clearlag.configure.locale.config.commands;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.clearlag.utils.Placeholders;
import sawfowl.localeapi.api.LocaleReference;

@ConfigSerializable
public class Halt implements LocaleReference {

	@Setting("Enable")
	private Component enable = deserialize("&eEnabled halt function in the world&f: &b" + Placeholders.WORLD + "&e.");
	@Setting("Disable")
	private Component disable = deserialize("&eDisabled halt function in the world&f: &b" + Placeholders.WORLD + "&e.");
	public Halt() {}

	public Component getMessage(ServerWorld world, boolean isEnable) {
		return replace(isEnable ? enable : disable, Placeholders.WORLD, world.key().asString());
	}

	public static Halt createRu() {
		Halt ru = new Halt();
		ru.enable = ru.deserialize("&eВключена функция остановки в мире&f: &b" + Placeholders.WORLD + "&e.");
		ru.disable = ru.deserialize("&eВыключена функция остановки в мире&f: &b" + Placeholders.WORLD + "&e.");
		return ru;
	}

}
