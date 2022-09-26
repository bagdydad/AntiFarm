package antifarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockShearEntityEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiSheepShearing implements Listener {

	private final Configuration config;

	public AntiSheepShearing(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockShearEntity(BlockShearEntityEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getEntity() == null || !event.getEntity().getType().equals(EntityType.SHEEP)) return;
		if (!config.getBoolean("dispenser-settings.prevent-shearing", true)) return;

		event.setCancelled(true);

	}

}
