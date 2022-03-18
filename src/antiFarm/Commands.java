package antiFarm;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (cmd.getName().equalsIgnoreCase("antifarm") && sender.isOp() || sender.hasPermission("antifarm.admin")) {
			if (args.length >= 1) {
				if (args[0].equals("reload")) {
					J.configJ.createConfig();
					sender.sendMessage(ChatColor.GREEN + "[antiFarm] Configuration file reloaded!");
				}
			} else {
				sender.sendMessage(ChatColor.GREEN + "[antiFarm] Anti AFK farm blocker plugin.");
				sender.sendMessage(ChatColor.GREEN + "[antiFarm] Author: oTuin");
				sender.sendMessage(ChatColor.GREEN + "[antiFarm] Version: " + Main.plugin.getDescription().getVersion().toString());
				sender.sendMessage(ChatColor.GREEN + "[antiFarm] Thanks for using my plugin :)");
			}
		}
		return false;
	}
}
