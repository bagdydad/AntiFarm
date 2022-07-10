package antifarm;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent.ChangeReason;

import configuration.Configuration;
import core.AntiFarmPlugin;

public class AntiVillagerCareer implements Listener {

	private final Configuration config;

	public AntiVillagerCareer(AntiFarmPlugin plugin) {
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVillagerCareerChange(VillagerCareerChangeEvent event) {

		if (event.isCancelled() || event.getEntity() == null || event.getReason() == null) return;
		if (!event.getReason().equals(ChangeReason.EMPLOYED)) return;
		if (!config.getBoolean("villager-settings.prevent-villagers-profession-change", true)) return;

		Villager villager = event.getEntity();
		villager.setVillagerExperience(villager.getVillagerExperience() + 1);

	}

}
