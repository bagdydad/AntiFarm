package antifarm;

import java.util.Collection;
import java.util.Random;

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
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiFroglightFarm implements Listener {

	private final AntiFarmPlugin plugin;
	private final Configuration config;
	Random random = new Random();

	public AntiFroglightFarm(AntiFarmPlugin plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityDeath(EntityDeathEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.getEntity() == null || event.getEntity().getKiller() != null || !event.getEntity().getType().equals(EntityType.MAGMA_CUBE) || !config.getBoolean("creature-product-limiter.frog.enable", true)) return;

		MagmaCube magmaCube = (MagmaCube) event.getEntity();

		if (magmaCube.getSize() != 1) return;

		event.getDrops().clear();

		Collection<Entity> entities = event.getEntity().getNearbyEntities(1, 1, 1);

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (magmaCube.getPose().equals(Pose.DYING)) {

            	String killerUID = magmaCube.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "killerUID"), PersistentDataType.STRING, null);

        		for (Entity entity : entities) {
        			if (entity.getType().equals(EntityType.FROG)) {
        				if (entity.getUniqueId().toString().equals(killerUID)) {

        					Frog frog = (Frog) entity;
        					Variant variant = frog.getVariant();
        					Location location = frog.getLocation();

        					int currentTongue = frog.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "currentTongue"), PersistentDataType.INTEGER, 0);
        					int maxTongue = frog.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "maxTongue"), PersistentDataType.INTEGER, 0);

        					if (maxTongue == 0) {
        						maxTongue = random.nextInt(config.getInt("creature-product-limiter.frog.froglight-max", 10) - config.getInt("creature-product-limiter.frog.froglight-min", 5)) + config.getInt("creature-product-limiter.frog.froglight-min", 5);
        						frog.getPersistentDataContainer().set(new NamespacedKey(plugin, "maxTongue"), PersistentDataType.INTEGER, maxTongue);
        					}

        					if (currentTongue < maxTongue) {

        						frog.getPersistentDataContainer().set(new NamespacedKey(plugin, "currentTongue"), PersistentDataType.INTEGER, currentTongue + 1);

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

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getEntity() == null || event.getDamager() == null) return;

		Entity attacker = event.getDamager();
		Entity victim = event.getEntity();

		if (!attacker.getType().equals(EntityType.FROG) || !victim.getType().equals(EntityType.MAGMA_CUBE) || !config.getBoolean("creature-product-limiter.frog.enable", true)) return;

		Entity magmaCube = victim;
		Entity frog = attacker;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (magmaCube.getPose().equals(Pose.DYING)) {
            	magmaCube.getPersistentDataContainer().set(new NamespacedKey(plugin, "killerUID"), PersistentDataType.STRING, frog.getUniqueId().toString());
            }
        }, 1);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getHumanEntity() == null || event.getEntity() == null || !config.getBoolean("creature-product-limiter.frog.enable") || !event.getEntity().getType().equals(EntityType.FROG)) return;

		Entity frog = event.getEntity();
		int currentTongue = frog.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "currentTongue"), PersistentDataType.INTEGER, 0);
		int maxTongue = frog.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "maxTongue"), PersistentDataType.INTEGER, 0);

		if (currentTongue >= maxTongue) {
			maxTongue = random.nextInt(config.getInt("creature-product-limiter.frog.froglight-max", 10) - config.getInt("creature-product-limiter.frog.froglight-min", 5)) + config.getInt("creature-product-limiter.frog.froglight-min", 5);
			frog.getPersistentDataContainer().set(new NamespacedKey(plugin, "currentTongue"), PersistentDataType.INTEGER, 0);
			frog.getPersistentDataContainer().set(new NamespacedKey(plugin, "maxTongue"), PersistentDataType.INTEGER, maxTongue);
			event.getHumanEntity().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("creature-product-limiter.frog.feed-msg").replaceAll("&", "§"));
		}

	}

}
