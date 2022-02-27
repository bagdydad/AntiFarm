package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreedEvent;

public class AntiVillagerBreed implements Listener {
	
	@EventHandler
	public void onEntityBreed(EntityBreedEvent event) {
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (J.configJ.config.getBoolean("disable-mob-farms.villager-breed")) {
				event.setExperience(0);
			}
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (event.getSpawnReason().equals(SpawnReason.BREEDING)) {
				if (J.configJ.config.getBoolean("disable-mob-farms.villager-breed")) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}