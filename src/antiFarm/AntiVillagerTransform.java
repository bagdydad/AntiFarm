package antiFarm;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import core.J;
import core.Main;

public class AntiVillagerTransform implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (!event.getDamager().getType().equals(EntityType.ZOMBIE) && !event.getDamager().getType().equals(EntityType.ZOMBIE_VILLAGER)) return;
		if (!event.getEntity().getType().equals(EntityType.VILLAGER)) return;
		if (J.configJ.config.getBoolean("villager-settings.prevent-villagers-infection", true)) {
			Double damage = event.getDamage();
			event.setDamage(0);
			LivingEntity villager = (LivingEntity) event.getEntity();
			villager.damage(damage);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType().equals(EntityType.ZOMBIE_VILLAGER)) {
			if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_APPLE) || event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.GOLDEN_APPLE) || event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ENCHANTED_GOLDEN_APPLE) || event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) {
				if (J.configJ.config.getBoolean("villager-settings.prevent-zombie-villagers-cure", true)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPotionSplash(PotionSplashEvent event) {
		for (PotionEffect effect : event.getPotion().getEffects()) {
			if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
				for (LivingEntity entity : event.getAffectedEntities()) {
					if (entity.getType().equals(EntityType.ZOMBIE_VILLAGER)) {
						if (J.configJ.config.getBoolean("villager-settings.prevent-zombie-villagers-cure", true)) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
							    @Override
							    public void run() {
							    	entity.removePotionEffect(PotionEffectType.WEAKNESS);
							    }
							}, 2);
						}
					}
				}
			}
		}
	}
	
}