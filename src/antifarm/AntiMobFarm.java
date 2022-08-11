package antifarm;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiMobFarm implements Listener {

	private final AntiFarmPlugin plugin;
	private final Configuration config;

	public AntiMobFarm(AntiFarmPlugin plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

		if (event.isCancelled() || event.getEntity() == null || event.getDamager() == null) return;

		Entity entity = event.getEntity();

		if (entity instanceof Player || entity instanceof ArmorStand || entity instanceof Villager || entity instanceof ChestedHorse) return;

		if (event.getCause().equals(DamageCause.PROJECTILE)) {
			Projectile projectile = (Projectile) event.getDamager();
			if (!((Entity) projectile.getShooter() instanceof Player)) return;
		} else if (!(event.getDamager() instanceof Player)) return;

		event.getEntity().getPersistentDataContainer().set(new NamespacedKey(plugin, "lastPlayerHit"), PersistentDataType.LONG, new Date().getTime());

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityDeath(EntityDeathEvent event) {

		if (event.getEntity() == null) return;

		Entity entity = event.getEntity();

		if (entity instanceof Player || entity instanceof ArmorStand || entity instanceof Villager || entity instanceof ChestedHorse) return;

		if (!config.getBoolean("prevent-mob-farms.enable", true)) return;
		if (config.getBoolean("prevent-mob-farms.blacklist", true) && !config.getStringList("prevent-mob-farms.moblist").contains(event.getEntity().getType().toString().toUpperCase())) return;
		if (!config.getBoolean("prevent-mob-farms.blacklist", true) && config.getStringList("prevent-mob-farms.moblist").contains(event.getEntity().getType().toString().toUpperCase())) return;

		Long lastPlayerHit = entity.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "lastPlayerHit"), PersistentDataType.LONG, new Date().getTime() - 10000);

		LocalDateTime lphTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastPlayerHit), TimeZone.getDefault().toZoneId());

		if (Duration.between(lphTime, LocalDateTime.now()).toSeconds() < 10) return;

		event.getDrops().clear();
		event.setDroppedExp(0);

	}

}