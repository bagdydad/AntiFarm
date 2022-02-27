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

public class AntiPistonFarm extends Config implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		if (event.isCancelled())
			return;
		if (!J.configJ.config.getBoolean("disable-farms.piston-farm"))
			return;
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonRetract(BlockPistonRetractEvent event) {
		if (event.isCancelled())
			return;
		if (!J.configJ.config.getBoolean("disable-farms.piston-farm"))
			return;
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

	public boolean checkPistonBlocks(Block piston, BlockFace direction, List<Block> pistonBlocks) {
		for (Block block : pistonBlocks) {
			for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
				if (block.getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
					if (J.configJ.config.getBoolean("settings.break-pistons")) {
						piston.breakNaturally();
						piston.setType(Material.AIR);
					}
					return true;
				}
			}
			if (block.getType().equals(Material.SAND) || block.getType().equals(Material.RED_SAND)
					|| block.getType().equals(Material.FARMLAND) || block.getType().equals(Material.SOUL_SAND)
					|| block.getType().equals(Material.END_STONE)) {
				if (!block.getRelative(BlockFace.UP).getType().equals(Material.AIR) && !block.getRelative(BlockFace.UP).getType().equals(block.getType())) {
					for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
						if (block.getRelative(BlockFace.UP).getType().toString().equals(checkBlock.toUpperCase())) {
							if (J.configJ.config.getBoolean("settings.break-pistons")) {
								piston.breakNaturally();
								piston.setType(Material.AIR);
							}
							return true;
						}
					}
				}
			} else if (block.getType().equals(Material.JUNGLE_LOG)) {
				for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
					if (block.getRelative(BlockFace.EAST).getType().toString().equals(checkBlock.toUpperCase())
							|| block.getRelative(BlockFace.NORTH).getType().toString().equals(checkBlock.toUpperCase())
							|| block.getRelative(BlockFace.SOUTH).getType().toString().equals(checkBlock.toUpperCase())
							|| block.getRelative(BlockFace.WEST).getType().toString().equals(checkBlock.toUpperCase())) {
						if (J.configJ.config.getBoolean("settings.break-pistons")) {
							piston.breakNaturally();
							piston.setType(Material.AIR);
						}
						return true;
					}
				}
			}
			if (J.configJ.config.getBoolean("disable-farms.cactus-farm")) {
				Block checkBlock = block.getRelative(direction);
				if (checkBlock.getRelative(BlockFace.NORTH).getType().equals(Material.CACTUS)
						|| checkBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.CACTUS)
						|| checkBlock.getRelative(BlockFace.EAST).getType().equals(Material.CACTUS)
						|| checkBlock.getRelative(BlockFace.WEST).getType().equals(Material.CACTUS)) {
					if (J.configJ.config.getBoolean("settings.break-pistons")) {
						piston.breakNaturally();
						piston.setType(Material.AIR);
					}
					return true;
				}
			}
		}
		return false;
	}

}
