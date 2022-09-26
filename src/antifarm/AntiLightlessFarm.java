package antifarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Dispenser;
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

public class AntiLightlessFarm implements Listener {

	private final Configuration config;

	public AntiLightlessFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockPlace(BlockPlaceEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null) return;
		if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;
		if (event.getBlock().getLightLevel() > 7) return;
		if (!config.getBoolean("farms-settings.prevent-lightless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlock().getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteract(PlayerInteractEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;

		if (event.getItem() == null || event.getClickedBlock() == null || event.getAction() == null) return;
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if (!event.getItem().getType().equals(Material.BONE_MEAL))  return;
		if (!event.getClickedBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;
		if (event.getClickedBlock().getLightLevel() > 7) return;
		if (!config.getBoolean("farms-settings.prevent-lightless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getClickedBlock().getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockGrow(BlockGrowEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null) return;
		if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;
		if (event.getBlock().getLightLevel() > 7) return;
		if (!config.getBoolean("farms-settings.prevent-lightless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(event.getBlock().getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDispense(BlockDispenseEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || event.getItem() == null) return;
		if (!event.getBlock().getType().equals(Material.DISPENSER)) return;
		if (!event.getItem().getType().equals(Material.BONE_MEAL)) return;

		Dispenser dispenser = (Dispenser) event.getBlock().getBlockData();
		Block block = event.getBlock().getRelative(dispenser.getFacing());

		if (!block.getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND)) return;
		if (block.getLightLevel() >= 7) return;
		if (!config.getBoolean("farms-settings.prevent-lightless-farms", true)) return;
		if (!config.getStringList("farm-blocks").contains(block.getType().toString().toUpperCase())) return;

		event.setCancelled(true);

	}

}
