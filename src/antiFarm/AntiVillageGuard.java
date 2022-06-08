package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import core.J;

public class AntiVillageGuard implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getEntity().getType().equals(EntityType.IRON_GOLEM)) {
			if (event.getSpawnReason().equals(SpawnReason.VILLAGE_DEFENSE)) {
				if (J.configJ.config.getBoolean("villager-settings.prevent-golem-spawning.village-defense", true)) {
					event.setCancelled(true);
				}
			} else if (event.getSpawnReason().equals(SpawnReason.VILLAGE_INVASION)) {
				if (J.configJ.config.getBoolean("villager-settings.prevent-golem-spawning.village-raids", true)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}
