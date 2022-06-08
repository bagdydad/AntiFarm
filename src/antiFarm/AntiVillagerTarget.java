package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import core.J;

public class AntiVillagerTarget  implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getTarget() == null) return;
		if (event.getTarget().getType().equals(EntityType.VILLAGER)) {
			if (J.configJ.config.getBoolean("villager-settings.prevent-targeting-villager", true)) {
				event.setCancelled(true);
			}
		}
	}
	
}
