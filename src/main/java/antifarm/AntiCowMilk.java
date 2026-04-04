package antifarm;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import core.AntiFarmPlugin;

public class AntiCowMilk implements Listener {

	private final Configuration config;
	private final AntiFarmPlugin plugin;
	Random random = new Random();

	public AntiCowMilk(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;

		if (event.isCancelled() || event.getPlayer() == null || event.getRightClicked() == null || !config.getBoolean("creature-product-limiter.cow.enable") || !event.getRightClicked().getType().equals(EntityType.COW)) return;

		Entity cow = event.getRightClicked();
		int milked = cow.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "milked"), PersistentDataType.INTEGER, 0);
		Long lastFeed = cow.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "lastFeed"), PersistentDataType.LONG, new Date().getTime() - 600000);
		int elapsedTime = (int) (Duration.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(lastFeed), TimeZone.getDefault().toZoneId()), LocalDateTime.now()).toSeconds());
		int cooldown = config.getInt("creature-product-limiter.cow.milk-cooldown-sec", 600);
		int remainingTime =  cooldown - elapsedTime;

		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BUCKET) || event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BUCKET)) {
			if (milked == 0 && elapsedTime >= cooldown) {
				cow.getPersistentDataContainer().set(new NamespacedKey(plugin, "milked"), PersistentDataType.INTEGER, 1);
				event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + (config.getString("creature-product-limiter.cow.milk-msg").replaceAll("%time%", String.valueOf(remainingTime)).replaceAll("&", "§")));
			} else if (elapsedTime < cooldown) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + (config.getString("creature-product-limiter.cow.cooldown-msg").replaceAll("%time%", String.valueOf(remainingTime)).replaceAll("&", "§")));
			} else if (milked == 1){
				event.setCancelled(true);
				event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + (config.getString("creature-product-limiter.cow.malnutrition-warning-msg").replaceAll("%time%", String.valueOf(remainingTime)).replaceAll("&", "§")));
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getHumanEntity() == null || event.getEntity() == null || !config.getBoolean("creature-product-limiter.cow.enable") || !event.getEntity().getType().equals(EntityType.COW)) return;

		Entity cow = event.getEntity();
		int milked = cow.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "milked"), PersistentDataType.INTEGER, 0);

		cow.getPersistentDataContainer().set(new NamespacedKey(plugin, "lastFeed"), PersistentDataType.LONG, new Date().getTime());

		if (milked == 1) {
			cow.getPersistentDataContainer().set(new NamespacedKey(plugin, "milked"), PersistentDataType.INTEGER, 0);
			event.getHumanEntity().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("creature-product-limiter.cow.feed-msg").replaceAll("&", "§"));
		}

	}

}
