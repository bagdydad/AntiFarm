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

public class AntiFishFarm implements Listener {

	HashMap<String, Integer> fishCount = new HashMap<String, Integer>();
	HashMap<String, LocalDateTime> fishTime = new HashMap<String, LocalDateTime>();
	LocalDateTime clearHashMapsTime;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFish(PlayerFishEvent event) {
		if (event.getPlayer() == null)
			return;
		if (!J.configJ.config.getBoolean("disable-fish-farms.enable"))
			return;
		Player player = event.getPlayer();
		Chunk chunk = player.getLocation().getChunk();
		String key = player.getName() + "." + chunk.getX() + "." + chunk.getZ();
		if (fishCount.get(key) == null) {
			fishCount.put(key, 0);
		}
		Integer value = fishCount.get(key);
		if (event.getState().equals(State.CAUGHT_FISH)) {
			if (value < J.configJ.config.getInt("disable-fish-farms.caught-fish-per-chunk")) {
				fishCount.replace(key, value, value + 1);
			}
			if (clearHashMapsTime == null) {
				clearHashMapsTime = LocalDateTime.now();
			}
			LocalDateTime chmOldT = clearHashMapsTime;
			LocalDateTime chmNowT = LocalDateTime.now();
			Duration chmD = Duration.between(chmOldT, chmNowT);
			if (chmD.toHours() >= 2) {
				fishCount.clear();
				fishTime.clear();
			}
		} else if (event.getState().equals(State.FISHING)) {
			if (value != null && value >= J.configJ.config.getInt("disable-fish-farms.caught-fish-per-chunk")) {
				if (fishTime.get(key) == null) {
					fishTime.put(key, LocalDateTime.now());
				}
				LocalDateTime rmvKeyOldT = fishTime.get(key);
				LocalDateTime rmvKeyNowT = LocalDateTime.now();
				Duration rmvKeyD = Duration.between(rmvKeyOldT, rmvKeyNowT);
				if (rmvKeyD.toSeconds() >= J.configJ.config.getInt("disable-fish-farms.chunk-cooldown")) {
					fishCount.remove(key);
					fishTime.remove(key);
				} else {
					event.setCancelled(true);
					if (J.configJ.config.getBoolean("disable-fish-farms.kick")) {
						player.sendMessage(
								J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("disable-fish-farms.warn-msg").replaceAll("&", "§"));
						fishCount.replace(key, fishCount.get(key), value + 1);
						if (value >= (J.configJ.config.getInt("disable-fish-farms.caught-fish-per-chunk") + 10)) {
							player.kickPlayer(
									J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("disable-fish-farms.kick-msg").replaceAll("&", "§"));
							fishCount.replace(key, fishCount.get(key),
									J.configJ.config.getInt("disable-fish-farms.caught-fish-per-chunk"));
						}
					} else if (J.configJ.config.getBoolean("disable-fish-farms.warn")) {
						player.sendMessage(
								J.configJ.config.getString("settings.prefix").replaceAll("&", "§") + J.configJ.config.getString("disable-fish-farms.warn-msg").replaceAll("&", "§"));
					}
				}
			}
		}
	}

}
