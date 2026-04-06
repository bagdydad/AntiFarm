package antifarm;

import configuration.Configuration;
import core.AntiFarmPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AntiCactusFarm implements Listener {

    private static final BlockFace[] ADJACENT_FACES = {
            BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SELF
    };
    private static final BlockFace[] HORIZONTAL_FACES = {
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST
    };
    private final AntiFarmPlugin plugin;
    private final Set<Material> fallingBlocks;
    private Set<String> disabledWorlds;
    private boolean preventCactusFarms;
    private boolean preventPistonFarms;
    private boolean breakBlocks;
    private boolean breakPistons;

    public AntiCactusFarm(AntiFarmPlugin plugin) {
        this.plugin = plugin;
        this.fallingBlocks = EnumSet.of(Material.SAND, Material.RED_SAND, Material.DRAGON_EGG);
        for (Material mat : Material.values()) {
            if (mat.name().endsWith("_CONCRETE_POWDER")) {
                fallingBlocks.add(mat);
            }
        }
        reloadConf();
    }

    public void reloadConf() {
        Configuration config = plugin.getConfig();
        this.disabledWorlds = new HashSet<>(config.getStringList("settings.disabled-worlds"));
        this.preventCactusFarms = config.getBoolean("farms-settings.prevent-cactus-farms", true);
        this.preventPistonFarms = config.getBoolean("farms-settings.prevent-piston-farms", true);
        this.breakBlocks = config.getBoolean("settings.break-blocks", true);
        this.breakPistons = config.getBoolean("settings.break-pistons", true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        if (!preventCactusFarms) return;
        Block block = event.getBlock();
        if (block.getRelative(BlockFace.DOWN).getType() != Material.CACTUS) return;
        if (disabledWorlds.contains(block.getWorld().getName())) return;

        for (BlockFace face : ADJACENT_FACES) {
            Material type = block.getRelative(face).getType();
            if (type != Material.CACTUS && type != Material.AIR && type != Material.CAVE_AIR && type != Material.VOID_AIR) {
                event.setCancelled(true);
                if (breakBlocks) {
                    scheduleCactusBreak(block);
                }
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (!preventCactusFarms) return;
        Block block = event.getBlock();
        if (block.getType() != Material.CACTUS) return;
        Block source = event.getSourceBlock();
        if (!fallingBlocks.contains(source.getType())) return;
        if (disabledWorlds.contains(block.getWorld().getName())) return;
        event.setCancelled(true);
        source.breakNaturally();
        source.setType(Material.AIR, false);
        if (breakBlocks) {
            scheduleCactusBreak(block);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        handlePistonEvent(event, event.getBlock(), event.getDirection(), event.getBlocks());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        handlePistonEvent(event, event.getBlock(), event.getDirection(), event.getBlocks());
    }

    private void handlePistonEvent(Cancellable event, Block piston, BlockFace direction, List<Block> blocks) {
        if (!preventCactusFarms || preventPistonFarms) return;
        if (disabledWorlds.contains(piston.getWorld().getName())) return;
        if (blocks.isEmpty()) {
            if (!checkPistonBlocks(piston, direction)) return;
        } else {
            boolean found = false;
            for (Block b : blocks) {
                if (checkPistonBlocks(b, direction)) {
                    found = true;
                    break;
                }
            }
            if (!found) return;
        }
        event.setCancelled(true);

        if (breakPistons) {
            piston.breakNaturally();
            piston.setType(Material.AIR, false);
        }
    }

    private boolean checkPistonBlocks(Block block, BlockFace direction) {
        Block checkBlock = block.getRelative(direction);
        for (BlockFace face : HORIZONTAL_FACES) {
            if (checkBlock.getRelative(face).getType() == Material.CACTUS) {
                return true;
            }
        }
        return false;
    }

    private void scheduleCactusBreak(Block block) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (int i = 0; i < 4; i++) {
                Block replace = block.getRelative(0, -i, 0);
                Material type = replace.getType();
                if (type == Material.CACTUS || type == Material.SAND) {
                    replace.breakNaturally();
                }
            }
        }, 1L);
    }
}