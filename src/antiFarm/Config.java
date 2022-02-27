package antiFarm;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private File configFile;
	public FileConfiguration config;

	public void createConfig() {
		Main.plugin.getLogger().info(ChatColor.GREEN + "Checking the config file...");
		configFile = new File(Main.plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			Main.plugin.saveResource("config.yml", false);
			Main.plugin.getLogger().info(ChatColor.GREEN + "Config file not found, a new config file was created!");
		}
		config = new YamlConfiguration();
		try {
			config.load(configFile);
			Main.plugin.getLogger().info(ChatColor.GREEN + "Config file loaded!");
		} catch (IOException | InvalidConfigurationException e) {
			Main.plugin.getLogger().warning(ChatColor.RED + "An error was encountered trying to load the config file!");
			e.printStackTrace();
			//configFile.delete();
			//Main.plugin.saveResource("config.yml", false);
			//Main.plugin.getLogger().warning(ChatColor.RED + "[antiFarm] Config file reset and rebuilt!");
			//createConfig();
		}
	}

}