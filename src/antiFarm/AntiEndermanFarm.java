package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class AntiEndermanFarm implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if (event.getEntity().getType().equals(EntityType.ENDERMAN)) {
			if (J.configJ.config.getBoolean("disable-farms.enderman-farm")) {
				for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
					if (event.getItem().getName().toUpperCase().equals(checkBlock.toUpperCase())) {
						event.setCancelled(true);
						break;
					}
				}
			}
		}
	}

}
