package com.redmancometh.redcrates.cfg;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.annotations.Expose;
import com.redmancometh.warcore.util.ItemUtil;

import lombok.Data;

@Data
public class CrateReward {
	private String name;
	private List<String> commands, lore;
	private int chance;
	private short dataValue = 0;
	private Material material;
	@Expose(serialize = false, deserialize = false)
	private ItemStack item;

	public void init() {
		ItemStack item = new ItemStack(material, 1, dataValue);
		ItemUtil.setName(item, name);
		if (lore != null && lore.size() > 0)
			item = ItemUtil.setLore(item, lore);
		this.item = item;
	}
}
