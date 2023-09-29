package sawfowl.clearlag.configure.config.locale;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import sawfowl.clearlag.utils.Placeholders;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.LocaleService;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.utils.AbstractLocaleUtil;

public class Locales {

	private final LocaleService localeService;
	private final boolean json;
	private String pluginid = "clearlag";
	public Locales(LocaleService localeService, boolean json) {
		this.localeService = localeService;
		this.json = json;
		localeService.localesExist(pluginid);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.DEFAULT);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.RU_RU);
		generateDefault();
		generateRu();
	}

	public LocaleService getLocaleService() {
		return localeService;
	}

	public Locale getSystemLocale() {
		return localeService.getSystemOrDefaultLocale();
	}

	public Component getText(Locale locale, Object... path) {
		return getAbstractLocaleUtil(locale).getComponent(json, path);
	}

	public List<Component> getListTexts(Locale locale, Object... path) {
		return getAbstractLocaleUtil(locale).getListComponents(json, path);
	}

	public String getString(Locale locale, Object... path) {
		return TextUtils.serializeLegacy(getText(locale, path));
	}

	public String getStringForConsole(Object... path) {
		return TextUtils.serializeLegacy(getText(getSystemLocale(), path));
	}

	public Component getTextWithReplaced(Locale locale, Map<String, String> map, Object... path) {
		return replace(getText(locale, path), map);
	}

	public Component getTextReplaced(Locale locale, Map<String, Component> map, Object... path) {
		return replaceComponent(getText(locale, path), map);
	}

	public Component getTextFromDefault(Object... path) {
		return getAbstractLocaleUtil(org.spongepowered.api.util.locale.Locales.DEFAULT).getComponent(json, path);
	}

	private void generateRu() {
		Locale locale = org.spongepowered.api.util.locale.Locales.RU_RU;
		boolean save = check(locale, toText("&7[&bClear&cLag&7] "), null, LocalePath.PREFIX);
		save = check(locale, toText("&eУдалено предметов&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.REMOVE_ITEMS);
		save = check(locale, toText("Изменена скорость тиков в мире \"" + Placeholders.WORLD + "\" c " + Placeholders.FROM + " на " + Placeholders.TO + "."), null, LocalePath.CHANGE_TICK_SPEED);
		save = check(locale, toText("Изменена дальность видимости в мире \"" + Placeholders.WORLD + "\" c " + Placeholders.FROM + " на " + Placeholders.TO + "."), null, LocalePath.CHANGE_VIEWING_RADIUS);
		save = check(locale, toText("&eПредметы лежащие на земле будут удалены через &c10&e секунд!"), null, LocalePath.CHANGE_CLEAR_WARN10S);
		save = check(locale, toText("&eПредметы лежащие на земле будут удалены через &a30&e секунд!"), null, LocalePath.CHANGE_CLEAR_WARN30S);
		save = check(locale, toText("&3Список команд"), null, LocalePath.COMMAND_HELP_TITLE);
		save = checkList(locale, Arrays.asList(toText("&2/lagg clear&f - &6удалить лежащие на земле предметы.").clickEvent(ClickEvent.runCommand("/lagg clear")), toText("&2/lagg halt&f - &6включить/выключить функцию остановки в мире").clickEvent(ClickEvent.runCommand("/lagg halt")), toText("&2/lagg gc&f - &6очистить память.").clickEvent(ClickEvent.runCommand("/lagg gc")), toText("&2/lagg kill&f - &6убийство мобов по категориям.").clickEvent(ClickEvent.runCommand("/lagg kill"))), null, LocalePath.COMMAND_HELP_LIST);
		save = checkList(locale, Arrays.asList(toText("&2/lagg kill monsters&f - &6убить всех враждебных мобов").clickEvent(ClickEvent.runCommand("/lagg kill monsters")), toText("&2/lagg kill creature&f - &6убить всех мирных мобов.").clickEvent(ClickEvent.runCommand("/lagg kill creature")), toText("&2/lagg kill ambient&f - &6убить всех мобов окружения.").clickEvent(ClickEvent.runCommand("/lagg kill ambient")), toText("&2/lagg kill waterсreature&f - &6убить всех мирных морских мобов.").clickEvent(ClickEvent.runCommand("/lagg kill waterсreature")), toText("&2/lagg kill waterambient&f - &6убить всех морских мобов окружения.").clickEvent(ClickEvent.runCommand("/lagg kill waterambient")), toText("&2/lagg kill misc&f - &6убить всех мобов вне других категорий.").clickEvent(ClickEvent.runCommand("/lagg kill misc")), toText("&2/lagg kill all&f - &6убить всех мобов.").clickEvent(ClickEvent.runCommand("/lagg kill all"))), null, LocalePath.COMMAND_KILL_HELP);
		save = check(locale, toText("&eУбито монстров&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_MONSTERS);
		save = check(locale, toText("&eУбито мобов&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_ALL);
		save = check(locale, toText("&eУбито мобов окружения&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_AMBIENT);
		save = check(locale, toText("&eУбито мирных мобов&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_CREATURE);
		save = check(locale, toText("&eУбито водных мобов окружения&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_WATER_AMBIENT);
		save = check(locale, toText("&eУбито водных мирных мобов&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_WATER_CREATURE);
		save = check(locale, toText("&eУбито мобов без категории&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_MISC);
		save = check(locale, toText("&eОчищенно памяти&f: &b" + Placeholders.SIZE + "&eМб."), null, LocalePath.COMMAND_GARBAGECOLLECTOR);
		save = check(locale, toText("&eВыключена функция остановки в мире&f: &b" + Placeholders.WORLD + "&e."), null, LocalePath.COMMAND_HALT_DISABLE);
		save = check(locale, toText("&eВключена функция остановки в мире&f: &b" + Placeholders.WORLD + "&e."), null, LocalePath.COMMAND_HALT_ENABLE);
		if(save) save(locale);
	}

	private void generateDefault() {
		Locale locale = org.spongepowered.api.util.locale.Locales.DEFAULT;
		boolean save = check(locale, toText("&7[&bClear&cLag&7] "), null, LocalePath.PREFIX);
		save = check(locale, toText("&eItems removed&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.REMOVE_ITEMS);
		save = check(locale, toText("Changed the tick rate in the world - \"" + Placeholders.WORLD + "\" from " + Placeholders.FROM + " to " + Placeholders.TO + "."), null, LocalePath.CHANGE_TICK_SPEED);
		save = check(locale, toText("Changed the range of visibility in the world - \"" + Placeholders.WORLD + "\" from " + Placeholders.FROM + " to " + Placeholders.TO + "."), null, LocalePath.CHANGE_VIEWING_RADIUS);
		save = check(locale, toText("&eItems lying on the ground will be removed after &c10&e seconds!"), null, LocalePath.CHANGE_CLEAR_WARN10S);
		save = check(locale, toText("&eItems lying on the ground will be removed after &a30&e seconds!"), null, LocalePath.CHANGE_CLEAR_WARN30S);
		save = check(locale, toText("&3Commands list"), null, LocalePath.COMMAND_HELP_TITLE);
		save = checkList(locale, Arrays.asList(toText("&2/lagg clear&f - &6remove any items lying on the ground.").clickEvent(ClickEvent.runCommand("/lagg clear")), toText("&2/lagg halt&f - &6enable/disable the halt function in the world.").clickEvent(ClickEvent.runCommand("/lagg halt")), toText("&2/lagg gc&f - &6clear ram.").clickEvent(ClickEvent.runCommand("/lagg gc")), toText("&2/lagg kill&f - &6killing mobs by category.").clickEvent(ClickEvent.runCommand("/lagg kill"))), null, LocalePath.COMMAND_HELP_LIST);
		save = checkList(locale, Arrays.asList(toText("&2/lagg kill monsters&f - &6kill all hostile mobs").clickEvent(ClickEvent.runCommand("/lagg kill monsters")), toText("&2/lagg kill creature&f - &6kill all the peaceful mobs.").clickEvent(ClickEvent.runCommand("/lagg kill creature")), toText("&2/lagg kill ambient&f - &6kill all the ambient mobs.").clickEvent(ClickEvent.runCommand("/lagg kill ambient")), toText("&2/lagg kill waterсreature&f - &6kill all the peaceful sea mobs.").clickEvent(ClickEvent.runCommand("/lagg kill waterсreature")), toText("&2/lagg kill waterambient&f - &6kill all the ambient sea mobs.").clickEvent(ClickEvent.runCommand("/lagg kill waterambient")), toText("&2/lagg kill misc&f - &6kill all mobs outside of the other categories.").clickEvent(ClickEvent.runCommand("/lagg kill misc")), toText("&2/lagg kill all&f - &6kill all the mobs.").clickEvent(ClickEvent.runCommand("/lagg kill all"))), null, LocalePath.COMMAND_KILL_HELP);
		save = check(locale, toText("&eKilled monsters&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_MONSTERS);
		save = check(locale, toText("&eKilled mobs&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_ALL);
		save = check(locale, toText("&eKilled ambient mobs&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_AMBIENT);
		save = check(locale, toText("&eKilled peaceful mobs&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_CREATURE);
		save = check(locale, toText("&eKilled ambient water mobs&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_WATER_AMBIENT);
		save = check(locale, toText("&eKilled aquatic peaceful mobs&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_WATER_CREATURE);
		save = check(locale, toText("&eKilled mobs without category&f: &b" + Placeholders.SIZE + "&e."), null, LocalePath.COMMAND_KILL_MISC);
		save = check(locale, toText("&eRAM cleared&f: &b" + Placeholders.SIZE + "&eMb."), null, LocalePath.COMMAND_GARBAGECOLLECTOR);
		save = check(locale, toText("&eDisabled halt function in the world&f: &b" + Placeholders.WORLD + "&e."), null, LocalePath.COMMAND_HALT_DISABLE);
		save = check(locale, toText("&eEnabled halt function in the world&f: &b" + Placeholders.WORLD + "&e."), null, LocalePath.COMMAND_HALT_ENABLE);
		if(save) save(locale);
	}

	private Component replace(Component component, Map<String, String> map) {
		return TextUtils.replace(component, map);
	}

	private Component replaceComponent(Component component, Map<String, Component> map) {
		return TextUtils.replaceToComponents(component, map);
	}

	private AbstractLocaleUtil getAbstractLocaleUtil(Locale locale) {
		return localeService.getPluginLocales(pluginid).getOrDefault(locale, localeService.getPluginLocales(pluginid).get(org.spongepowered.api.util.locale.Locales.DEFAULT));
	}

	private Component toText(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	private boolean check(Locale locale, Component value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkComponent(json, value, comment, path);
	}
/*
	private boolean check(Locale locale, String value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkString(value, comment, path);
	}
*/
	private boolean checkList(Locale locale, List<Component> value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkListComponents(json, value, comment, path);
	}

	private void save(Locale locale) {
		getAbstractLocaleUtil(locale).saveLocaleNode();
	}

}
