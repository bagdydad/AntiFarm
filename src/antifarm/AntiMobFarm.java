package antifarm;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
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
import org.bukkit.event.entity.EntityDamageEvent;
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

	@EventHandler(priority = EventPriority.LOWEST)
	private void onEntityDeath(EntityDeathEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.getEntity() instanceof Player || event.getEntity() instanceof ArmorStand || event.getEntity() instanceof Villager || event.getEntity() instanceof ChestedHorse) return;

		if (!config.getBoolean("prevent-mob-farms.enable", true)) return;
		if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.CUSTOM) && config.getBoolean("prevent-mob-farms.allow-custom-death-drops", false)) {
			event.setDroppedExp(0);
			event.getDrops().clear();
			return;
		}
		if (config.getBoolean("prevent-mob-farms.blacklist", true) && !config.getStringList("prevent-mob-farms.moblist").contains(event.getEntity().getType().toString().toUpperCase())) return;
		if (!config.getBoolean("prevent-mob-farms.blacklist", true) && config.getStringList("prevent-mob-farms.moblist").contains(event.getEntity().getType().toString().toUpperCase())) return;

		double damageTaken = event.getEntity().getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "damageTaken"), PersistentDataType.DOUBLE, 0.0);
		double maxHealth = event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double damagePercentage = config.getDouble("prevent-mob-farms.required-damage-percent-for-loot") / 100;
		if ((maxHealth * damagePercentage) <= damageTaken) return;

		if (config.getBoolean("prevent-mob-farms.block-drop-xp", true)) event.setDroppedExp(0);
		if (config.getBoolean("prevent-mob-farms.block-drop-item", true)) event.getDrops().clear();

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDamage(EntityDamageEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getEntity() instanceof Player || event.getEntity() instanceof ArmorStand || event.getEntity() instanceof Villager || event.getEntity() instanceof ChestedHorse) return;

		if (!event.getCause().equals(DamageCause.ENTITY_ATTACK) && !event.getCause().equals(DamageCause.PROJECTILE) && !event.getCause().equals(DamageCause.FIRE_TICK)) return;

		if (!config.getBoolean("prevent-mob-farms.enable", true)) return;
		if (config.getBoolean("prevent-mob-farms.blacklist", true) && !config.getStringList("prevent-mob-farms.moblist").contains(event.getEntity().getType().toString().toUpperCase())) return;
		if (!config.getBoolean("prevent-mob-farms.blacklist", true) && config.getStringList("prevent-mob-farms.moblist").contains(event.getEntity().getType().toString().toUpperCase())) return;

		Player player = null;
		Entity entity = event.getEntity();
		Double eventDamage = event.getFinalDamage();

		if (event.getCause().equals(DamageCause.PROJECTILE)) {
			Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) event).getDamager();
			if (!((Entity) projectile.getShooter() instanceof Player)) return;
			player = (Player) projectile.getShooter();
		} else if (event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)) return;
			player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
		} else if (event.getCause().equals(DamageCause.FIRE_TICK)) {
			Long lastPlayerHit = entity.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "lastPlayerHit"), PersistentDataType.LONG, new Date().getTime() - 10000);
			LocalDateTime lphTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastPlayerHit), TimeZone.getDefault().toZoneId());
			if (Duration.between(lphTime, LocalDateTime.now()).toSeconds() > 5) return;
		}

		if (player != null) {
			if (player.getInventory().getItemInMainHand().getEnchantments().toString().contains("FIRE")) {
				event.getEntity().getPersistentDataContainer().set(new NamespacedKey(plugin, "lastPlayerHit"), PersistentDataType.LONG, new Date().getTime());
			}
		}

		double damageTaken = entity.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "damageTaken"), PersistentDataType.DOUBLE, 0.0);
		damageTaken = damageTaken + eventDamage;
		entity.getPersistentDataContainer().set(new NamespacedKey(plugin, "damageTaken"), PersistentDataType.DOUBLE, damageTaken);

	}

}