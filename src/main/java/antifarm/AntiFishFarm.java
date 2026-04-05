package antifarm;

import configuration.Configuration;
import core.AntiFarmPlugin;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AntiFishFarm implements Listener {

    private static final long clearInterval = 2L * 60 * 60 * 1000; //2h in ms
    private final Configuration config;

    private final HashMap<String, fishingInfo> fishData = new HashMap<>();
    private boolean enabled;
    private Set<String> disabledWorlds;
    private long chunkCooldownMs;
    private int maxFish;
    private boolean warnEnabled;
    private boolean kickEnabled;
    private String warnMsg;
    private String kickMsg;
    private long lastClearMs = System.currentTimeMillis();

    public AntiFishFarm(AntiFarmPlugin plugin) {
        this.config = plugin.getConfig();
        getConfig();
    }

    public void getConfig() {
        enabled = config.getBoolean("anti-fishing.enable", true);
        disabledWorlds = new HashSet<>(config.getStringList("settings.disabled-worlds"));
        chunkCooldownMs = config.getInt("anti-fishing.chunk-cooldown", 600) * 1000L;
        maxFish = config.getInt("anti-fishing.caught-fish-per-chunk", 10);
        warnEnabled = config.getBoolean("anti-fishing.warn", true);
        kickEnabled = config.getBoolean("anti-fishing.kick", false);
        String prefix = config.getString("settings.prefix", "").replace("&", "§");
        warnMsg = prefix + config.getString("anti-fishing.warn-msg", "").replace("&", "§");
        kickMsg = prefix + config.getString("anti-fishing.kick-msg", "").replace("&", "§");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerFish(PlayerFishEvent event) {
        if (!enabled || event.isCancelled()) return;
        Player player = event.getPlayer();
        if (disabledWorlds.contains(player.getWorld().getName())) return;

        long now = System.currentTimeMillis();
        if (now - lastClearMs >= clearInterval) {
            fishData.clear();
            lastClearMs = now;
        }

        Chunk chunk = player.getLocation().getChunk();
        String key = player.getName() + '.' + chunk.getX() + '.' + chunk.getZ();

        fishingInfo stats = fishData.get(key);
        if (stats != null && stats.firstCaughtTime != 0 && (now - stats.firstCaughtTime) >= chunkCooldownMs) {
            fishData.remove(key);
            stats = null;
        }

        if (stats == null) {
            stats = new fishingInfo();
            fishData.put(key, stats);
        }

        long previousCount = stats.count;
        if (event.getState() == State.CAUGHT_FISH) {
            if (stats.firstCaughtTime == 0) {
                stats.firstCaughtTime = now;
            }
            stats.count++;
        }

        if (previousCount >= maxFish) {
            event.setCancelled(true);
            stats.count++;
            if (warnEnabled) {
                player.sendMessage(warnMsg);
            }

            if (kickEnabled && previousCount >= maxFish + 10) {
                player.kickPlayer(kickMsg);
                stats.count = maxFish;
            }
        }
    }

    private static class fishingInfo {
        long count = 0;
        long firstCaughtTime = 0;
    }
}