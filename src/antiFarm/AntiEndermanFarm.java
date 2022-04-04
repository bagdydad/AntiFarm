package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class AntiEndermanFarm implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getEntity().getType().equals(EntityType.ENDERMAN)) {
			if (J.configJ.config.getBoolean("prevent-farms.enderman-harvesting-farms")) {
				if (J.configJ.config.getStringList("farm-blocks").contains(event.getBlock().getType().toString().toUpperCase())) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getEntity().getType().equals(EntityType.ENDERMAN)) {
			if (J.configJ.config.getBoolean("prevent-farms.enderman-harvesting-farms")) {
				if (J.configJ.config.getStringList("farm-blocks").contains(event.getItem().getType().toString().toUpperCase())) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}
