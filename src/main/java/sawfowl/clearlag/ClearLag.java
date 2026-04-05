package sawfowl.clearlag;

import java.lang.invoke.MethodHandles;
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
import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.api.world.gamerule.GameRules;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import sawfowl.clearlag.commands.MainCommand;
import sawfowl.clearlag.configure.config.Config;
import sawfowl.clearlag.configure.locale.LocaleConfig;
import sawfowl.clearlag.listeners.CollisionsListener;
import sawfowl.clearlag.utils.Logger;
import sawfowl.commandpack.api.mixin.game.MixinServerWorld;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.LocalesList;
import sawfowl.localeapi.api.config.ReferencedConfig;
import sawfowl.localeapi.api.services.ConfigurationService;
import sawfowl.localeapi.api.services.LocaleService;

@Plugin("clearlag")
public class ClearLag {

	private Path configDir;
	private PluginContainer container;
	private static ClearLag instance;
	private ReferencedConfig<Config> config;
	private long nextClearItems;
	private ScheduledTask taskClear;
	private ScheduledTask taskMonsters;
	private Map<ResourceKey, ScheduledTask> worldsTasks = new HashMap<ResourceKey, ScheduledTask>();
	private Logger logger;
	private CollisionsListener collisionsListener;
	private DamageSource damageSource;
	private LocalesList<LocaleConfig> locales;

