package antifarm;

import configuration.Configuration;
import core.AntiFarmPlugin;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.HashSet;
import java.util.Set;

public class AntiBarteringFarm implements Listener {
    private final AntiFarmPlugin plugin;
    private boolean preventBarteringEnabled;
    private boolean disableOnGroundPickup;
    private Set<String> disabledWorlds;

    public AntiBarteringFarm(AntiFarmPlugin plugin) {
        this.plugin = plugin;
        reloadConf();
    }

    public void reloadConf() {
        Configuration config = plugin.getConfig();
        this.preventBarteringEnabled = config.getBoolean("prevent-bartering-farm.enable", true);
        this.disableOnGroundPickup = config.getBoolean("prevent-bartering-farm.disable-on-ground-gold-picking", false);
        this.disabledWorlds = new HashSet<>(config.getStringList("settings.disabled-worlds"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGoldDispense(BlockDispenseEvent event) {
        if (!preventBarteringEnabled) return;
        if (event.getItem() == null || event.getItem().getType() != Material.GOLD_INGOT) return;
        if (event.getBlock().getWorld().getEnvironment() != Environment.NETHER) return;
        Material blockType = event.getBlock().getType();
        if (blockType != Material.DISPENSER && blockType != Material.DROPPER) return;
        if (disabledWorlds.contains(event.getBlock().getWorld().getName())) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPiglinPickup(EntityPickupItemEvent event) {
        if (!disableOnGroundPickup) return;
        if (event.getEntity().getType() != EntityType.PIGLIN) return;
        if (event.getItem().getItemStack().getType() != Material.GOLD_INGOT) return;
        if (disabledWorlds.contains(event.getEntity().getWorld().getName())) return;
        event.setCancelled(true);
    }
}