package antifarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiEndermanFarm implements Listener {

	private final Configuration config;

	public AntiEndermanFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityChangeBlock(EntityChangeBlockEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getEntity() == null) return;
		if (!event.getEntity().getType().equals(EntityType.ENDERMAN)) return;
		if (!config.getBoolean("farms-settings.prevent-enderman-harvesting-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlock().getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityPickupItem(EntityPickupItemEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getEntity() == null) return;
		if (!event.getEntity().getType().equals(EntityType.ENDERMAN)) return;
		if (!config.getBoolean("farms-settings.prevent-enderman-harvesting-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getItem().getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

}
