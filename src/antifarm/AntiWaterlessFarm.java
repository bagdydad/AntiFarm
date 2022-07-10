package antifarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiWaterlessFarm implements Listener {

	private final Configuration config;

	public AntiWaterlessFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockPlace(BlockPlaceEvent event) {

		if (event.isCancelled() || event.getBlock() == null || event.getPlayer() == null) return;
		if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;

		BlockData blockData = event.getBlock().getRelative(BlockFace.DOWN).getBlockData();
		Farmland farmland = (Farmland) blockData;

		if (farmland.getMoisture() != 0) return;
		if (!config.getBoolean("farms-settings.prevent-waterless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlock().getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getPlayer() == null || event.getAction() == null || event.getClickedBlock() == null || event.getItem() == null) return;
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if (!event.getItem().getType().equals(Material.BONE_MEAL)) return;
		if (!event.getClickedBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;

		BlockData blockData = event.getClickedBlock().getRelative(BlockFace.DOWN).getBlockData();
		Farmland farmland = (Farmland) blockData;

		if (farmland.getMoisture() != 0) return;
		if (!config.getBoolean("farms-settings.prevent-waterless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getClickedBlock().getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockGrow(BlockGrowEvent event) {

		if (event.isCancelled() || event.getBlock() == null) return;
		if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;

		BlockData blockData = event.getBlock().getRelative(BlockFace.DOWN).getBlockData();
		Farmland farmland = (Farmland) blockData;

		if (farmland.getMoisture() != 0) return;
		if (!config.getBoolean("farms-settings.prevent-waterless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlock().getType().toString().toUpperCase())) return;

		event.setCancelled(true);
		event.getBlock().breakNaturally();
		event.getBlock().setType(Material.AIR);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDispense(BlockDispenseEvent event) {

		if (event.isCancelled() || event.getBlock() == null || event.getItem() == null) return;
		if (!event.getBlock().getType().equals(Material.DISPENSER)) return;
		if (!event.getItem().getType().equals(Material.BONE_MEAL)) return;

		Dispenser dispenser = (Dispenser) event.getBlock().getBlockData();
		Block block = event.getBlock().getRelative(dispenser.getFacing());

		if (!block.getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;

		Farmland farmland = (Farmland) block.getRelative(BlockFace.DOWN).getBlockData();

		if (farmland.getMoisture() != 0) return;
		if (!config.getBoolean("farms-settings.prevent-waterless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(block.getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

}