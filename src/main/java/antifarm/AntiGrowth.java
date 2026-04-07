package antifarm;

import configuration.Configuration;
import core.AntiFarmPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class AntiGrowth implements Listener {
    private final Configuration config;
    private final Set<Material> disabledMaterials = EnumSet.noneOf(Material.class);
    private boolean preventGrowthEnabled;
    private Set<String> disabledWorlds;

    public AntiGrowth(AntiFarmPlugin plugin) {
        this.config = plugin.getConfig();
        reloadConf();
    }

    public void reloadConf() {
        preventGrowthEnabled = config.getBoolean("growth.prevent-growth", true);
        disabledWorlds = new HashSet<>(config.getStringList("settings.disabled-worlds"));
        disabledMaterials.clear();
        for (String matName : config.getStringList("growth.prevent-growth-of")) {
            try {
                disabledMaterials.add(Material.valueOf(matName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Invalid material in prevent-growth-of: " + matName);
            }
        }
    }

    private boolean shouldCancel(Material newMaterial, String worldName) {
        if (!preventGrowthEnabled) return false;
        if (!disabledMaterials.contains(newMaterial)) return false;
        return !disabledWorlds.contains(worldName);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        if (shouldCancel(event.getNewState().getType(), event.getBlock().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        if (shouldCancel(event.getNewState().getType(), event.getBlock().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (shouldCancel(event.getNewState().getType(), event.getBlock().getWorld().getName())) {
            event.setCancelled(true);
        }
    }
}