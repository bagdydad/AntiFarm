package antiFarm;

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

import core.J;

public class AntiFishFarm implements Listener {

	HashMap<String, Integer> fishCount = new HashMap<String, Integer>();
	HashMap<String, LocalDateTime> fishTime = new HashMap<String, LocalDateTime>();
	LocalDateTime clearHashMapsTimer;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFish(PlayerFishEvent event) {
		if (event.isCancelled()) return;
		if (event.getPlayer() == null) return;
		if (J.configJ.config.getBoolean("anti-fishing.enable", true)) {
			if (clearHashMapsTimer == null) {
				clearHashMapsTimer = LocalDateTime.now();
			} else if (Duration.between((LocalDateTime) clearHashMapsTimer, (LocalDateTime) LocalDateTime.now()).toHours() >= 2) {
				fishCount.clear();
				fishTime.clear();
				clearHashMapsTimer = LocalDateTime.now();
			}
			Player player = event.getPlayer();
			Chunk chunk = player.getLocation().getChunk();
			String key = player.getName() + "." + String.valueOf(chunk.getX()) + "." + String.valueOf(chunk.getZ());
			if (fishTime.get(key) != null) {
				if (Duration.between((LocalDateTime) fishTime.get(key), (LocalDateTime) LocalDateTime.now()).toSeconds() >= J.configJ.config.getInt("anti-fishing.chunk-cooldown", 600)) {
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
			if (value >= J.configJ.config.getInt("anti-fishing.caught-fish-per-chunk", 10)) {
				event.setCancelled(true);
				fishCount.replace(key, fishCount.get(key), value + 1);
				if (J.configJ.config.getBoolean("anti-fishing.warn", true)) {
					player.sendMessage(J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("anti-fishing.warn-msg").replaceAll("&", "§"));
				}
				if (J.configJ.config.getBoolean("anti-fishing.kick", false)) {
					if (value >= (J.configJ.config.getInt("anti-fishing.caught-fish-per-chunk", 10) + 10)) {
						player.kickPlayer(J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("anti-fishing.kick-msg").replaceAll("&", "§"));
						fishCount.replace(key, fishCount.get(key), J.configJ.config.getInt("anti-fishing.caught-fish-per-chunk", 10));
					}
				}
			}
		}
	}
	
}