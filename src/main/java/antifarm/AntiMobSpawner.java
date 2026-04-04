package antifarm;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
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
	private final Configuration spawners;
	Random random = new Random();

	public AntiMobSpawner(AntiFarmPlugin plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
		this.spawners = plugin.getSpawners();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onSpawnerSpawn(SpawnerSpawnEvent event) {

		if (event.isCancelled() || event.getSpawner() == null) return;

		if (config.getStringList("settings.disabled-worlds").contains(event.getSpawner().getWorld().getName())) return;

		if (config.getBoolean("mob-spawner-settings.prevent-spawn", true)) {
			event.setCancelled(true);
			return;
		}

		if (!config.getBoolean("mob-spawner-settings.spawner-data", false)) return;

		CreatureSpawner spawner = event.getSpawner();
		String spawnerType = spawner.getSpawnedType().toString().toLowerCase();

		if (spawners.get("spawners." + spawnerType) == null) return;

		String world = event.getSpawner().getWorld().getName();

		if (world.equalsIgnoreCase("world")) world = "overworld";
		if (world.equalsIgnoreCase("world_nether")) world = "the_nether";
		if (world.equalsIgnoreCase("world_the_end")) world = "the_end";

		Location location = event.getSpawner().getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		boolean match = true;

		if (spawner.getMaxNearbyEntities() != spawners.getInt("spawners." + spawnerType + ".maxNearbyEntities")) match = false;
		if (spawner.getMaxSpawnDelay() != spawners.getInt("spawners." + spawnerType + ".maxSpawnDelay")) match = false;
		if (spawner.getMinSpawnDelay() != spawners.getInt("spawners." + spawnerType + ".minSpawnDelay")) match = false;
		if (spawner.getRequiredPlayerRange() != spawners.getInt("spawners." + spawnerType + ".requiredPlayerRange")) match = false;
		if (spawner.getSpawnCount() != spawners.getInt("spawners." + spawnerType + ".spawnCount")) match = false;
		if (spawner.getSpawnRange() != spawners.getInt("spawners." + spawnerType + ".spawnRange")) match = false;

		if (match) return;

		int maxNearbyEntities = spawners.getInt("spawners." + spawnerType + ".maxNearbyEntities", spawner.getMaxNearbyEntities());
		int maxSpawnDelay = spawners.getInt("spawners." + spawnerType + ".maxSpawnDelay", spawner.getMaxSpawnDelay());
		int minSpawnDelay = spawners.getInt("spawners." + spawnerType + ".minSpawnDelay", spawner.getMinSpawnDelay());
		int requiredPlayerRange = spawners.getInt("spawners." + spawnerType + ".requiredPlayerRange", spawner.getRequiredPlayerRange());
		int spawnCount = spawners.getInt("spawners." + spawnerType + ".spawnCount", spawner.getSpawnCount());
		int spawnRange = spawners.getInt("spawners." + spawnerType + ".spawnRange", spawner.getSpawnRange());
		int delay = random.nextInt(maxSpawnDelay-minSpawnDelay) + minSpawnDelay;

		String nbt = "SpawnData:{entity:{id:" + spawnerType + "}}" + ",MaxNearbyEntities:" + maxNearbyEntities + ",MaxSpawnDelay:" + maxSpawnDelay + ",MinSpawnDelay:" + minSpawnDelay + ",RequiredPlayerRange:" + requiredPlayerRange + ",SpawnCount:" + spawnCount + ",SpawnRange:" + spawnRange + ",Delay:" + delay;

		event.getSpawner().getBlock().setType(Material.AIR);
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "execute in minecraft:" + world + " run setblock " + x + " " + y + " " + z + " minecraft:spawner{" + nbt + "}");

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