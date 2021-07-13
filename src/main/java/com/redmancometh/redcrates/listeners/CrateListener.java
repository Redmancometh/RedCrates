package com.redmancometh.redcrates.listeners;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.redmancometh.redcrates.RedCrates;
import com.redmancometh.redcrates.cfg.Crate;
import com.redmancometh.redcrates.cfg.CratesConfig;
import com.redmancometh.redcrates.menus.RewardMenu;
import com.redmancometh.redcrates.menus.RewardPreviewMenu;

public class CrateListener implements Listener {

	private Cache<UUID, Boolean> rightCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build();

	@EventHandler
	public void openCrate(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Block b = e.getClickedBlock();
		if (b == null)
			return;
		Player p = e.getPlayer();
		CratesConfig cfg = RedCrates.getInstance().cfg();
		for (Crate crate : cfg.getCrates()) {
			if (crate.getCrateBlock() != b.getType())
				continue;
			Location crateLoc = crate.getLocation();
			if (crateLoc.getX() == b.getX() && crateLoc.getY() == b.getY() && crateLoc.getZ() == b.getZ()) {
				e.setCancelled(true);
				if (rightCache.asMap().containsKey(p.getUniqueId()))
					return;
				rightCache.put(p.getUniqueId(), true);
				if (item != null && crate.getCrateItem().isItem(item)) {
					if (item.getAmount() > 1)
						item.setAmount(item.getAmount() - 1);
					else
						p.setItemInHand(new ItemStack(Material.AIR));
					RewardMenu menu = new RewardMenu(crate);
					menu.open(p);
					return;
				}
				RewardPreviewMenu previewMenu = new RewardPreviewMenu(crate);
				previewMenu.open(p);
			}
		}
	}
}
