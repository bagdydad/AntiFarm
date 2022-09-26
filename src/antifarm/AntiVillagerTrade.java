package antifarm;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiVillagerTrade implements Listener {

	private final Configuration config;

	public AntiVillagerTrade(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;

		if (event.isCancelled() || event.getPlayer() == null || event.getRightClicked() == null || !event.getRightClicked().getType().equals(EntityType.VILLAGER) && !event.getRightClicked().getType().equals(EntityType.WANDERING_TRADER)) return;

		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)) event.setCancelled(config.getBoolean("villager-settings.prevent-villager-trade", false));
		if (event.getRightClicked().getType().equals(EntityType.WANDERING_TRADER)) event.setCancelled(config.getBoolean("villager-settings.prevent-wandering-trader-trade", false));

	}

}
