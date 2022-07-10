package antifarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
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

		if (event.getPlayer() == null || event.getBlock() == null || event.getBlockClicked() == null || event.getBlockFace() == null || event.getBucket() == null) return;
		if (!event.getBucket().equals(Material.WATER_BUCKET)) return;
		if (!config.getBoolean("farms-settings.prevent-water-harvesting-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlockClicked().getRelative(event.getBlockFace()).getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDispense(BlockDispenseEvent event) {

		if (event.getBlock() == null || event.getItem() == null || event.getVelocity() == null) return;
		if (!event.getBlock().getType().equals(Material.DISPENSER)) return;
		if (!event.getItem().getType().equals(Material.WATER_BUCKET)) return;

		Dispenser dispenser = (Dispenser) event.getBlock().getBlockData();
		Block block = event.getBlock().getRelative(dispenser.getFacing());

		if (!block.getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;
		if (!config.getBoolean("farms-settings.prevent-water-harvesting-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(block.getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

}