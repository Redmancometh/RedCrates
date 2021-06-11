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
		getConfig().getNoReward().init();
		System.out.println(getConfig().getNoReward());
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
			crate.getBonusRewards().forEach((reward) -> {
				try {
					crate.getBonusBucket().insertElement(reward, reward.getChance());
					reward.init();
				} catch (Exception e) {
					System.out.println("Error with reward " + reward + " in crate: " + crate);
					e.printStackTrace();
				}
			});
			Double upper = crate.getRewardBucket().getUpperBound();
			Double blanks = (upper * (crate.getBonusFailChance() / 100));
			int bonusUpper = upper.intValue() + blanks.intValue();
			System.out.println("TOTAL BLANKS: " + blanks);
			System.out.println("RANGE: " + (bonusUpper - upper));
			crate.getBonusBucket().insertElement(getConfig().getNoReward(), bonusUpper - upper);
			crate.init();
		});
	}
}
