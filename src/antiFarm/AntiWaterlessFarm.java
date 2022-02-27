package antiFarm;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiWaterlessFarm implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND))
			return;
		if (!J.configJ.config.getBoolean("disable-farms.waterless-farm"))
			return;
		BlockData blockData = event.getBlock().getRelative(BlockFace.DOWN).getBlockData();
		Farmland farmland = (Farmland) blockData;
		if (farmland.getMoisture() == 0) {
			for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
				if (event.getBlock().getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
					event.setCancelled(true);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!(event.getItem() != null))
			return;
		if (!event.getItem().getType().equals(Material.BONE_MEAL))
			return;
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		if (!event.getClickedBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND))
			return;
		if (!J.configJ.config.getBoolean("disable-farms.waterless-farm"))
			return;
		BlockData blockData = event.getClickedBlock().getRelative(BlockFace.DOWN).getBlockData();
		Farmland farmland = (Farmland) blockData;
		if (farmland.getMoisture() == 0) {
			for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
				if (event.getClickedBlock().getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
					event.setCancelled(true);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onBlockGrow(BlockGrowEvent event) {
		if (event.isCancelled())
			return;
		if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND))
			return;
		if (!J.configJ.config.getBoolean("disable-farms.waterless-farm"))
			return;
		BlockData blockData = event.getBlock().getRelative(BlockFace.DOWN).getBlockData();
		Farmland farmland = (Farmland) blockData;
		if (farmland.getMoisture() == 0) {
			for (String checkBlock : J.configJ.config.getStringList("farm-blocks")) {
				if (event.getBlock().getType().toString().toUpperCase().equals(checkBlock.toUpperCase())) {
					event.setCancelled(true);
					event.getBlock().breakNaturally();
					event.getBlock().setType(Material.AIR);
					break;
				}
			}
		}
	}

}