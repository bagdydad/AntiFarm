package antifarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;

import core.AntiFarmPlugin;

public class AntiStringDupe implements Listener {

	private final Configuration config;

	public AntiStringDupe(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockFromTo(BlockFromToEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.getBlock() == null || event.getToBlock() == null || event.getFace() == null) return;
		if (!event.getBlock().getType().equals(Material.WATER)) return;
		if (!config.getBoolean("farms-settings.prevent-string-dupe", true)) return;
		if (!event.getToBlock().getType().equals(Material.TRIPWIRE)) return;

		event.getToBlock().setType(Material.AIR);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDispense(BlockDispenseEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || event.getItem() == null || event.getVelocity() == null || !event.getBlock().getType().equals(Material.DISPENSER)) return;
		if (!config.getBoolean("farms-settings.prevent-string-dupe", true) || !event.getItem().getType().equals(Material.WATER_BUCKET) && !event.getItem().getType().equals(Material.LAVA_BUCKET)) return;

		Dispenser dispenser = (Dispenser) event.getBlock().getBlockData();
		Block block = event.getBlock().getRelative(dispenser.getFacing());

		if (!block.getType().equals(Material.TRIPWIRE)) return;

		block.setType(Material.AIR);

	}


}
