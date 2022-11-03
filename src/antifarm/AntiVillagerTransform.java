package antifarm;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiVillagerTransform implements Listener {

	private final Configuration config;
	Random random = new Random();

	public AntiVillagerTransform(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;

		if (event.isCancelled() || event.getPlayer() == null || event.getRightClicked() == null || event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInOffHand() == null) return;
		if (!event.getRightClicked().getType().equals(EntityType.ZOMBIE_VILLAGER)) return;
		if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_APPLE) && !event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.GOLDEN_APPLE) && !event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ENCHANTED_GOLDEN_APPLE) && !event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) return;
		if (!config.getBoolean("villager-settings.prevent-zombie-villagers-cure.enable", true)) return;
		if (config.getInt("villager-settings.prevent-zombie-villagers-cure.cure-percent", 0) != 0) return;

		event.setCancelled(true);

	}

	@EventHandler
	private void onEntityTransform(EntityTransformEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getEntity().getWorld().getName())) return;

		if (event.getEntity() == null || event.getTransformReason() == null || !event.getEntityType().equals(EntityType.VILLAGER) && !event.getEntityType().equals(EntityType.ZOMBIE_VILLAGER)) return;
		if (event.getEntity().getType().equals(EntityType.VILLAGER) && event.getTransformReason().equals(TransformReason.INFECTION) && random.nextInt(100) < config.getInt("villager-settings.prevent-villagers-infection.infection-percent", 30)) return;
		if (event.getEntity().getType().equals(EntityType.ZOMBIE_VILLAGER) && event.getTransformReason().equals(TransformReason.CURED) && random.nextInt(100) < config.getInt("villager-settings.prevent-zombie-villagers-cure.cure-percent", 30)) return;

		LivingEntity transformedEntity = (LivingEntity) event.getTransformedEntity();
		transformedEntity.setHealth(0);

	}

}