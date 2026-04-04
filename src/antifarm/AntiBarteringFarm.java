package antifarm;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiBarteringFarm implements Listener {

    private final Configuration config;

    public AntiBarteringFarm(AntiFarmPlugin plugin) {
        this.config = plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onGoldDispense(BlockDispenseEvent event) {
        if (event.getItem() == null || event.getItem().getType() != Material.GOLD_INGOT) return;
        if (event.getBlock().getWorld().getEnvironment() != Environment.NETHER) return;
        if (!config.getBoolean("prevent-bartering-farm.enable", true)) return;

        Material blockType = event.getBlock().getType();
        if (blockType != Material.DISPENSER && blockType != Material.DROPPER) return;
        if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPiglinPickup(EntityPickupItemEvent event) {
        if (event.getEntity().getType() != EntityType.PIGLIN) return;
        if (event.getItem().getItemStack().getType() != Material.GOLD_INGOT) return;
        if (!config.getBoolean("prevent-bartering-farm.disable-on-ground-gold-picking", false)) return;
        if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;
        event.setCancelled(true);
    }
}