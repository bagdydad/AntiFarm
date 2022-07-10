package antifarm;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Frog.Variant;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiFroglightFarm implements Listener {

	private final AntiFarmPlugin plugin;
	private final Configuration config;

	public AntiFroglightFarm(AntiFarmPlugin plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityDeath(EntityDeathEvent event) {

		if (event.getEntity() == null || event.getEntity().getKiller() != null) return;
		if (!event.getEntity().getType().equals(EntityType.MAGMA_CUBE)) return;
		if (!config.getBoolean("prevent-froglight-farms.enable")) return;

		MagmaCube magmaCube = (MagmaCube) event.getEntity();

		if (magmaCube.getSize() != 1) return;

		event.getDrops().clear();

		Collection<Entity> entities = event.getEntity().getNearbyEntities(1, 1, 1);

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (magmaCube.getPose().equals(Pose.DYING)) {

            	String killerUID = magmaCube.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "killerUID"), PersistentDataType.STRING, "none");

        		for (Entity entity : entities) {
        			if (entity.getType().equals(EntityType.FROG)) {
        				if (entity.getUniqueId().toString().equals(killerUID)) {

        					Frog frog = (Frog) entity;
        					Variant variant = frog.getVariant();
        					Location location = frog.getLocation();

        					int count = entity.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "maxTongue"), PersistentDataType.INTEGER, 0);

        					if (count < config.getInt("prevent-froglight-farms.count")) {

        						frog.getPersistentDataContainer().set(new NamespacedKey(plugin, "maxTongue"), PersistentDataType.INTEGER, count + 1);

        						if (variant.equals(Variant.WARM)) {
            						location.getWorld().dropItem(location, new ItemStack(Material.PEARLESCENT_FROGLIGHT, 1));
            					} else if (variant.equals(Variant.TEMPERATE)) {
            						location.getWorld().dropItem(location, new ItemStack(Material.OCHRE_FROGLIGHT, 1));
            					} else if (variant.equals(Variant.COLD)) {
            						location.getWorld().dropItem(location, new ItemStack(Material.VERDANT_FROGLIGHT, 1));
            					}

        					}
        				}
        			}
        		}
            }
        }, 1);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDamage(EntityDamageByEntityEvent event) {

		if (event.isCancelled() || event.getEntity() == null || event.getDamager() == null) return;

		Entity attacker = event.getDamager();
		Entity victim = event.getEntity();

		if (!attacker.getType().equals(EntityType.FROG)) return;
		if (!victim.getType().equals(EntityType.MAGMA_CUBE)) return;
		if (!config.getBoolean("prevent-froglight-farms.enable")) return;

		MagmaCube magmaCube = (MagmaCube) victim;
		Frog frog = (Frog) attacker;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (magmaCube.getPose().equals(Pose.DYING)) {
            	magmaCube.getPersistentDataContainer().set(new NamespacedKey(plugin, "killerUID"), PersistentDataType.STRING, frog.getUniqueId().toString());
            }
        }, 1);

	}

}
