package com.redmancometh.redcrates.cfg;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Data;

@Data
public class ItemData {
	private String name;
	private List<String> lore;
	private Material material;
	private short damageValue = 0;

	public ItemStack buildItem() {
		ItemStack item = new ItemStack(material, 1, damageValue);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public boolean isItem(ItemStack item) {
		if (item == null || item.getType() != material || item.getDurability() != damageValue || !item.hasItemMeta())
			return false;
		ItemMeta im = item.getItemMeta();
		if (!im.hasDisplayName() || !im.hasLore())
			return false;
		if (im.getDisplayName().equals(name) && im.getLore().equals(lore))
			return true;
		return false;
	}
}
