package antiFarm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class AntiMobSpawner implements Listener {

	@EventHandler
	public void onSpawnerSpawn(SpawnerSpawnEvent event) {
		if (event.isCancelled())
			return;
		if (!J.configJ.config.getBoolean("disable-mob-spawner.spawn-prevent"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		if (event.getPlayer() == null)
			return;
		if (event.getBlock().getType() != Material.SPAWNER)
			return;
		if (!J.configJ.config.getBoolean("disable-mob-spawner.break-prevent"))
			return;
		if (event.getPlayer().hasPermission("antifarm.admin") || event.getPlayer().isOp()) {
			if (!event.getPlayer().isSneaking()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(J.configJ.config.getString("settings.prefix").replaceAll("&", "§")
						+ J.configJ.config.getString("disable-mob-spawner.admin-warn-message").replaceAll("&", "§"));
			}
		} else {
			event.setCancelled(true);
			event.getPlayer().sendMessage(J.configJ.config.getString("settings.prefix").replaceAll("&", "§")
					+ J.configJ.config.getString("disable-mob-spawner.player-warn-message").replaceAll("&", "§"));
		}
	}

}