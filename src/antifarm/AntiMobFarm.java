package antifarm;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiMobFarm implements Listener {

	private final Configuration config;

	public AntiMobFarm(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityDeath(EntityDeathEvent event) {

		if (event.getEntity() == null || event.getEntity().getKiller() != null) return;
		if (event.getEntity() instanceof Player || event.getEntity() instanceof ArmorStand) return;
		if (!config.getBoolean("prevent-mob-farms.enable", true)) return;
		if (!config.getStringList("prevent-mob-farms.blacklist").contains(event.getEntity().getType().toString().toUpperCase()) && !config.getStringList("prevent-mob-farms.blacklist").contains("All")) return;
		if (config.getStringList("prevent-mob-farms.whitelist").contains(event.getEntity().getType().toString().toUpperCase())) return;

		event.getDrops().clear();
		event.setDroppedExp(0);

	}

}