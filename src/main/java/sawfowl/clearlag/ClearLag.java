package sawfowl.clearlag;

import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityCategories;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppedGameEvent;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.gamerule.GameRules;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import sawfowl.clearlag.commands.MainCommand;
import sawfowl.clearlag.configure.config.Config;
import sawfowl.clearlag.configure.config.locale.LocalePath;
import sawfowl.clearlag.configure.config.locale.Locales;
import sawfowl.clearlag.listeners.CollisionsListener;
import sawfowl.clearlag.utils.Logger;
import sawfowl.clearlag.utils.Placeholders;
import sawfowl.commandpack.api.CommandPack;
import sawfowl.localeapi.api.event.LocaleServiseEvent;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

@Plugin("clearlag")
public class ClearLag {

	private Path configDir;
	private PluginContainer container;
	private static ClearLag instance;
	private ConfigurationReference<CommentedConfigurationNode> configurationReference;
	private ValueReference<Config, CommentedConfigurationNode> config;
	private long nextClearItems;
	private ScheduledTask taskClear;
	private ScheduledTask taskMonsters;
	private Map<ResourceKey, ScheduledTask> worldsTasks = new HashMap<ResourceKey, ScheduledTask>();
	private Logger logger;
	private Locales locales;
	private CollisionsListener collisionsListener;
	private CommandPack commandPack;
	private DamageSource damageSource;

	@Inject
	public ClearLag(PluginContainer container, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		this.container = container;
		configDir = configDirectory;
		logger = new Logger();
	}

	@Listener
	public void onLoadLocaleServise(LocaleServiseEvent.Construct event) {
		loadConfig();
		locales = new Locales(event.getLocaleService(), getConfig().isJsonLocales());
		nextClearItems = Instant.now().getEpochSecond() + getConfig().getAutoClear().getClearInterval();
	}

	@Listener
	public void onLoadCommandPackAPI(CommandPack.PostAPI event) {
		commandPack = event.getAPI();
	}

	@Listener(order = Order.LAST)
	public void onCompleteLoad(StartedEngineEvent<Server> event) {
		load();
		damageSource = DamageSource.builder().type(DamageTypes.GENERIC).build();
	}

	@Listener
	public void onReload(RefreshGameEvent event) {
		reload();
	}

	@Listener
	public void onCommandRegister(RegisterCommandEvent<Parameterized> event) {
		new MainCommand(instance).register(event);
	}

	@Listener
	public void onStop(StoppedGameEvent event) {
		stopTasks();
	}

	public static ClearLag getInstance() {
		return instance;
	}

	public PluginContainer getContainer() {
		return container;
	}

	public Config getConfig() {
		return config.get();
	}

	public Locales getLocales() {
		return locales;
	}

	public DamageSource getDamageSource() {
		return damageSource;
	}

	public void reload() {
		loadConfig();
		load();
	}

	public void load() {
		stopTasks();
		if(getConfig().getAutoClear().isEnable()) taskClear = Sponge.asyncScheduler().submit(Task.builder().plugin(container).interval(1, TimeUnit.SECONDS).execute(() -> {
			long current = Instant.now().getEpochSecond();
			switch ((int) (nextClearItems - current)) {
				case 10: {
					Sponge.server().onlinePlayers().forEach(player -> player.sendMessage(locales.getComponent(player.locale(), LocalePath.PREFIX).append(locales.getComponent(player.locale(), LocalePath.CHANGE_CLEAR_WARN10S))));
					break;
				}
				case 30: {
					Sponge.server().onlinePlayers().forEach(player -> player.sendMessage(locales.getComponent(player.locale(), LocalePath.PREFIX).append(locales.getComponent(player.locale(), LocalePath.CHANGE_CLEAR_WARN30S))));
					break;
				}
				default: break;
			}
			if(current < nextClearItems) return;
			nextClearItems = current + getConfig().getAutoClear().getClearInterval();
			long removed = removeItems();
			if(getConfig().getAutoClear().isDebug()) logger.info(locales.getString(locales.getSystemLocale(), LocalePath.REMOVE_ITEMS).replace(Placeholders.SIZE, String.valueOf(removed)));
			for(ServerPlayer player : Sponge.server().onlinePlayers()) player.sendMessage(locales.getComponent(player.locale(), LocalePath.PREFIX).append(locales.getText(player.locale(), LocalePath.REMOVE_ITEMS).replace(Placeholders.SIZE, removed).get()));
		}).build());
		if(getConfig().getAutoClear().getLimitMonsters() > 0) taskMonsters = Sponge.asyncScheduler().submit(Task.builder().plugin(container).interval(getConfig().getAutoClear().getClearInterval(), TimeUnit.SECONDS).execute(() -> {
			Sponge.server().worldManager().worlds().forEach(this::killMonsters);
		}).build());
		if(getConfig().getPerformance().getViewingRadius().isEnable() && getConfig().getPerformance().getTickSpeed().isEnable()) for(ServerWorld world : Sponge.server().worldManager().worlds()) {
			worldsTasks.put(world.key(), Sponge.asyncScheduler().submit(Task.builder().plugin(container).interval(5, TimeUnit.SECONDS).execute(() -> workWorld(world)).build()));
		}
		if(getConfig().getCollisionLimit() < 2) {
			if(collisionsListener != null) Sponge.eventManager().unregisterListeners(collisionsListener);
			collisionsListener = null;
		} else if(collisionsListener == null) Sponge.eventManager().registerListeners(container, collisionsListener = new CollisionsListener(instance));
	}

