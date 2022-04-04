package antiFarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class AntiCactusFarm implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockGrow(BlockGrowEvent event) {
		if (event.isCancelled()) return;
		if (event.getBlock() == null) return;
		if (event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.CACTUS)) {
			if (J.configJ.config.getBoolean("prevent-farms.cactus-farms")) {
				Block block = event.getBlock();
				Location bLoc = block.getLocation();
				List<Block> domeBlocks = new ArrayList<Block>();
				Collections.addAll(domeBlocks, block, block.getRelative(BlockFace.UP), block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.WEST));
				for (Block dBlock : domeBlocks) {
					if (!(dBlock.getType().equals(Material.CACTUS)) && !(dBlock.getType().equals(Material.AIR)) && !(dBlock.getType().equals(Material.CAVE_AIR))) {
						event.setCancelled(true);
						if (J.configJ.config.getBoolean("settings.break-blocks")) {
							for (int i = 0; i < 4; i++) {
								Block replace = block.getWorld().getBlockAt(bLoc.getBlockX(), bLoc.getBlockY() - i, bLoc.getBlockZ());
								if (replace.getType().equals(Material.CACTUS) || replace.getType().equals(Material.SAND)) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
									    @Override
									    public void run() {
									    	replace.breakNaturally();
									    	replace.setType(Material.AIR);
									    }
									}, 2);
								}
							}
						}
						break;
					}
				}
			}
		}
	}
	
}