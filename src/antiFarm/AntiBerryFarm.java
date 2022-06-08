package antiFarm;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import core.J;

public class AntiBerryFarm implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (event.getEntity().getType().equals(EntityType.FOX)) {
			if (event.getBlock().getType().equals(Material.SWEET_BERRY_BUSH)) {
				if (J.configJ.config.getBoolean("prevent-farms.berry-farms", true)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}
