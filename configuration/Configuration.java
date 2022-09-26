package configuration;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import core.AntiFarmPlugin;

public class Configuration extends YamlConfiguration {

	private final AntiFarmPlugin plugin;
	private File file;

	public Configuration(String path, AntiFarmPlugin plugin) {

        this.plugin = plugin;
        this.load(path);

    }

    public void saveDefaults(String path) {
        this.plugin.saveResource(path + ".yml", false);
    }

    @Override
	public void load(String path) {

    	this.file = new File(plugin.getDataFolder(), path + ".yml");

    	if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.saveDefaults(path);
        }

        try {
        	ConfigUpdater.update(plugin, path + ".yml", file);
            super.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void save() {

        try {
            super.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reload(String path) {
    	this.load(path);
    }

}