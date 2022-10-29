package antifarm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent.ChangeReason;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiLavaFarm implements Listener {

	private final Configuration config;
	private final AntiFarmPlugin plugin;

	public AntiLavaFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onCauldronLevelChange(CauldronLevelChangeEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getBlock() == null || !event.getBlock().getType().equals(Material.CAULDRON) || !event.getReason().equals(ChangeReason.NATURAL_FILL) || !config.getBoolean("farms-settings.prevent-infinity-lava", true)) return;

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				if (!event.getBlock().getType().equals(Material.LAVA_CAULDRON)) return;
				for (int i = 1; i < 25; i++) {
					Block block = new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY() + i, event.getBlock().getZ()).getBlock();
					if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.CAVE_AIR) && !block.getType().equals(Material.POINTED_DRIPSTONE) && !block.getType().equals(Material.DRIPSTONE_BLOCK) && !block.getType().equals(Material.LAVA)) return;
					if (block.getType().equals(Material.LAVA)) {
						block.setType(Material.AIR);
						return;
					}
				}
			}
		}, 2);



	}

}
