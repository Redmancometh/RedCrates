package com.redmancometh.redcrates;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.redmancometh.redcrates.cfg.CratesConfig;
import com.redmancometh.redcrates.commands.CrateCommand;
import com.redmancometh.redcrates.controller.CrateController;
import com.redmancometh.redcrates.listeners.CrateListener;

public class RedCrates extends JavaPlugin {
	private CrateController crateController;

	@Override
	public void onEnable() {
		super.onEnable();
		this.crateController = new CrateController();
		this.crateController.init();
		Bukkit.getPluginManager().registerEvents(new CrateListener(), this);
		getCommand("crates").setExecutor(new CrateCommand());
	}

	public CratesConfig cfg() {
		return getCrateController().getCfgMan().getConfig();
	}

	public CrateController getCrateController() {
		return crateController;
	}

	public static RedCrates getInstance() {
		return JavaPlugin.getPlugin(RedCrates.class);
	}
}
