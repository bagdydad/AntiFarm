package antifarm;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiArmadilloXPFarm implements Listener {
    private final Configuration config;

    public AntiArmadilloFarm(AntiFarmPlugin plugin) {
        this.config = plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!config.getBoolean("xp-farms.prevent-armadillo-xp-farm", true)) return;
        if (event.isCancelled() || event.getNewEffect() == null) return;
        if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;
        if (event.getEntity().getType() == EntityType.ARMADILLO && event.getNewEffect().getType() == PotionEffectType.INFESTED) {
            event.setCancelled(true);
        }
    }
}