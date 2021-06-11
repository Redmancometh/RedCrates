package com.redmancometh.redcrates.commands;

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
		Player p = (Player) sender;
		if (!p.isOp())
			return true;
		CratesConfig cfg = RedCrates.getInstance().cfg();
		for (Crate crate : cfg.getCrates()) {
			if (crate.getName().equalsIgnoreCase(args[0])) {
				p.getInventory().addItem(crate.getCrateItem().buildItem());
			}
		}
		return true;
	}

	@Data
	public class PojoTest {
		private String test;
	}
}
