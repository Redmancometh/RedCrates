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
	private List<String> commands;
	private int chance;
	private Material material;
	@Expose(serialize = false, deserialize = false)
	private ItemStack item;

	public void init() {
		ItemStack item = new ItemStack(material);
		ItemUtil.setName(item, name);
		this.item = item;
	}
}
