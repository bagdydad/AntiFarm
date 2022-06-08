package antiFarm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import core.J;

public class AntiBoneMeal implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDispense(BlockDispenseEvent event) {
		if (event.isCancelled()) return;
		if (event.getItem() == null) return;
		if (event.getBlock().getType().equals(Material.DISPENSER)) {
			if (event.getItem().getType().equals(Material.BONE_MEAL)) {
				if (J.configJ.config.getBoolean("settings.prevent-dispense-bonemeal", true)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}