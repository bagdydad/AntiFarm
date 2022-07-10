package antifarm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;


public class AntiMobSpawner implements Listener {

	private final Configuration config;

	public AntiMobSpawner(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onSpawnerSpawn(SpawnerSpawnEvent event) {

		if (event.isCancelled()) return;
		if (!config.getBoolean("mob-spawner-settings.prevent-spawn", true)) return;

		event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event) {

		if (event.isCancelled() || event.getPlayer() == null || event.getBlock() == null) return;
		if (!event.getBlock().getType().equals(Material.SPAWNER)) return;
		if (!config.getBoolean("mob-spawner-settings.prevent-break", true)) return;

		if (event.getPlayer().hasPermission("antifarm.admin") || event.getPlayer().isOp()) {
			if (!event.getPlayer().isSneaking()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("mob-spawner-settings.admin-warn-message").replaceAll("&", "§"));
			}
		} else {
			event.setCancelled(true);
			event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("mob-spawner-settings.player-warn-message").replaceAll("&", "§"));
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onRightClick(PlayerInteractEvent event) {

		if (event.getItem() == null || event.getPlayer() == null || event.getClickedBlock() == null || event.getAction() == null) return;
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if (!event.getClickedBlock().getType().equals(Material.SPAWNER)) return;
		if (!event.getItem().getType().toString().contains("SPAWN_EGG")) return;
		if (!config.getBoolean("mob-spawner-settings.prevent-transformation", true)) return;

		event.setCancelled(true);
		event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("mob-spawner-settings.transformation-warn-message").replaceAll("&", "§"));

	}

}