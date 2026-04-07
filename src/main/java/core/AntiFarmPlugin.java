package core;

import antifarm.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import configuration.Configuration;

public class AntiFarmPlugin extends JavaPlugin implements Listener {

    private Configuration config;
    private Configuration spawners;
    private AntiFishFarm antiFishFarm;
    private AntiDispenser antiDispenser;
    private AntiCactusFarm antiCactusFarm;
    private AntiBarteringFarm antiBarteringFarm;
    private AntiGrowth antiGrowth;
    private AntiSnowballFarm antiSnowballFarm;
    private AntiArmadilloFarm antiArmadilloFarm;

    @Override
    public void onEnable() {

        config = new Configuration("config", this);
        spawners = new Configuration("spawners", this);
        antiFishFarm = new AntiFishFarm(this);
        antiDispenser = new AntiDispenser(this);
        antiCactusFarm = new AntiCactusFarm(this);
        antiBarteringFarm = new AntiBarteringFarm(this);
        antiGrowth = new AntiGrowth(this);
        antiSnowballFarm = new AntiSnowballFarm(this);
        registerEvents(this, new AntiPistonFarm(this), new AntiVillagerFarm(this), new AntiWaterFarm(this), antiCactusFarm,
                new AntiEndermanFarm(this), new AntiVillagerBreed(this), new AntiMobFarm(this),
                antiDispenser, antiFishFarm, new AntiMobSpawner(this),
                new AntiVillagerTransform(this), new AntiVillagerTarget(this), new AntiVillageGuard(this), antiSnowballFarm,
                new AntiRaidFarm(this), new AntiBerryFarm(this), new AntiZeroTickFarm(this), antiGrowth,
                new AntiFroglightFarm(this), new AntiVillagerCareer(this), new AntiVillagerTrade(this),
                new AntiChickenEggFarm(this), new AntiCowMilk(this), new AntiLavaFarm(this), antiBarteringFarm);
            if (armadillocheck()) {
                antiArmadilloFarm = new AntiArmadilloFarm(this);
                Bukkit.getPluginManager().registerEvents(antiArmadilloFarm, this);
            }

        getCommand("antifarm").setExecutor(new Commands(this));
    }

    @Override
    public void onDisable() {
    }

    private static void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public Configuration getConfig() {
        return config;
    }

    public Configuration getSpawners() {
        return spawners;
    }

    public void reloadListeners() {
        antiFishFarm.reloadConf();
        antiDispenser.reloadConf();
        antiCactusFarm.reloadConf();
        antiBarteringFarm.reloadConf();
        antiGrowth.reloadConf();
        antiSnowballFarm.reloadConf();
        if (antiArmadilloFarm != null) {
            antiArmadilloFarm.reloadConf();
        }
    }

    private boolean armadillocheck() {
        try {
            EntityType.valueOf("ARMADILLO");
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}