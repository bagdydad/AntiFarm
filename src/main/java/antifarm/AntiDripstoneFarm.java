package antifarm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiDripstoneFarm implements Listener {

	private final Configuration config;

	public AntiDripstoneFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockPhysics(BlockPhysicsEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || !event.getBlock().getType().equals(Material.POINTED_DRIPSTONE) || !config.getBoolean("farms-settings.prevent-dripstone-farms", true)) return;

		Block block = event.getBlock();
		PointedDripstone dripstone = (PointedDripstone) block.getBlockData();

		if (dripstone.getVerticalDirection().equals(BlockFace.UP)) {
			if (!block.getRelative(BlockFace.DOWN).getType().equals(Material.STONE)) {
				block.setType(Material.AIR);
			}
		}

	}


}
