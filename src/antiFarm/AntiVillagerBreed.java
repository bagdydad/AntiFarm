package antiFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import core.J;

import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class AntiVillagerBreed implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVillagerPickup(EntityPickupItemEvent event) {
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (J.configJ.config.getBoolean("villager-settings.prevent-villagers-breed", true)) {
				Villager villager = (Villager) event.getEntity();
				villager.setBreed(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVillagerDrop(EntityDropItemEvent event) {
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (J.configJ.config.getBoolean("villager-settings.prevent-villagers-breed", true)) {
				Villager villager = (Villager) event.getEntity();
				villager.setBreed(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVillagerBreed(EntityBreedEvent event) {
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (J.configJ.config.getBoolean("villager-settings.prevent-villagers-breed", true)) {
				event.setCancelled(true);
				Villager mother = (Villager) event.getMother();
				Villager father = (Villager) event.getFather();
				mother.setBreed(false);
				father.setBreed(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVillagerSpawn(CreatureSpawnEvent event) {
		if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
			if (event.getSpawnReason().equals(SpawnReason.BREEDING)) {
				if (J.configJ.config.getBoolean("villager-settings.prevent-villagers-breed", true)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}