package antiFarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AntiLightLevelFarm implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getType().equals(Material.WHEAT) || block.getType().equals(Material.BEETROOTS)
				|| block.getType().equals(Material.POTATOES) || block.getType().equals(Material.CARROTS)) {
			if (block.getLightLevel() <= 7) {
				if (J.configJ.config.getBoolean("disable-farms.light-level-farm")) {
					block.breakNaturally();
					block.setType(Material.AIR);
				}
			}
		}
	}

}
