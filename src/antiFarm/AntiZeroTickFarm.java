package antiFarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import core.J;

public class AntiZeroTickFarm implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		if (event.isCancelled()) return;
		if (J.configJ.config.getBoolean("prevent-farms.zerotick-farms", true)) {
			Block piston = event.getBlock();
			BlockFace direction = event.getDirection();
			List<Block> pistonBlocks = new ArrayList<Block>();
			if (event.getBlocks().isEmpty()) {
				pistonBlocks = Arrays.asList(event.getBlock());
			} else {
				pistonBlocks = event.getBlocks();
			}
			event.setCancelled(checkPistonBlocks(piston, direction, pistonBlocks));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonRetract(BlockPistonRetractEvent event) {
		if (event.isCancelled()) return;
		if (J.configJ.config.getBoolean("prevent-farms.zerotick-farms", true)) {
			Block piston = event.getBlock();
			BlockFace direction = event.getDirection();
			List<Block> pistonBlocks = new ArrayList<Block>();
			if (event.getBlocks().isEmpty()) {
				pistonBlocks = Arrays.asList(event.getBlock());
			} else {
				pistonBlocks = event.getBlocks();
			}
			event.setCancelled(checkPistonBlocks(piston, direction, pistonBlocks));
		}
	}
	
	public boolean checkPistonBlocks(Block piston, BlockFace direction, List<Block> pistonBlocks) {
		for (Block block : pistonBlocks) {
			if (block.getType().equals(Material.GRASS_BLOCK) || block.getType().equals(Material.SAND) || block.getType().equals(Material.RED_SAND) || block.getType().equals(Material.FARMLAND) || block.getType().equals(Material.SOUL_SAND) || block.getType().equals(Material.END_STONE)) {
				if (!block.getRelative(BlockFace.UP).getType().equals(Material.AIR) && !block.getRelative(BlockFace.UP).getType().equals(block.getType())) {
					if (J.configJ.config.getStringList("farm-blocks").contains(block.getRelative(BlockFace.UP).getType().toString())) {
						if (J.configJ.config.getBoolean("settings.break-pistons", true)) {
							piston.breakNaturally();
							piston.setType(Material.AIR);
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
