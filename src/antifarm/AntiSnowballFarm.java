package antifarm;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiSnowballFarm implements Listener {

	private final Configuration config;

	public AntiSnowballFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityBlockForm(EntityBlockFormEvent event) {

		if (event.isCancelled() || event.getEntity() == null) return;
		if (!event.getEntity().getType().equals(EntityType.SNOWMAN)) return;
		if (!event.getNewState().getType().equals(Material.SNOW)) return;
		if (!config.getBoolean("farms-settings.prevent-snowball-farms", true)) return;

		event.setCancelled(true);

	}

}
