package antifarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.ItemStack;

import core.AntiFarmPlugin;

public class AntiStringDupe implements Listener {

	private final Configuration config;

	public AntiStringDupe(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getPlayer() == null || event.getBlock() == null || !event.getBlock().getType().equals(Material.TRIPWIRE)) return;
		if (!config.getBoolean("farms-settings.prevent-string-dupe", true)) return;

		event.getBlock().setType(Material.AIR);
		event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.STRING));

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockFromTo(BlockFromToEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.getBlock() == null || event.getToBlock() == null) return;
		if (!event.getBlock().getType().equals(Material.WATER) && !event.getBlock().getType().equals(Material.LAVA) && !event.getBlock().getType().toString().contains("TRAPDOOR") || !event.getToBlock().getType().equals(Material.TRIPWIRE)) return;
		if (!config.getBoolean("farms-settings.prevent-string-dupe", true)) return;

		event.setCancelled(true);
		event.getToBlock().setType(Material.AIR);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockPhysics(BlockPhysicsEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || event.getSourceBlock() == null) return;
		if (!event.getSourceBlock().getType().equals(Material.WATER) && !event.getSourceBlock().getType().equals(Material.LAVA) && !event.getSourceBlock().getType().toString().contains("TRAPDOOR") || !event.getBlock().getType().equals(Material.TRIPWIRE)) return;
		if (!config.getBoolean("farms-settings.prevent-string-dupe", true)) return;

		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockDispense(BlockDispenseEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || event.getItem() == null || event.getVelocity() == null || !event.getBlock().getType().equals(Material.DISPENSER)) return;
		if (!event.getItem().getType().equals(Material.WATER_BUCKET) && !event.getItem().getType().equals(Material.LAVA_BUCKET)) return;

		Dispenser dispenser = (Dispenser) event.getBlock().getBlockData();
		Block block = event.getBlock().getRelative(dispenser.getFacing());

		if (!block.getType().equals(Material.TRIPWIRE) || !config.getBoolean("farms-settings.prevent-string-dupe", true)) return;

		event.setCancelled(true);

	}

}
