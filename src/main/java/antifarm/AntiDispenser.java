package antifarm;

import configuration.Configuration;
import core.AntiFarmPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockShearEntityEvent;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class AntiDispenser implements Listener {
    private final Configuration config;
    private boolean enabled;
    private boolean preventShearing;
    private Set<String> disabledWorlds;
    private EnumSet<Material> blockedItems;
    private EnumSet<Material> farmBlocks;

    public AntiDispenser(AntiFarmPlugin plugin) {
        this.config = plugin.getConfig();
        reloadConf();
    }

    public void reloadConf() {
        this.enabled = config.getBoolean("anti-dispenser.enable", false);
        this.preventShearing = config.getBoolean("anti-dispenser.prevent-shearing", true);
        this.disabledWorlds = new HashSet<>(config.getStringList("settings.disabled-worlds"));
        this.blockedItems = EnumSet.noneOf(Material.class);
        for (String item : config.getStringList("anti-dispenser.blocked-item-list")) {
            Material mat = Material.getMaterial(item.toUpperCase());
            if (mat != null) blockedItems.add(mat);
        }
        this.farmBlocks = EnumSet.noneOf(Material.class);
        for (String block : config.getStringList("farm-blocks")) {
            Material mat = Material.getMaterial(block.toUpperCase());
            if (mat != null) farmBlocks.add(mat);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onDispense(BlockDispenseEvent event) {
        if (!enabled) return;
        if (event.getBlock().getType() != Material.DISPENSER) return;
        if (!blockedItems.contains(event.getItem().getType())) return;
        if (disabledWorlds.contains(event.getBlock().getWorld().getName())) return;
        Dispenser dispenser = (Dispenser) event.getBlock().getBlockData();
        Block block = event.getBlock().getRelative(dispenser.getFacing());
        if (!farmBlocks.contains(block.getType())) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockShearEntity(BlockShearEntityEvent event) {
        if (!enabled || !preventShearing) return;
        if (event.getEntity().getType() != EntityType.SHEEP) return;
        if (disabledWorlds.contains(event.getBlock().getWorld().getName())) return;
        event.setCancelled(true);
    }
}