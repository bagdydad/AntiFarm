package antiFarm;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

public class AntiSnowFarm implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityBlockForm(EntityBlockFormEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getEntity().getType().equals(EntityType.SNOWMAN)) {
			if (event.getNewState().getType().equals(Material.SNOW)) {
				if (J.configJ.config.getBoolean("prevent-farms.snow-farms", true)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}
