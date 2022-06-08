package antiFarm;

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

import core.J;

public class AntiWaterFarm implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.getBlock().getType().equals(Material.WATER)) {
			if (J.configJ.config.getBoolean("prevent-farms.water-harvesting-farms", true)) {
				if (J.configJ.config.getStringList("farm-blocks").contains(event.getBlock().getRelative(event.getBlock().getFace(event.getToBlock())).getType().toString().toUpperCase())) {
					event.setCancelled(true);
					if (J.configJ.config.getBoolean("settings.break-blocks", true)) {
						event.getBlock().getRelative(event.getBlock().getFace(event.getToBlock())).setType(Material.AIR);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if (event.getBucket().equals(Material.WATER_BUCKET)) {
			if (J.configJ.config.getBoolean("prevent-farms.water-harvesting-farms", true)) {
				if (J.configJ.config.getStringList("farm-blocks").contains(event.getBlockClicked().getRelative(event.getBlockFace()).getType().toString().toUpperCase())) {
					event.setCancelled(true);
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
				if (block.getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) {
					if (J.configJ.config.getBoolean("prevent-farms.water-harvesting-farms", true)) {
						if (J.configJ.config.getStringList("farm-blocks").contains(block.getType().toString().toUpperCase())) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
}