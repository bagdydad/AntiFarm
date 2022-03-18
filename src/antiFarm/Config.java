package antiFarm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private File configFile;
	public FileConfiguration config;

	public void createConfig() {
		Bukkit.getLogger().info("[antiFarm] Checking the config file...");
		configFile = new File(Main.plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			Main.plugin.saveResource("config.yml", false);
			Bukkit.getLogger().info("[antiFarm] Config file not found, a new config file was created!");
		}
		config = new YamlConfiguration();
		try {
			File currentConfigFile = new File(Main.plugin.getDataFolder(), "currentConfig.yml");
			copyFile(configFile, currentConfigFile);
			Main.plugin.saveResource("config.yml", true);
			BufferedReader reader1 = new BufferedReader(new FileReader(currentConfigFile));
			BufferedReader reader2 = new BufferedReader(new FileReader(configFile));
			String cc = reader1.readLine();
			String nc = reader2.readLine();
			int lineNumber = 1;
			boolean match = true;
			while (cc != null || nc != null) {
				if(cc != null && nc != null && !cc.equalsIgnoreCase(nc)) {
					String[] split = cc.split(":");
					String[] ncsplit = nc.split(":");
					if (split[0].trim().equals(ncsplit[0].trim())) {
						if (split.length > 1) {
							if (!split[1].trim().equals("true") && !split[1].trim().equals("false")) {
								if (!String.valueOf(split[1].trim().charAt(0)).equals("\"")) {
									setVariable(currentConfigFile.toPath(), lineNumber, nc);
									match = false;
								} else if (!String.valueOf(split[1].charAt(split[1].length() - 1)).equals("\"")) {
									setVariable(currentConfigFile.toPath(), lineNumber, nc);
									match = false;
								} else if (split[1].trim().substring(1, split[1].length() - 1).contains("\"")) {
									setVariable(currentConfigFile.toPath(), lineNumber, nc);
									match = false;
								}
							}
						}
					} else {
						setVariable(currentConfigFile.toPath(), lineNumber, nc);
						match = false;
					}
				}
				cc = reader1.readLine();
				nc = reader2.readLine();
				lineNumber++;
			}
			reader1.close();
			reader2.close();
			File checkConfig = new File(Main.plugin.getDataFolder(), "config.yml");
			checkConfig.delete();
			copyFile(currentConfigFile, configFile);
			currentConfigFile.delete();
			config.load(configFile);
			if (!match) {
				Bukkit.getLogger().warning("[antiFarm] Corrections have been made to your config file, please review your settings.");
			}
			Bukkit.getLogger().info("[antiFarm] Config file loaded!");
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().warning("[antiFarm] An error was encountered trying to load the config file!");
			e.printStackTrace();
		}
	}
	
	public static void setVariable(Path path, int lineNumber, String data) throws IOException {
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		lines.set(lineNumber - 1, data);
		Files.write(path, lines, StandardCharsets.UTF_8);
		Bukkit.getLogger().warning("[antiFarm] Config file problem detected and fixed! Row: " + lineNumber + " (config.yml)");
	}
	
	public static void copyFile(File from, File to) throws IOException {
		com.google.common.io.Files.copy(from, to);
	}
}