package com.redmancometh.redcrates.cfg;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.redmancometh.configcore.config.ConfigManager;
import com.redmancometh.redcrates.RedCrates;

public class CrateConfigManager extends ConfigManager<CratesConfig> {
	// This is a pretty stupid place for this. Move it later.
	private BukkitTask particleRunnable;

	public CrateConfigManager() {
		super("crates.json", CratesConfig.class);
		setOnReload(this::init);
	}

	@Override
	public void init() {
		super.init();
		if (this.particleRunnable != null) {
			System.out.println("CANCELLING");
			this.particleRunnable.cancel();
		}
		getConfig().getNoReward().init();
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
			crate.getBonusBucket().insertElement(getConfig().getNoReward(), bonusUpper - upper);
			crate.init();
		});
		getConfig().getCrates().forEach((crate) -> {
			System.out.println("Crate named: " + crate.getName());
			System.out.println("Upper bound: " + crate.getBonusBucket().getUpperBound());
			if (crate.getName().toLowerCase().contains("void")) {
				crate.getBonusBucket().forEach((range, reward) -> {
					System.out.println("Range: " + range + " for " + reward.getName());
				});
			}
		});
		ParticleRunnable particles = new ParticleRunnable();
		this.particleRunnable = particles.runTaskTimer(RedCrates.getInstance(), 0, 5);
	}

	public class ParticleRunnable extends BukkitRunnable {
		@Override
		public void run() {
			getConfig().getCrates().forEach((crate) -> {
				Location crateLoc = crate.getLocation().clone();
				crateLoc.getWorld().playEffect(crateLoc.add(0, 1, 0), crate.getEffect(), 3);
			});
		}
	}
}
