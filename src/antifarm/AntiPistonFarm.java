package antifarm;

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

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiPistonFarm implements Listener {

	private final Configuration config;

	public AntiPistonFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPistonExtend(BlockPistonExtendEvent event) {

		if (event.isCancelled() || event.getBlock() == null || event.getDirection() == null) return;
		if (!config.getBoolean("farms-settings.prevent-piston-farms", true)) return;

		Block piston = event.getBlock();
		BlockFace direction = event.getDirection();
		List<Block> pistonBlocks = new ArrayList<Block>();

		if (event.getBlocks().isEmpty()) {
			pistonBlocks = Arrays.asList(event.getBlock());
		} else {
			pistonBlocks = event.getBlocks();
		}

		if (!checkPistonBlocks(piston, direction, pistonBlocks)) return;

		event.setCancelled(true);

		if (!config.getBoolean("settings.break-pistons", true)) return;

		piston.breakNaturally();
		piston.setType(Material.AIR);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPistonRetract(BlockPistonRetractEvent event) {

		if (event.isCancelled() || event.getBlock() == null || event.getDirection() == null) return;
		if (!config.getBoolean("farms-settings.prevent-piston-farms", true)) return;

		Block piston = event.getBlock();
		BlockFace direction = event.getDirection();
		List<Block> pistonBlocks = new ArrayList<Block>();

		if (event.getBlocks().isEmpty()) {
			pistonBlocks = Arrays.asList(event.getBlock());
		} else {
			pistonBlocks = event.getBlocks();
		}

		if (!checkPistonBlocks(piston, direction, pistonBlocks)) return;

		event.setCancelled(true);

		if (!config.getBoolean("settings.break-pistons", true)) return;

		piston.breakNaturally();
		piston.setType(Material.AIR);

	}

	private boolean checkPistonBlocks(Block piston, BlockFace direction, List<Block> pistonBlocks) {

		for (Block block : pistonBlocks) {
			for (String checkBlock : config.getStringList("farm-blocks")) {
				if (block.getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
					return true;
				}
			}
			if (block.getType().equals(Material.GRASS_BLOCK) || block.getType().equals(Material.SAND) || block.getType().equals(Material.RED_SAND) || block.getType().equals(Material.FARMLAND) || block.getType().equals(Material.SOUL_SAND) || block.getType().equals(Material.END_STONE)) {
				if (!block.getRelative(BlockFace.UP).getType().equals(Material.AIR) && !block.getRelative(BlockFace.UP).getType().equals(block.getType())) {
					if (config.getStringList("farm-blocks").contains(block.getRelative(BlockFace.UP).getType().toString())) {
						return true;
					}
				}
			} else if (block.getType().equals(Material.JUNGLE_LOG)) {
				for (String checkBlock : config.getStringList("farm-blocks")) {
					if (block.getRelative(BlockFace.EAST).getType().toString().equals(checkBlock.toUpperCase()) || block.getRelative(BlockFace.NORTH).getType().toString().equals(checkBlock.toUpperCase()) || block.getRelative(BlockFace.SOUTH).getType().toString().equals(checkBlock.toUpperCase()) || block.getRelative(BlockFace.WEST).getType().toString().equals(checkBlock.toUpperCase())) {
						return true;
					}
				}
			}
			if (config.getBoolean("farms-settings.prevent-cactus-farms")) {
				Block checkBlock = block.getRelative(direction);
				if (checkBlock.getRelative(BlockFace.NORTH).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.EAST).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.WEST).getType().equals(Material.CACTUS)) {
					return true;
				}
			}
		}

		return false;

	}

}
