package antifarm;

import java.util.Random;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.persistence.PersistentDataType;

import core.AntiFarmPlugin;

public class AntiChickenEggFarm implements Listener {

	private final Configuration config;
	private final AntiFarmPlugin plugin;
	Random random = new Random();

	public AntiChickenEggFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityDropItem(EntityDropItemEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.getEntity() == null || event.getItemDrop() == null || !event.getEntityType().equals(EntityType.CHICKEN) || !event.getItemDrop().getType().equals(EntityType.EGG) || !config.getBoolean("creature-product-limiter.chicken.enable", true)) return;

		Entity chicken = event.getEntity();
		int currentEgg = chicken.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "currentEgg"), PersistentDataType.INTEGER, 0);
		int maxEgg = chicken.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "maxEgg"), PersistentDataType.INTEGER, 0);

		if (maxEgg == 0) {
			maxEgg = random.nextInt(config.getInt("creature-product-limiter.chicken.egg-max", 10) - config.getInt("creature-product-limiter.chicken.egg-min", 5)) + config.getInt("creature-product-limiter.chicken.egg-min", 5);
			chicken.getPersistentDataContainer().set(new NamespacedKey(plugin, "maxEgg"), PersistentDataType.INTEGER, maxEgg);
		}

		if (currentEgg < maxEgg) {
			chicken.getPersistentDataContainer().set(new NamespacedKey(plugin, "currentEgg"), PersistentDataType.INTEGER, currentEgg + 1);
		} else {
			event.setCancelled(true);
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getHumanEntity() == null || event.getEntity() == null || !config.getBoolean("creature-product-limiter.chicken.enable") || !event.getEntity().getType().equals(EntityType.CHICKEN)) return;

		Entity chicken = event.getEntity();
		int currentEgg = chicken.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "currentEgg"), PersistentDataType.INTEGER, 0);
		int maxEgg = chicken.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "maxEgg"), PersistentDataType.INTEGER, 0);

		if (currentEgg >= maxEgg) {
			maxEgg = random.nextInt(config.getInt("creature-product-limiter.chicken.egg-max", 10) - config.getInt("creature-product-limiter.chicken.egg-min", 5)) + config.getInt("creature-product-limiter.chicken.egg-min", 5);
			chicken.getPersistentDataContainer().set(new NamespacedKey(plugin, "currentEgg"), PersistentDataType.INTEGER, 0);
			chicken.getPersistentDataContainer().set(new NamespacedKey(plugin, "maxEgg"), PersistentDataType.INTEGER, maxEgg);
			event.getHumanEntity().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("creature-product-limiter.chicken.feed-msg").replaceAll("&", "§"));
		}

	}

}
