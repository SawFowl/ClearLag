package sawfowl.clearlag.configure.locale.config;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

import sawfowl.clearlag.utils.Placeholders;
import sawfowl.localeapi.api.LocaleReference;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class Messages implements LocaleReference {

	private Component prefix = deserialize("&7[&bClear&cLag&7] ");

	private Component removeItems = deserialize("&eItems removed&f: &b" + Placeholders.SIZE + "&e.");

	private Component changeTickSpeed = deserialize("Changed the tick rate in the world - \"" + Placeholders.WORLD + "\" from " + Placeholders.FROM + " to " + Placeholders.TO + ".");

	private Component changeViewingRadius = deserialize("Changed the range of visibility in the world - \"" + Placeholders.WORLD + "\" from " + Placeholders.FROM + " to " + Placeholders.TO + ".");

	private Component clearWarn10s = deserialize("&eItems lying on the ground will be removed after &c10&e seconds!");

	private Component clearWarn30s = deserialize("&eItems lying on the ground will be removed after &a30&e seconds!");
	public Messages() {}

	public Component getPrefix() {
		return prefix;
	}

	public Component getRemoveItems(long size) {
		return prefix.append(replace(removeItems, Placeholders.SIZE, size));
	}

	public String getRemoveItemsLog(long size) {
		return TextUtils.clearDecorations(removeItems).replace(Placeholders.SIZE, String.valueOf(size));
	}

	public Component getChangeTickSpeed(ServerWorld world, int from, int to) {
		return prefix.append(Text.of(changeTickSpeed).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, from).replace(Placeholders.TO, to).get());
	}

	public String getChangeTickSpeedLog(ServerWorld world, int from, int to) {
		return TextUtils.clearDecorations(changeTickSpeed).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, String.valueOf(from)).replace(Placeholders.TO, String.valueOf(to));
	}

	public Component getChangeViewingRadius(ServerWorld world, int from, int to) {
		return prefix.append(Text.of(changeViewingRadius).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, from).replace(Placeholders.TO, to).get());
	}

	public String getChangeViewingRadiusLog(ServerWorld world, int from, int to) {
		return TextUtils.clearDecorations(changeViewingRadius).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, String.valueOf(from)).replace(Placeholders.TO, String.valueOf(to));
	}

	public Component getClearWarn10s() {
		return prefix.append(clearWarn10s);
	}

	public Component getClearWarn30s() {
		return prefix.append(clearWarn30s);
	}

	public static Messages createRu() {
		Messages ru = new Messages();
		ru.removeItems = ru.deserialize("&eУдалено предметов&f: &b" + Placeholders.SIZE + "&e.");
		ru.changeTickSpeed = ru.deserialize("Изменена скорость тиков в мире \"" + Placeholders.WORLD + "\" c " + Placeholders.FROM + " на " + Placeholders.TO + ".");
		ru.changeViewingRadius = ru.deserialize("Изменена дальность видимости в мире \"" + Placeholders.WORLD + "\" c " + Placeholders.FROM + " на " + Placeholders.TO + ".");
		ru.clearWarn10s = ru.deserialize("&eПредметы лежащие на земле будут удалены через &c10&e секунд!");
		ru.clearWarn30s = ru.deserialize("&eПредметы лежащие на земле будут удалены через &a30&e секунд!");
		return ru;
	}

}
