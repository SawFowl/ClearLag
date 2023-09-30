package sawfowl.clearlag.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.data.Keys;
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
		List<Entity> list = event.entities().stream().filter(entity -> !entity.type().equals(EntityTypes.PLAYER.get())).collect(Collectors.toList());
		if(list.size() < plugin.getConfig().getCollisionLimit()) return;
		list.forEach(this::damage);
		list = null;
	}

	private void damage(Entity entity) {
		if(!entity.isRemoved() && entity.get(Keys.HEALTH).isPresent() ) 
			entity.damage(entity.get(Keys.HEALTH).get() > 2 ? 2 : entity.get(Keys.HEALTH).get(), damageSource);
	}

}
