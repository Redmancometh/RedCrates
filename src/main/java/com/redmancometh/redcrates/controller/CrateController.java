package com.redmancometh.redcrates.controller;

import com.redmancometh.redcrates.cfg.CrateConfigManager;

import lombok.Getter;

@Getter
public class CrateController {
	private CrateConfigManager cfgMan = new CrateConfigManager();

	public void init() {
		cfgMan.init();
		System.out.println("INITIALIZING CONTROLLER");
		cfgMan.getConfig().getCrates().forEach((crate) -> {
			System.out.println("SETTING CRATE BLOCK");
			crate.getLocation().getBlock().setType(crate.getCrateBlock());
			for (int x = 0; x < 5; x++) {
				System.out.println(crate.getLocation().getBlock());
			}
		});
	}
}
