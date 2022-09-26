package antifarm;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiCactusFarm implements Listener {

	private final AntiFarmPlugin plugin;
	private final Configuration config;

	public AntiCactusFarm(AntiFarmPlugin plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockGrow(BlockGrowEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null) return;
		if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.CACTUS)) return;
		if (!config.getBoolean("farms-settings.prevent-cactus-farms", true)) return;

		Block block = event.getBlock();
		Location bLoc = block.getLocation();
		List<Block> domeBlocks = new ArrayList<Block>();
		Collections.addAll(domeBlocks, block, block.getRelative(BlockFace.UP), block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.WEST));

		for (Block dBlock : domeBlocks) {
			if (!(dBlock.getType().equals(Material.CACTUS)) && !(dBlock.getType().equals(Material.AIR)) && !(dBlock.getType().equals(Material.CAVE_AIR))) {

				event.setCancelled(true);

				if (config.getBoolean("settings.break-blocks", true)) {
					for (int i = 0; i < 4; i++) {

						Block replace = block.getWorld().getBlockAt(bLoc.getBlockX(), bLoc.getBlockY() - i, bLoc.getBlockZ());

						if (replace.getType().equals(Material.CACTUS) || replace.getType().equals(Material.SAND)) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							    	replace.breakNaturally();
							    	replace.setType(Material.AIR);
							}, 1);
						}
					}
				}

				break;
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockPhysics(BlockPhysicsEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || event.getSourceBlock() == null) return;
		if (!event.getSourceBlock().getType().equals(Material.SAND) || event.getSourceBlock().getType().equals(Material.RED_SAND) || event.getSourceBlock().getType().toString().contains("CONCRETE_POWDER") || event.getSourceBlock().getType().equals(Material.DRAGON_EGG)) return;
		if (!event.getBlock().getType().equals(Material.CACTUS)) return;
		if (!config.getBoolean("farms-settings.prevent-cactus-farms", true)) return;

		event.setCancelled(true);
		event.getSourceBlock().breakNaturally();
		event.getSourceBlock().setType(Material.AIR);

		if (!config.getBoolean("settings.break-blocks", true)) return;

		Block block = event.getBlock();
		Location bLoc = block.getLocation();

		for (int i = 0; i < 4; i++) {

			Block replace = block.getWorld().getBlockAt(bLoc.getBlockX(), bLoc.getBlockY() - i, bLoc.getBlockZ());

			if (replace.getType().equals(Material.CACTUS) || replace.getType().equals(Material.SAND)) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				    	replace.breakNaturally();
				    	replace.setType(Material.AIR);
				}, 1);
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPistonExtend(BlockPistonExtendEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || event.getDirection() == null) return;
		if (config.getBoolean("farms-settings.prevent-piston-farms", true)) return;
		if (!config.getBoolean("farms-settings.prevent-cactus-farms", true)) return;

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
		if (config.getBoolean("farms-settings.prevent-piston-farms", true)) return;
		if (!config.getBoolean("farms-settings.prevent-cactus-farms", true)) return;

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
			Block checkBlock = block.getRelative(direction);
			if (checkBlock.getRelative(BlockFace.NORTH).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.EAST).getType().equals(Material.CACTUS) || checkBlock.getRelative(BlockFace.WEST).getType().equals(Material.CACTUS)) {
				return true;
			}
		}

		return false;

	}

}