package core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import antiFarm.AntiBerryFarm;
import antiFarm.AntiBoneMeal;
import antiFarm.AntiCactusFarm;
import antiFarm.AntiEndermanFarm;
import antiFarm.AntiFishFarm;
import antiFarm.AntiLightlessFarm;
import antiFarm.AntiMobFarm;
import antiFarm.AntiMobSpawner;
import antiFarm.AntiPistonFarm;
import antiFarm.AntiRaidFarm;
import antiFarm.AntiSnowballFarm;
import antiFarm.AntiVillageGuard;
import antiFarm.AntiVillagerBreed;
import antiFarm.AntiVillagerFarm;
import antiFarm.AntiVillagerTarget;
import antiFarm.AntiVillagerTransform;
import antiFarm.AntiWaterFarm;
import antiFarm.AntiWaterlessFarm;
import antiFarm.AntiZeroTickFarm;
import metrics.Metrics;
import updateChecker.UpdateChecker;

public class Main extends JavaPlugin implements Listener {
	
	public static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		J.configJ.loadConfig();
		registerEvents(Main.plugin, new AntiPistonFarm(), new AntiVillagerFarm(), new AntiWaterFarm(), new AntiCactusFarm(), new AntiEndermanFarm(), new AntiVillagerBreed(), new AntiMobFarm(), new AntiLightlessFarm(), new AntiBoneMeal(), new AntiFishFarm(), new AntiWaterlessFarm(), new AntiMobSpawner(), new AntiVillagerTransform(), new AntiVillagerTarget(), new AntiVillageGuard(), new AntiSnowballFarm(), new AntiRaidFarm(), new AntiBerryFarm(), new AntiZeroTickFarm());
		getCommand("antifarm").setExecutor(new Commands());
        if (J.configJ.config.getBoolean("settings.bstats", true)) {
        	@SuppressWarnings("unused")
        	Metrics metrics = new Metrics(this, 14827);
        }
        if (J.configJ.config.getBoolean("settings.update-check", true)) {
        	 updateCheck();
        }
	}
	
	public void onDisable() {
		plugin = null;
	}
	
	public static void registerEvents(Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, plugin);
		}
	}
	
	public void updateCheck() {
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
	
}