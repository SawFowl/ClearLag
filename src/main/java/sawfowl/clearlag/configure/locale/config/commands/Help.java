package sawfowl.clearlag.configure.locale.config.commands;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class Help {

	@Setting("List")
	private List<Component> list = Arrays.asList(
		TextUtils.deserializeLegacy("&2/lagg clear&f - &6remove any items lying on the ground.")
			.clickEvent(ClickEvent.runCommand("/lagg clear")),
		TextUtils.deserializeLegacy("&2/lagg halt&f - &6enable/disable the halt function in the world.")
			.clickEvent(ClickEvent.runCommand("/lagg halt")),
		TextUtils.deserializeLegacy("&2/lagg gc&f - &6clear ram.")
			.clickEvent(ClickEvent.runCommand("/lagg gc")),
		TextUtils.deserializeLegacy("&2/lagg kill&f - &6killing mobs by category.")
			.clickEvent(ClickEvent.runCommand("/lagg kill"))
		);
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3Commands list");
	public Help() {}

	public List<Component> getList() {
		return list;
	}

	public Component getTitle() {
		return title;
	}

	public static Help createRu() {
		Help ru = new Help();
		ru.list = Arrays.asList(
			TextUtils.deserializeLegacy("&2/lagg clear&f - &6удалить лежащие на земле предметы.")
				.clickEvent(ClickEvent.runCommand("/lagg clear")),
			TextUtils.deserializeLegacy("&2/lagg halt&f - &6включить/выключить функцию остановки в мире")
				.clickEvent(ClickEvent.runCommand("/lagg halt")),
			TextUtils.deserializeLegacy("&2/lagg gc&f - &6очистить память.")
				.clickEvent(ClickEvent.runCommand("/lagg gc")),
			TextUtils.deserializeLegacy("&2/lagg kill&f - &6убийство мобов по категориям.")
				.clickEvent(ClickEvent.runCommand("/lagg kill"))
			);
		ru.title = TextUtils.deserializeLegacy("&3Список команд");
		return ru;
	}

}
