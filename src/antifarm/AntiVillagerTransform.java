package antifarm;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiVillagerTransform implements Listener {

	private final Configuration config;

	public AntiVillagerTransform(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.isCancelled() || event.getEntity() == null || event.getDamager() == null) return;
		if (!event.getDamager().getType().equals(EntityType.ZOMBIE) && !event.getDamager().getType().equals(EntityType.ZOMBIE_VILLAGER)) return;
		if (!event.getEntity().getType().equals(EntityType.VILLAGER)) return;
		if (!config.getBoolean("villager-settings.prevent-villagers-infection", true)) return;

		Double damage = event.getDamage();

		event.setDamage(0);

		LivingEntity villager = (LivingEntity) event.getEntity();

		villager.damage(damage);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;
		if (event.isCancelled() || event.getPlayer() == null || event.getRightClicked() == null || event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInOffHand() == null) return;
		if (!event.getRightClicked().getType().equals(EntityType.ZOMBIE_VILLAGER)) return;
		if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_APPLE) && !event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.GOLDEN_APPLE) && !event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ENCHANTED_GOLDEN_APPLE) && !event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) return;
		if (!config.getBoolean("villager-settings.prevent-zombie-villagers-cure", true)) return;

		event.setCancelled(true);

	}

}