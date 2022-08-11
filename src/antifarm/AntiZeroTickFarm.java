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

public class AntiZeroTickFarm implements Listener {

	private final Configuration config;

	public AntiZeroTickFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPistonExtend(BlockPistonExtendEvent event) {

		if (event.isCancelled()) return;
		if (!config.getBoolean("farms-settings.prevent-zerotick-farms", true)) return;

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

		if (event.isCancelled()) return;
		if (!config.getBoolean("farms-settings.prevent-zerotick-farms", true)) return;

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
			if (config.getStringList("farmland-blocks").contains(block.getType().toString().toUpperCase())) {
				if (config.getStringList("farm-blocks").contains(block.getRelative(BlockFace.UP).getType().toString())) {
					return true;
				}
			}
		}

		return false;

	}

}
