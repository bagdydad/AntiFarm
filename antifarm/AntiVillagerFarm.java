package antifarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiVillagerFarm implements Listener {

	private final Configuration config;

	public AntiVillagerFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityChangeBlock(EntityChangeBlockEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled()) return;
		if (!event.getEntity().getType().equals(EntityType.VILLAGER)) return;
		if (!config.getBoolean("villager-settings.prevent-villagers-harvesting-farms", true)) return;

		event.setCancelled(true);

	}

}
