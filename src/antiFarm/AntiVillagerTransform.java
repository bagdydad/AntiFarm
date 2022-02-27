package antiFarm;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class AntiVillagerTransform implements Listener {

	@EventHandler
	public void onVillagerTransform(EntityTransformEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (!event.getEntity().getType().equals(EntityType.VILLAGER)) return;
		if (event.getTransformReason().equals(TransformReason.INFECTION)) {
			if (J.configJ.config.getBoolean("settings.block-villager-infection")) {
				event.setCancelled(true);
			}
		}
		if (event.getTransformReason().equals(TransformReason.CURED)) {
			if (J.configJ.config.getBoolean("settings.block-zombie-villager-cure")) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType().equals(EntityType.ZOMBIE_VILLAGER)) {
			if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_APPLE) || event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.GOLDEN_APPLE)) {
				if (J.configJ.config.getBoolean("settings.block-zombie-villager-cure")) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}