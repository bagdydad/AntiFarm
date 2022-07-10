package antifarm;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiRaidFarm implements Listener {

	private final Configuration config;

	public AntiRaidFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	private Cache<UUID, Long> raidTimer;

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onRaidTrigger(RaidTriggerEvent event) {

		if (event.isCancelled()) return;
		if (event.getPlayer() == null) return;
		if (!config.getBoolean("prevent-raid-farms.enable", true)) return;

		if (raidTimer == null) {
			raidTimer = CacheBuilder.newBuilder().expireAfterWrite(config.getInt("prevent-raid-farms.cooldown", 600), TimeUnit.SECONDS).build();
		}

		if (raidTimer.getIfPresent(event.getPlayer().getUniqueId()) != null) {
			event.setCancelled(true);
		} else {
			raidTimer.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
		}

	}

}
