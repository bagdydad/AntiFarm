package antifarm;

import configuration.Configuration;
import core.AntiFarmPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public class AntiArmadilloFarm implements Listener {
    private final AntiFarmPlugin plugin;
    private boolean disableXp;
    private Set<String> disabledWorlds;

    public AntiArmadilloFarm(AntiFarmPlugin plugin) {
        this.plugin = plugin;
        reloadConf();
    }

    public void reloadConf() {
        Configuration config = plugin.getConfig();
        this.disableXp = config.getBoolean("prevent-armadillo-farm.disable-xp", false);
        this.disabledWorlds = new HashSet<>(config.getStringList("settings.disabled-worlds"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onArmadilloInfested(EntityPotionEffectEvent event) {
        if (!disableXp) return;
        if (event.getEntityType() != EntityType.ARMADILLO) return;
        var effect = event.getNewEffect();
        if (effect == null || effect.getType() != PotionEffectType.INFESTED) return;
        if (!disabledWorlds.isEmpty() && disabledWorlds.contains(event.getEntity().getWorld().getName())) return;
        event.setCancelled(true);
    }
}