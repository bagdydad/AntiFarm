package antiFarm;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class AntiMobFarm implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity().getKiller() == null) {
			if (!(event.getEntity() instanceof Player) && !(event.getEntity() instanceof ArmorStand)) {
				if (J.configJ.config.getBoolean("disable-mob-farms.all-mob-farms")) {
					event.getDrops().clear();
				} else if (event.getEntity().getType().equals(EntityType.IRON_GOLEM)) {
					if (J.configJ.config.getBoolean("disable-mob-farms.iron-farm")) {
						event.getDrops().clear();
					}
				} else if (event.getEntity().getType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
					if (J.configJ.config.getBoolean("disable-mob-farms.gold-farm")) {
						event.getDrops().clear();
					}
				}
			}
		}
	}

}
