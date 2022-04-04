package antiFarm;

import org.bukkit.entity.ArmorStand;
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
				if (J.configJ.config.getBoolean("prevent-mob-farms.enable", true)) {
					if (!J.configJ.config.getStringList("prevent-mob-farms.whitelist").contains(event.getEntity().getType().toString().toUpperCase())) {
						event.getDrops().clear();
					}
				}
			}
		}
	}
	
}
