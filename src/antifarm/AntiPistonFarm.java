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

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

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

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

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

			if (config.getStringList("farm-blocks").contains("COCOA")) {
				if (block.getType().equals(Material.JUNGLE_LOG)) {
					if (block.getRelative(BlockFace.EAST).getType().equals(Material.COCOA) || block.getRelative(BlockFace.NORTH).getType().equals(Material.COCOA) || block.getRelative(BlockFace.SOUTH).getType().equals(Material.COCOA) || block.getRelative(BlockFace.WEST).getType().equals(Material.COCOA)) {
						return true;
					}
				}
			}

			if (config.getBoolean("farms-settings.prevent-cactus-farms")) {
				if (config.getStringList("farm-blocks").contains("CACTUS")) {
					Block checkBlock = block.getRelative(direction);
					if (checkBlock.getRelative(BlockFace.NORTH).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.EAST).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.WEST).getType().equals(Material.CACTUS)) {
						return true;
					}
				}
			}

		}

		return false;

	}

}
