package antifarm;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import configuration.Configuration;
import core.AntiFarmPlugin;


public class AntiMobSpawner implements Listener {

	private final AntiFarmPlugin plugin;
	private final Configuration config;
	Random random = new Random();

	public AntiMobSpawner(AntiFarmPlugin plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onSpawnerSpawn(SpawnerSpawnEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getSpawner().getWorld().getName())) return;

		if (event.isCancelled()) return;

		if (config.getBoolean("mob-spawner-settings.prevent-spawn", true)) {
			event.setCancelled(true);
			return;
		}

		if (!config.getBoolean("mob-spawner-settings.spawner-data.enable", false)) return;

		CreatureSpawner spawner = event.getSpawner();
		String spawnerType = spawner.getSpawnedType().toString().toLowerCase();

		if (config.get("mob-spawner-settings.spawner-data.spawners." + spawnerType) == null) return;

		World world = event.getSpawner().getWorld();
		Location location = event.getSpawner().getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		boolean match = true;

		if (spawner.getMaxNearbyEntities() != config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".maxNearbyEntities")) match = false;
		if (spawner.getMaxSpawnDelay() != config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".maxSpawnDelay")) match = false;
		if (spawner.getMinSpawnDelay() != config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".minSpawnDelay")) match = false;
		if (spawner.getRequiredPlayerRange() != config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".requiredPlayerRange")) match = false;
		if (spawner.getSpawnCount() != config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".spawnCount")) match = false;
		if (spawner.getSpawnRange() != config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".spawnRange")) match = false;

		if (match) return;

		int maxNearbyEntities = config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".maxNearbyEntities", spawner.getMaxNearbyEntities());
		int maxSpawnDelay = config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".maxSpawnDelay", spawner.getMaxSpawnDelay());
		int minSpawnDelay = config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".minSpawnDelay", spawner.getMinSpawnDelay());
		int requiredPlayerRange = config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".requiredPlayerRange", spawner.getRequiredPlayerRange());
		int spawnCount = config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".spawnCount", spawner.getSpawnCount());
		int spawnRange = config.getInt("mob-spawner-settings.spawner-data.spawners." + spawnerType + ".spawnRange", spawner.getSpawnRange());
		int delay = random.nextInt(maxSpawnDelay-minSpawnDelay) + minSpawnDelay;

		String nbt = "SpawnData:{entity:{id:" + spawnerType + "}}" + ",MaxNearbyEntities:" + maxNearbyEntities + ",MaxSpawnDelay:" + maxSpawnDelay + ",MinSpawnDelay:" + minSpawnDelay + ",RequiredPlayerRange:" + requiredPlayerRange + ",SpawnCount:" + spawnCount + ",SpawnRange:" + spawnRange + ",Delay:" + delay;

		event.getSpawner().getBlock().setType(Material.AIR);
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "execute in minecraft:" + world.getName() + " run setblock " + x + " " + y + " " + z + " minecraft:spawner{" + nbt + "}");

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getBlock().getWorld().getName())) return;

		if (event.isCancelled() || event.getPlayer() == null || event.getBlock() == null) return;
		if (!event.getBlock().getType().equals(Material.SPAWNER)) return;
		if (!config.getBoolean("mob-spawner-settings.prevent-break", true)) return;

		if (event.getPlayer().hasPermission("antifarm.admin") || event.getPlayer().isOp()) {
			if (!event.getPlayer().isSneaking()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("mob-spawner-settings.admin-warn-message").replaceAll("&", "§"));
			}
		} else {
			event.setCancelled(true);
			event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("mob-spawner-settings.player-warn-message").replaceAll("&", "§"));
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onRightClick(PlayerInteractEvent event) {

		if (config.getStringList("settings.disabled-worlds").contains(event.getPlayer().getWorld().getName())) return;

		if (event.getItem() == null || event.getPlayer() == null || event.getClickedBlock() == null || event.getAction() == null) return;
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if (!event.getClickedBlock().getType().equals(Material.SPAWNER)) return;
		if (!event.getItem().getType().toString().contains("SPAWN_EGG")) return;
		if (!config.getBoolean("mob-spawner-settings.prevent-transformation", true)) return;

		event.setCancelled(true);
		event.getPlayer().sendMessage(config.getString("settings.prefix").replaceAll("&", "§") + config.getString("mob-spawner-settings.transformation-warn-message").replaceAll("&", "§"));

	}

}