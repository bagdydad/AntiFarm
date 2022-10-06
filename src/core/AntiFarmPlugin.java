package core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import antifarm.AntiBerryFarm;
import antifarm.AntiBoneMeal;
import antifarm.AntiCactusFarm;
import antifarm.AntiEndermanFarm;
import antifarm.AntiFishFarm;
import antifarm.AntiFroglightFarm;
import antifarm.AntiLightlessFarm;
import antifarm.AntiMobFarm;
import antifarm.AntiMobSpawner;
import antifarm.AntiPistonFarm;
import antifarm.AntiRaidFarm;
import antifarm.AntiSheepShearing;
import antifarm.AntiSnowballFarm;
import antifarm.AntiVillageGuard;
import antifarm.AntiVillagerBreed;
import antifarm.AntiVillagerCareer;
import antifarm.AntiVillagerFarm;
import antifarm.AntiVillagerTarget;
import antifarm.AntiVillagerTrade;
import antifarm.AntiVillagerTransform;
import antifarm.AntiWaterFarm;
import antifarm.AntiWaterlessFarm;
import antifarm.AntiZeroTickFarm;
import configuration.Configuration;
import metrics.Metrics;
import update.UpdateChecker;

public class AntiFarmPlugin extends JavaPlugin implements Listener {

	private Configuration config;
	private Configuration spawners;

	@Override
	public void onEnable() {

		config = new Configuration("config", this);
		spawners = new Configuration("spawners", this);

		registerEvents(this, new AntiPistonFarm(this), new AntiVillagerFarm(this), new AntiWaterFarm(this), new AntiCactusFarm(this),
				new AntiEndermanFarm(this), new AntiVillagerBreed(this), new AntiMobFarm(this), new AntiLightlessFarm(this),
				new AntiBoneMeal(this), new AntiFishFarm(this), new AntiWaterlessFarm(this), new AntiMobSpawner(this),
				new AntiVillagerTransform(this), new AntiVillagerTarget(this), new AntiVillageGuard(this), new AntiSnowballFarm(this),
				new AntiRaidFarm(this), new AntiBerryFarm(this), new AntiZeroTickFarm(this), new AntiSheepShearing(this),
				new AntiFroglightFarm(this), new AntiVillagerCareer(this), new AntiVillagerTrade(this));

		getCommand("antifarm").setExecutor(new Commands(this));

		if (config.getBoolean("settings.bstats", true)) {
			@SuppressWarnings("unused")
			Metrics metrics = new Metrics(this, 14827);
		}

		if (config.getBoolean("settings.update-check", true)) {
			updateCheck();
		}

	}

	@Override
	public void onDisable() {
	}

	private static void registerEvents(Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, plugin);
		}
	}

	private void updateCheck() {
		new UpdateChecker(this, 99472).getVersion(version -> {
			getLogger().info("Checking update...");
			if (this.getDescription().getVersion().equals(version)) {
				getLogger().info("There is not a new update available.");
			} else {
				getLogger().info("There is a new update available.");
				getLogger().info("Current version: " + getDescription().getVersion());
				getLogger().info("Latest version: " + version);
				getLogger().info("Spigot: https://www.spigotmc.org/resources/anti-farm.99472/");
			}
		});
	}

	@Override
	public Configuration getConfig() {
		return config;
	}

	public Configuration getSpawners() {
		return spawners;
	}

}