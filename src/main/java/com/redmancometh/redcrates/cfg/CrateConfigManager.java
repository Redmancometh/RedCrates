package com.redmancometh.redcrates.cfg;

import com.redmancometh.configcore.config.ConfigManager;

public class CrateConfigManager extends ConfigManager<CratesConfig> {

	public CrateConfigManager() {
		super("crates.json", CratesConfig.class);
		setOnReload(this::init);
	}

	@Override
	public void init() {
		super.init();
		getConfig().getCrates().forEach((crate) -> {
			crate.getRewards().forEach((reward) -> {
				try {
					crate.getRewardBucket().insertElement(reward, reward.getChance());
					reward.init();
				} catch (Exception e) {
					System.out.println("Error with reward " + reward + " in crate: " + crate);
					e.printStackTrace();
				}
			});
			crate.init();
		});
	}
}
