package com.redmancometh.redcrates.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.redmancometh.redcrates.RedCrates;
import com.redmancometh.redcrates.cfg.Crate;
import com.redmancometh.redcrates.cfg.CratesConfig;
import com.redmancometh.redcrates.menus.RewardMenu;

public class CrateListener implements Listener {
	@EventHandler
	public void openCrate(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Block b = e.getClickedBlock();
		if (item == null || b == null)
			return;
		Player p = e.getPlayer();
		CratesConfig cfg = RedCrates.getInstance().cfg();
		for (Crate crate : cfg.getCrates()) {
			if (crate.getCrateBlock() != b.getType() || !crate.getCrateItem().isItem(item))
				continue;
			e.setCancelled(true);
			if (item.getAmount() > 1)
				item.setAmount(item.getAmount() - 1);
			else
				p.setItemInHand(new ItemStack(Material.AIR));
			RewardMenu menu = new RewardMenu(crate);
			menu.open(p);
		}
	}
}
