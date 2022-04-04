package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class AntiVillagerFarm implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (event.isCancelled()) return;
		if (!event.getEntity().getType().equals(EntityType.VILLAGER)) return;
		if (J.configJ.config.getBoolean("villager-settings.prevent-villagers-harvesting-farms", true)) {
			event.setCancelled(true);
		}
	}
	
}
