package antifarm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiRaidFarm implements Listener {

	private final Configuration config;

	public AntiRaidFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	private HashMap<UUID, LocalDateTime> raidTimer = new HashMap<UUID, LocalDateTime>();

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onRaidTrigger(RaidTriggerEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;

		if (event.isCancelled()) return;
		if (event.getPlayer() == null) return;
		if (!config.getBoolean("prevent-raid-farms.enable", true)) return;

		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		if (raidTimer.get(uuid) == null) {
			raidTimer.put(uuid, LocalDateTime.now());
		} else if (Duration.between(raidTimer.get(uuid), LocalDateTime.now()).toSeconds() >= config.getInt("prevent-raid-farms.cooldown", 600)) {
			raidTimer.remove(uuid);
		} else {
			event.setCancelled(true);
		}

	}

}
