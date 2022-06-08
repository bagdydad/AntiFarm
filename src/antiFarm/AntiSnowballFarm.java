package antiFarm;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

import core.J;

public class AntiSnowballFarm implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityBlockForm(EntityBlockFormEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() == null) return;
		if (event.getEntity().getType().equals(EntityType.SNOWMAN)) {
			if (event.getNewState().getType().equals(Material.SNOW)) {
				if (J.configJ.config.getBoolean("prevent-farms.snowball-farms", true)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
}
