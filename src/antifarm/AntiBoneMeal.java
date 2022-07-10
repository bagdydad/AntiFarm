package antifarm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiBoneMeal implements Listener {

	private final Configuration config;

	public AntiBoneMeal(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDispense(BlockDispenseEvent event) {

		if (event.isCancelled() || event.getBlock() == null || event.getItem() == null) return;
		if (!event.getBlock().getType().equals(Material.DISPENSER)) return;
		if (!event.getItem().getType().equals(Material.BONE_MEAL)) return;
		if (!config.getBoolean("dispenser-settings.prevent-bonemeal", true)) return;

		event.setCancelled(true);

	}

}
