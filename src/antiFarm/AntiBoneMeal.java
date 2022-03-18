package antiFarm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class AntiBoneMeal implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDispense(BlockDispenseEvent event) {
		if (event.getBlock().getType().equals(Material.DISPENSER)) {
			if (event.getItem().getType().equals(Material.BONE_MEAL)) {
				if (J.configJ.config.getBoolean("settings.block-dispense-bonemeal")) {
					event.setCancelled(true);
				}
			}
		}
	}

}
