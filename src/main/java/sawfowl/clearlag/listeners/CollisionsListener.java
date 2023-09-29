package sawfowl.clearlag.listeners;

import java.util.stream.Stream;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.CollideEntityEvent;

import sawfowl.clearlag.ClearLag;

public class CollisionsListener {

	private final ClearLag plugin;
	private final DamageSource damageSource;
	public CollisionsListener(ClearLag plugin) {
		this.plugin = plugin;
		this.damageSource = DamageSource.builder().type(DamageTypes.SUFFOCATE).build();
	}

	@Listener
	public void onCollision(CollideEntityEvent event) {
		Stream<Entity> stream = event.entities().stream().filter(entity -> !entity.type().equals(EntityTypes.PLAYER.get()));
		if(stream.count() < plugin.getConfig().getCollisionLimit()) return;
		stream.forEach(this::damage);
		stream = null;
	}

	private void damage(Entity entity) {
		if(!entity.isRemoved()) entity.damage(2, damageSource);
	}

}
