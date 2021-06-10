package com.redmancometh.redcrates.controller;

import com.redmancometh.redcrates.cfg.CrateConfigManager;

import lombok.Getter;

@Getter
public class CrateController {
	private CrateConfigManager cfgMan = new CrateConfigManager();

	public void init() {
		cfgMan.init();
		cfgMan.getConfig().getCrates().forEach((crate) -> {
			crate.getLocation().getBlock().setType(crate.getCrateBlock());
		});
	}
}