	@Inject
	public ClearLag(PluginContainer container, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		this.container = container;
		configDir = configDirectory;
		logger = new Logger();
		locales = LocaleService.getInstance().createLocales(container, LocaleConfig.class);
		if(!locales.contains(Locales.DEFAULT)) locales.createReferencedTranslation(ConfigTypes.HOCON, Locales.DEFAULT, LocaleConfig.class);
		if(!locales.contains(Locales.RU_RU)) locales.createReferencedTranslation(ConfigTypes.HOCON, Locales.RU_RU, LocaleConfig.createRu());
		config = ConfigurationService.getInstance().createReferencedConfig(container, Config.class).setPath(configDir).setName("Config").setType(ConfigTypes.HOCON).build();
		nextClearItems = Instant.now().getEpochSecond() + getConfig().getAutoClear().getClearInterval();
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

	public LocalesList<LocaleConfig> getLocales() {
		return locales;
	}

	public DamageSource getDamageSource() {
		return damageSource;
	}

	public void reload() {
		config.load();
		load();
	}

	public void load() {
		stopTasks();
		if(getConfig().getAutoClear().isEnable()) taskClear = Sponge.asyncScheduler().submit(Task.builder().plugin(container).interval(1, TimeUnit.SECONDS).execute(() -> {
			long current = Instant.now().getEpochSecond();
			switch ((int) (nextClearItems - current)) {
				case 10: {
					Sponge.server().onlinePlayers().forEach(player -> player.sendMessage(locales.getAsReferenced(player).getMessages().getClearWarn10s()));
					break;
				}
				case 30: {
					Sponge.server().onlinePlayers().forEach(player -> player.sendMessage(locales.getAsReferenced(player).getMessages().getClearWarn30s()));
					break;
				}
				default: break;
			}
			if(current < nextClearItems) return;
			nextClearItems = current + getConfig().getAutoClear().getClearInterval();
			long removed = removeItems();
			if(getConfig().getAutoClear().isDebug()) logger.info(locales.getSystemAsReferenced().getMessages().getRemoveItemsLog(removed));
			for(ServerPlayer player : Sponge.server().onlinePlayers()) player.sendMessage(locales.getAsReferenced(player).getMessages().getRemoveItems(removed));
		}).build());
		if(getConfig().getAutoClear().getLimitMonsters() > 0) taskMonsters = Sponge.server().scheduler().submit(Task.builder().plugin(container).interval(getConfig().getAutoClear().getClearInterval(), TimeUnit.SECONDS).execute(() -> {
			Sponge.server().worldManager().worlds().forEach(this::killMonsters);
		}).build());
		if(getConfig().getPerformance().getViewingRadius().isEnable() && getConfig().getPerformance().getTickSpeed().isEnable()) for(ServerWorld world : Sponge.server().worldManager().worlds()) {
			worldsTasks.put(world.key(), Sponge.asyncScheduler().submit(Task.builder().plugin(container).interval(5, TimeUnit.SECONDS).execute(() -> workWorld(world)).build()));
		}
		if(getConfig().getCollisionLimit() < 2) {
			if(collisionsListener != null) Sponge.eventManager().unregisterListeners(collisionsListener);
			collisionsListener = null;
		} else if(collisionsListener == null) Sponge.eventManager().registerListeners(container, collisionsListener = new CollisionsListener(instance), MethodHandles.lookup());
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
		config.save();
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
		double tickTime = MixinServerWorld.cast(world).getTickTime();
		if(getConfig().getPerformance().getViewingRadius().isEnable() && !getConfig().getPerformance().getViewingRadius().isBlackList(world)) changeViewingRadius(world, tickTime, world.properties().viewDistance());
		if(getConfig().getPerformance().getTickSpeed().isEnable() && !getConfig().getPerformance().getTickSpeed().isBlackList(world)) changeTickSpeed(MixinServerWorld.cast(world), tickTime, world.properties().gameRule(GameRules.RANDOM_TICK_SPEED.get()));
	}

	private void changeViewingRadius(ServerWorld world, double tickTime, int view) {
		if(tickTime < getConfig().getPerformance().getViewingRadius().getTicks().getBeforeDecrease() && view <= getConfig().getPerformance().getViewingRadius().getMax(world)) {
			if(view == getConfig().getPerformance().getViewingRadius().getMax(world)) return;
			sync(() -> world.properties().setViewDistance(view + 1));
			if(getConfig().getPerformance().getViewingRadius().isDebug()) logger.info(locales.getSystemAsReferenced().getMessages().getChangeViewingRadiusLog(world, view, view + 1));
		} else if(tickTime > getConfig().getPerformance().getViewingRadius().getTicks().getBeforeIncrease() && view > 1) {
			sync(() -> world.properties().setViewDistance(view - 1));
			if(getConfig().getPerformance().getViewingRadius().isDebug()) logger.warn(locales.getSystemAsReferenced().getMessages().getChangeViewingRadiusLog(world, view, view - 1));
		}
	}

	private void changeTickSpeed(MixinServerWorld world, double tickTime, int speed) {
		if(tickTime < getConfig().getPerformance().getTickSpeed().getTicks().getBeforeIncrease() && speed <= getConfig().getPerformance().getTickSpeed().getMax(world)) {
			if(speed == getConfig().getPerformance().getTickSpeed().getMax(world) || getConfig().getPerformance().isHalted(world)) return;
			sync(() -> {
				world.properties().setGameRule(GameRules.RANDOM_TICK_SPEED.get(), speed + 1);
				if(world.isFreezeTicks()) world.setFreezeTicks(false);
			});
			if(getConfig().getPerformance().getTickSpeed().isDebug()) logger.info(locales.getSystemAsReferenced().getMessages().getChangeTickSpeedLog(world, speed, speed + 1));
		} else if(tickTime > getConfig().getPerformance().getTickSpeed().getTicks().getBeforeIncrease() && speed > 0) {
			sync(() -> world.properties().setGameRule(GameRules.RANDOM_TICK_SPEED.get(), speed - 1));
			if(getConfig().getPerformance().getTickSpeed().isDebug()) logger.warn(locales.getSystemAsReferenced().getMessages().getChangeTickSpeedLog(world, speed, speed - 1));
		} else if(tickTime  > getConfig().getPerformance().getTickSpeed().getTicks().getBeforeFreeze() && speed <= 1) {
			sync(() -> world.setFreezeTicks(true));
			logger.warn(locales.getSystemAsReferenced().getMessages().getFreeze(world));
		}
	}

	private void sync(Runnable runnable) {
		Sponge.server().scheduler().executor(container).execute(runnable);
	}

}
