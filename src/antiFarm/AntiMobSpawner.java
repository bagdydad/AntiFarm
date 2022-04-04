package antiFarm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiMobSpawner implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSpawnerSpawn(SpawnerSpawnEvent event) {
		if (event.isCancelled()) return;
		if (J.configJ.config.getBoolean("mob-spawner-settings.prevent-spawn")) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) return;
		if (event.getPlayer() == null) return;
		if (event.getBlock() == null) return;
		if (event.getBlock().getType().equals(Material.SPAWNER)) {
			if (J.configJ.config.getBoolean("mob-spawner-settings.prevent-break")) {
				if (event.getPlayer().hasPermission("antifarm.admin") || event.getPlayer().isOp()) {
					if (!event.getPlayer().isSneaking()) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("mob-spawner-settings.admin-warn-message").replaceAll("&", "§"));
					}
				} else {
					event.setCancelled(true);
					event.getPlayer().sendMessage(J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("mob-spawner-settings.player-warn-message").replaceAll("&", "§"));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRightClick(PlayerInteractEvent event) {
		if (event.getItem() == null) return;
		if (event.getPlayer() == null) return;
		if (event.getClickedBlock() == null) return;
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getType().equals(Material.SPAWNER)) {
				if (event.getItem().getType().toString().contains("SPAWN_EGG")) {
					if (J.configJ.config.getBoolean("mob-spawner-settings.prevent-transformation")) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("mob-spawner-settings.transformation-warn-message").replaceAll("&", "§"));
					}
				}
			}
		}
	}
	
}