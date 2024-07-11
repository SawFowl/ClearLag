package sawfowl.clearlag.configure.locale.config.commands;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.clearlag.utils.Placeholders;
import sawfowl.localeapi.api.LocaleReference;

@ConfigSerializable
public class Kill implements LocaleReference {

	@Setting("Help")
	private List<Component> help = Arrays.asList(
		deserialize("&2/lagg kill monsters&f - &6kill all hostile mobs")
			.clickEvent(ClickEvent.runCommand("/lagg kill monsters")),
		deserialize("&2/lagg kill creature&f - &6kill all the peaceful mobs.")
			.clickEvent(ClickEvent.runCommand("/lagg kill creature")),
		deserialize("&2/lagg kill ambient&f - &6kill all the ambient mobs.")
			.clickEvent(ClickEvent.runCommand("/lagg kill ambient")),
		deserialize("&2/lagg kill waterсreature&f - &6kill all the peaceful water mobs.")
			.clickEvent(ClickEvent.runCommand("/lagg kill waterсreature")),
		deserialize("&2/lagg kill waterambient&f - &6kill all the ambient water mobs.")
			.clickEvent(ClickEvent.runCommand("/lagg kill waterambient")),
		deserialize("&2/lagg kill misc&f - &6kill all mobs outside of the other categories.")
			.clickEvent(ClickEvent.runCommand("/lagg kill misc")),
		deserialize("&2/lagg kill all&f - &6kill all the mobs.")
			.clickEvent(ClickEvent.runCommand("/lagg kill all"))
		);
	@Setting("Monsters")
	private Component monsters = deserialize("&eKilled monsters&f: &b" + Placeholders.SIZE + "&e.");
	@Setting("Creature")
	private Component creature = deserialize("&eKilled peaceful mobs&f: &b" + Placeholders.SIZE + "&e.");
	@Setting("Ambient")
	private Component ambient = deserialize("&eKilled ambient mobs&f: &b" + Placeholders.SIZE + "&e.");
	@Setting("WaterAmbient")
	private Component waterAmbient = deserialize("&eKilled ambient water mobs&f: &b" + Placeholders.SIZE + "&e.");
	@Setting("WaterCreature")
	private Component waterCreature = deserialize("&eKilled aquatic peaceful mobs&f: &b" + Placeholders.SIZE + "&e.");
	@Setting("Misc")
	private Component misc = deserialize("&eKilled mobs without category&f: &b" + Placeholders.SIZE + "&e.");
	@Setting("All")
	private Component all = deserialize("&eKilled mobs&f: &b" + Placeholders.SIZE + "&e.");
	public Kill() {}

	public List<Component> getHelp() {
		return help;
	}

	public Component getMonsters(long size) {
		return replace(monsters, Placeholders.SIZE, size);
	}

	public Component getCreature(long size) {
		return replace(creature, Placeholders.SIZE, size);
	}

	public Component getAmbient(long size) {
		return replace(ambient, Placeholders.SIZE, size);
	}

	public Component getWaterAmbient(long size) {
		return replace(waterAmbient, Placeholders.SIZE, size);
	}

	public Component getWaterCreature(long size) {
		return replace(waterCreature, Placeholders.SIZE, size);
	}

	public Component getMisc(long size) {
		return replace(misc, Placeholders.SIZE, size);
	}

	public Component getAll(long size) {
		return replace(all, Placeholders.SIZE, size);
	}

	public static Kill createRu() {
		Kill ru = new Kill();
		ru.help = Arrays.asList(
			ru.deserialize("&2/lagg kill monsters&f - &6убить всех враждебных мобов")
				.clickEvent(ClickEvent.runCommand("/lagg kill monsters")),
			ru.deserialize("&2/lagg kill creature&f - &6убить всех мирных мобов.")
				.clickEvent(ClickEvent.runCommand("/lagg kill creature")),
			ru.deserialize("&2/lagg kill ambient&f - &6убить всех мобов окружения.")
				.clickEvent(ClickEvent.runCommand("/lagg kill ambient")),
			ru.deserialize("&2/lagg kill waterсreature&f - &6убить всех мирных морских мобов.")
				.clickEvent(ClickEvent.runCommand("/lagg kill waterсreature")),
			ru.deserialize("&2/lagg kill waterambient&f - &6убить всех морских мобов окружения.")
				.clickEvent(ClickEvent.runCommand("/lagg kill waterambient")),
			ru.deserialize("&2/lagg kill misc&f - &6убить всех мобов вне других категорий.")
				.clickEvent(ClickEvent.runCommand("/lagg kill misc")),
			ru.deserialize("&2/lagg kill all&f - &6убить всех мобов.").clickEvent(ClickEvent
				.runCommand("/lagg kill all"))
			);
		ru.monsters = ru.deserialize("&eУбито монстров&f: &b" + Placeholders.SIZE + "&e.");
		ru.creature = ru.deserialize("&eУбито мирных мобов&f: &b" + Placeholders.SIZE + "&e.");
		ru.ambient = ru.deserialize("&eУбито мобов окружения&f: &b" + Placeholders.SIZE + "&e.");
		ru.waterAmbient = ru.deserialize("&eУбито водных мобов окружения&f: &b" + Placeholders.SIZE + "&e.");
		ru.waterCreature = ru.deserialize("&eУбито водных мирных мобов&f: &b" + Placeholders.SIZE + "&e.");
		ru.misc = ru.deserialize("&eУбито мобов без категории&f: &b" + Placeholders.SIZE + "&e.");
		ru.all = ru.deserialize("&eУбито мобов&f: &b" + Placeholders.SIZE + "&e.");
		return ru;
	}

}
