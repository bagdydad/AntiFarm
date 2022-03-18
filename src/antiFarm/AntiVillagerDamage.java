package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class AntiVillagerDamage implements Listener {
	
	@EventHandler
	public void onDamageVillager(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getCause().equals(DamageCause.ENTITY_ATTACK)) return;
		if (event.getCause().equals(DamageCause.VOID)) return;
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (J.configJ.config.getBoolean("settings.block-villager-damage")) {
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
			}
		}
	}
	
	@EventHandler
	public void onDamageEntityByEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (J.configJ.config.getBoolean("settings.block-villager-damage")) {
				if (event.getDamager() == null) {
					event.setCancelled(true);
				}
				if (!event.getDamager().isOp()) {
					event.setCancelled(true);
				}
			}
		}
	}

}
