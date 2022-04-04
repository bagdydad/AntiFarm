package antiFarm;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	
	private File configFile;
	public FileConfiguration config;
	
	public void createConfig() {
		Bukkit.getLogger().info("[AntiFarm] Checking the config file...");
		configFile = new File(Main.plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			Main.plugin.saveResource("config.yml", false);
			Bukkit.getLogger().info("[AntiFarm] Config file not found, a new config file was created!");
		}
		config = new YamlConfiguration();
		boolean match = true;
		try {
			ConfigUpdater.update(Main.plugin, "config.yml", configFile);
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			match = false;
		}
		if (match) {
			Bukkit.getLogger().info("[AntiFarm] Config file loaded!");
		} else {
			Bukkit.getLogger().warning("[AntiFarm] An error was encountered trying to load the config file!");
		}
	}
	
}