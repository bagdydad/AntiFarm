package antifarm;

import configuration.Configuration;
import core.AntiFarmPlugin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

import java.util.HashSet;
import java.util.Set;

public class AntiSnowballFarm implements Listener {
    private final AntiFarmPlugin plugin;
    private boolean isEnabled;
    private Set<String> disabledWorlds;

    public AntiSnowballFarm(AntiFarmPlugin plugin) {
        this.plugin = plugin;
        reloadConf();
    }

    public void reloadConf() {
        Configuration config = plugin.getConfig();
        this.isEnabled = config.getBoolean("farms-settings.prevent-snowball-farms", true);
        this.disabledWorlds = new HashSet<>(config.getStringList("settings.disabled-worlds"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (!isEnabled) return;
        if (event.getEntity() == null || event.getEntity().getType() != EntityType.SNOW_GOLEM) return;
        if (event.getNewState().getType() != Material.SNOW) return;
        if (disabledWorlds.contains(event.getBlock().getWorld().getName())) return;
        event.setCancelled(true);
    }
}