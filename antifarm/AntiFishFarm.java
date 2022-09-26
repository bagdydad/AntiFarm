package antifarm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiFishFarm implements Listener {

	private final Configuration config;

	public AntiFishFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	private HashMap<String, Integer> fishCount = new HashMap<String, Integer>();
	private HashMap<String, LocalDateTime> fishTime = new HashMap<String, LocalDateTime>();
	private LocalDateTime clearHashMapsTimer;

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerFish(PlayerFishEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;

		if (event.isCancelled() || event.getPlayer() == null || event.getState() == null) return;
		if (!config.getBoolean("anti-fishing.enable", true)) return;

		if (clearHashMapsTimer == null) {
			clearHashMapsTimer = LocalDateTime.now();
		} else if (Duration.between(clearHashMapsTimer, LocalDateTime.now()).toHours() >= 2) {
			fishCount.clear();
			fishTime.clear();
			clearHashMapsTimer = LocalDateTime.now();
		}

		Player player = event.getPlayer();
		Chunk chunk = player.getLocation().getChunk();
		String key = player.getName() + "." + String.valueOf(chunk.getX()) + "." + String.valueOf(chunk.getZ());

		if (fishTime.get(key) != null) {
			if (Duration.between(fishTime.get(key), LocalDateTime.now()).toSeconds() >= config.getInt("anti-fishing.chunk-cooldown", 600)) {
				fishCount.remove(key);
				fishTime.remove(key);
			}
		}

		if (fishCount.get(key) == null) {
			fishCount.put(key, 0);
		}

		int value = fishCount.get(key);

		if (event.getState().equals(State.CAUGHT_FISH)) {
			if (fishTime.get(key) == null) {
				fishTime.put(key, LocalDateTime.now());
			}
			fishCount.replace(key, value, value + 1);
		}

		if (value >= config.getInt("anti-fishing.caught-fish-per-chunk", 10)) {

			event.setCancelled(true);
			fishCount.replace(key, fishCount.get(key), value + 1);

			if (config.getBoolean("anti-fishing.warn", true)) {
				player.sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("anti-fishing.warn-msg").replaceAll("&", "§"));
			}

			if (config.getBoolean("anti-fishing.kick", false)) {
				if (value >= (config.getInt("anti-fishing.caught-fish-per-chunk", 10) + 10)) {
					player.kickPlayer(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("anti-fishing.kick-msg").replaceAll("&", "§"));
					fishCount.replace(key, fishCount.get(key), config.getInt("anti-fishing.caught-fish-per-chunk", 10));
				}
			}

		}

	}

}