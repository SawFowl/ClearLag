package sawfowl.clearlag.configure.locale.config;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.clearlag.utils.Placeholders;
import sawfowl.localeapi.api.LocaleReference;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class Messages implements LocaleReference {

	@Setting("Prefix")
	private Component prefix = deserialize("&7[&bClear&cLag&7] ");

	@Setting("RemoveItems")
	private Component removeItems = deserialize("&eItems removed&f: &b" + Placeholders.SIZE + "&e.");

	@Setting("ChangeTickSpeed")
	private Component changeTickSpeed = deserialize("&eChanged the tick rate in the world - &b\"" + Placeholders.WORLD + "\"&e from &b" + Placeholders.FROM + "&e to &b" + Placeholders.TO + "&e.");

	@Setting("ChangeViewingRadius")
	private Component changeViewingRadius = deserialize("&eChanged the range of visibility in the world - &b\"" + Placeholders.WORLD + "\"&e from &b" + Placeholders.FROM + "&e to &b" + Placeholders.TO + "&e.");

	@Setting("ClearWarn10s")
	private Component clearWarn10s = deserialize("&eItems lying on the ground will be removed after &c10&e seconds!");

	@Setting("ClearWarn30s")
	private Component clearWarn30s = deserialize("&eItems lying on the ground will be removed after &a30&e seconds!");

	@Setting("Freeze")
	private Component freeze = deserialize("&4Heavy overload! Tiсks in the world &b\"" + Placeholders.WORLD + "\"&4 temporarily stopped! &eTicks will be enabled on one of the following work load tests.");
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

	public Component getFreeze(ServerWorld world) {
		return prefix.append(Text.of(freeze).replace(Placeholders.WORLD, world.key().asString()).get());
	}

	public static Messages createRu() {
		Messages ru = new Messages();
		ru.removeItems = ru.deserialize("&eУдалено предметов&f: &b" + Placeholders.SIZE + "&e.");
		ru.changeTickSpeed = ru.deserialize("&eИзменена скорость тиков в мире &b\"" + Placeholders.WORLD + "\"&e c &b" + Placeholders.FROM + "&e на &b" + Placeholders.TO + "&e.");
		ru.changeViewingRadius = ru.deserialize("&eИзменена дальность видимости в мире &b\"" + Placeholders.WORLD + "\"&e c &b" + Placeholders.FROM + "&e на &b" + Placeholders.TO + "&e.");
		ru.clearWarn10s = ru.deserialize("&eПредметы лежащие на земле будут удалены через &c10&e секунд!");
		ru.clearWarn30s = ru.deserialize("&eПредметы лежащие на земле будут удалены через &a30&e секунд!");
		ru.freeze = ru.deserialize("&4Сильная перегрузка! Тики в мире &b\"" + Placeholders.WORLD + "\"&4 временно остановленны! &eТики будут включены при одной из следующих проверок нагрузки.");
		return ru;
	}

}
