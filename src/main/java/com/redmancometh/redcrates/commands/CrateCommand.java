package com.redmancometh.redcrates.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.redmancometh.redcrates.RedCrates;
import com.redmancometh.redcrates.cfg.Crate;
import com.redmancometh.redcrates.cfg.CratesConfig;

import lombok.Data;

public class CrateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandSender p = sender;
		if (!p.isOp())
			return true;
		String action = args[0];
		String playerName = args[1];
		String keyName = args[2];
		if (label.equalsIgnoreCase("givecratekey")) {
			giveKey(sender, args[0], args[1], args[2]);
			return true;
		}
		String amount = args[3];
		switch (action.toLowerCase()) {
		case "give":
			giveKey(sender, playerName, keyName, amount);
			return true;
		case "givekey":
			giveKey(sender, playerName, keyName, amount);
			return true;
		case "gk":
			giveKey(sender, playerName, keyName, amount);
			return true;
		case "ga":
			giveAllKey(sender, args[1], args[2]);
			return true;
		case "giveall":
			giveAllKey(sender, args[1], args[2]);
			return true;
		}
		return true;
	}

	public void giveAllKey(CommandSender sender, String keyName, String amount) {
		CratesConfig cfg = RedCrates.getInstance().cfg();
		int count = Integer.parseInt(amount);
		for (Crate crate : cfg.getCrates()) {
			if (crate.getName().equalsIgnoreCase(keyName)) {
				Bukkit.getOnlinePlayers().forEach((p) -> {
					for (int x = 0; x < count; x++)
						p.getInventory().addItem(crate.getCrateItem().buildItem());
				});
				return;
			}
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&4!&e]&r&e There's no key by that name."));
	}

	public void giveKey(CommandSender sender, String playerName, String keyName, String amount) {
		int count = Integer.parseInt(amount);
		Player p = Bukkit.getPlayer(playerName);
		if (p == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&4!&e]&r&e Player is not online"));
			return;
		}
		CratesConfig cfg = RedCrates.getInstance().cfg();
		for (Crate crate : cfg.getCrates()) {
			if (crate.getName().equalsIgnoreCase(keyName)) {
				for (int x = 0; x < count; x++)
					p.getInventory().addItem(crate.getCrateItem().buildItem());
				return;
			}
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&4!&e]&r&e There's no key by that name."));
	}

	@Data
	public class PojoTest {
		private String test;
	}
}