	public long removeItems() {
		long size = 0;
		for(ServerWorld world : Sponge.server().worldManager().worlds()) if(!getConfig().getAutoClear().isBlackList(world)) size += removeItems(world);
		return size;
	}

	public long removeItems(ServerWorld world) {
		List<? extends Entity> list = world.entities().stream().filter(entity -> entity.type().equals(EntityTypes.ITEM.get()) && entity.get(Keys.ITEM_STACK_SNAPSHOT).isPresent() && !getConfig().getAutoClear().isBlackList(entity.get(Keys.ITEM_STACK_SNAPSHOT).get())).collect(Collectors.toList());
		long size = list.stream().count();
		list.forEach(Entity::remove);
		list.clear();
		list = null;
		return size;
	}

	public void killMonsters(ServerWorld world) {
		List<? extends Entity> list = world.entities().stream().filter(entity -> entity.type().category().equals(EntityCategories.MONSTER.get()) && !entity.get(Keys.CUSTOM_NAME).isPresent()).collect(Collectors.toList());
		if(list.stream().count() > getConfig().getAutoClear().getLimitMonsters()) list.forEach(entity -> {
			entity.offer(Keys.HEALTH, 0d);
			entity.remove();
		});
		list.clear();
		list = null;
	}

	public void saveConfig() {
		config.setAndSave(getConfig());
	}

	private void stopTasks() {
		if(taskClear != null) {
			taskClear.cancel();
			taskClear = null;
		}
		if(taskMonsters != null) {
			taskMonsters.cancel();
			taskMonsters = null;
		}
		if(!worldsTasks.isEmpty()) {
			worldsTasks.values().forEach(ScheduledTask::cancel);
			worldsTasks.clear();
		}
	}

	private void workWorld(ServerWorld world) {
		double tickTime = commandPack.getTPS().getWorldTickTime(world);
		if(getConfig().getPerformance().getViewingRadius().isEnable() && !getConfig().getPerformance().getViewingRadius().isBlackList(world)) changeViewingRadius(world, tickTime, world.properties().viewDistance());
		if(getConfig().getPerformance().getTickSpeed().isEnable() && !getConfig().getPerformance().getTickSpeed().isBlackList(world)) changeTickSpeed(world, tickTime, world.properties().gameRule(GameRules.RANDOM_TICK_SPEED.get()));
	}

	private void changeViewingRadius(ServerWorld world, double tickTime, int view) {
		if(tickTime < getConfig().getPerformance().getViewingRadius().getTicks().getBeforeDecrease() && view <= getConfig().getPerformance().getViewingRadius().getMax(world)) {
			if(view == getConfig().getPerformance().getViewingRadius().getMax(world)) return;
			sync(() -> world.properties().setViewDistance(view + 1));
			if(world.key().asString().contains("nether")) if(getConfig().getPerformance().getViewingRadius().isDebug()) logger.info(locales.getStringForConsole(LocalePath.CHANGE_VIEWING_RADIUS).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, String.valueOf(view)).replace(Placeholders.TO, String.valueOf(view + 1)));
		} else if(tickTime > getConfig().getPerformance().getViewingRadius().getTicks().getBeforeIncrease() && view > 1) {
			sync(() -> world.properties().setViewDistance(view - 1));
			if(world.key().asString().contains("nether")) if(getConfig().getPerformance().getViewingRadius().isDebug()) logger.warn(locales.getStringForConsole(LocalePath.CHANGE_VIEWING_RADIUS).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, String.valueOf(view)).replace(Placeholders.TO, String.valueOf(view - 1)));
		}
	}

	private void changeTickSpeed(ServerWorld world, double tickTime, int speed) {
		if(tickTime < getConfig().getPerformance().getTickSpeed().getTicks().getBeforeIncrease() && speed <= getConfig().getPerformance().getTickSpeed().getMax(world)) {
			if(speed == getConfig().getPerformance().getTickSpeed().getMax(world)) return;
			sync(() -> world.properties().setGameRule(GameRules.RANDOM_TICK_SPEED.get(), speed + 1));
			if(world.key().asString().contains("nether")) if(getConfig().getPerformance().getTickSpeed().isDebug()) logger.info(locales.getStringForConsole(LocalePath.CHANGE_TICK_SPEED).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, String.valueOf(speed)).replace(Placeholders.TO, String.valueOf(speed + 1)));
		} else if(tickTime > getConfig().getPerformance().getTickSpeed().getTicks().getBeforeIncrease() && speed > 0) {
			sync(() -> world.properties().setGameRule(GameRules.RANDOM_TICK_SPEED.get(), speed- 1));
			if(world.key().asString().contains("nether")) if(getConfig().getPerformance().getTickSpeed().isDebug()) logger.warn(locales.getStringForConsole(LocalePath.CHANGE_TICK_SPEED).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.FROM, String.valueOf(speed)).replace(Placeholders.TO, String.valueOf(speed - 1)));
		}
	}

	private void sync(Runnable runnable) {
		Sponge.server().scheduler().executor(container).execute(runnable);
	}

	private void loadConfig() {
		try {
			configurationReference = HoconConfigurationLoader.builder().defaultOptions(SerializeOptions.OPTIONS_VARIANT_1).path(configDir.resolve("Config.conf")).build().loadToReference();
			config = configurationReference.referenceTo(Config.class);
			configurationReference.save();
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

}
