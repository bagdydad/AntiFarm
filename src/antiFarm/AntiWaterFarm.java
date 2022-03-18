package antiFarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class AntiWaterFarm implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.getBlock().getType().equals(Material.WATER)) {
			if (J.configJ.config.getBoolean("disable-farms.water-farm")) {
				for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
					if (event.getToBlock().getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
						event.setCancelled(true);
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if (event.getBucket().equals(Material.WATER_BUCKET)) {
			if (J.configJ.config.getBoolean("disable-farms.water-farm")) {
				for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
					if (event.getBlock().getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
						event.setCancelled(true);
						break;
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDispense(BlockDispenseEvent event) {
		if (event.getBlock().getType().equals(Material.DISPENSER)) {
			if (event.getItem().getType().equals(Material.WATER_BUCKET)) {
				Dispenser dispenser = (Dispenser) event.getBlock().getBlockData();
				Block block = event.getBlock().getRelative(dispenser.getFacing());
				if (J.configJ.config.getBoolean("disable-farms.water-farm")) {
					for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
						if (block.getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
							event.setCancelled(true);
							break;
						}
					}
				}
			}
		}
	}
}