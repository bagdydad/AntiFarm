package antiFarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class AntiCactusFarm implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockGrow(BlockGrowEvent event) {
		if (event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.CACTUS)) {
			if (J.configJ.config.getBoolean("disable-farms.cactus-farm")) {
				World world = event.getBlock().getWorld();
				Location loc = event.getBlock().getLocation();
				List<Block> domeBlocks = new ArrayList<Block>();
				Collections.addAll(domeBlocks, world.getBlockAt(loc.getBlockX() + 1, loc.getBlockY(), loc.getBlockZ()),
						world.getBlockAt(loc.getBlockX() - 1, loc.getBlockY(), loc.getBlockZ()),
						world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 1),
						world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 1),
						world.getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()));
				for (Block block : domeBlocks) {
					if (!(block.getType().equals(Material.CACTUS)) && !(block.getType().equals(Material.AIR))) {
						event.setCancelled(true);
						if (J.configJ.config.getBoolean("settings.break-farm-blocks")) {
							world.getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ()).breakNaturally();
							world.getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ()).setType(Material.AIR);
						}
						break;
					}
				}
			}
		}
	}

}
