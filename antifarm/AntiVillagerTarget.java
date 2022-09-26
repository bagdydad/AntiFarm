package antifarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiVillagerTarget  implements Listener {

	private final Configuration config;

	public AntiVillagerTarget(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityTarget(EntityTargetEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getEntity() == null || event.getTarget() == null) return;
		if (!event.getTarget().getType().equals(EntityType.VILLAGER)) return;
		if (!config.getBoolean("villager-settings.prevent-targeting-villager", true)) return;

		event.setCancelled(true);

	}

}
