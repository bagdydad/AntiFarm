package antifarm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiWaterFarm implements Listener {

	private final Configuration config;

	public AntiWaterFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockFromTo(BlockFromToEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.getBlock() == null || event.getToBlock() == null || event.getFace() == null) return;
		if (!event.getBlock().getType().equals(Material.WATER)) return;
		if (!config.getBoolean("farms-settings.prevent-water-harvesting-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlock().getRelative(event.getBlock().getFace(event.getToBlock())).getType().toString().toUpperCase())) return;

		event.setCancelled(true);

		if (!config.getBoolean("settings.break-blocks", true)) return;

		event.getBlock().getRelative(event.getBlock().getFace(event.getToBlock())).setType(Material.AIR);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.getPlayer() == null || event.getBlock() == null || event.getBlockClicked() == null || event.getBlockFace() == null || event.getBucket() == null) return;
		if (!event.getBucket().equals(Material.WATER_BUCKET)) return;
		if (!config.getBoolean("farms-settings.prevent-water-harvesting-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlockClicked().getRelative(event.getBlockFace()).getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

}