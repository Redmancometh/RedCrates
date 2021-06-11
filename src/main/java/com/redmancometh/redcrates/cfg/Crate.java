package com.redmancometh.redcrates.cfg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;

import com.google.gson.annotations.Expose;
import com.redmancometh.warcore.util.Pair;
import com.redmancometh.warcore.util.WeightedChooser;

import lombok.Data;

@Data
public class Crate {
	private String name;
	private ItemData crateItem;
	private Location location;
	private Material crateBlock;
	private List<CrateReward> rewards, bonusRewards;
	private double bonusFailChance;
	private WeightedChooser<CrateReward> rewardBucket = new WeightedChooser();
	private WeightedChooser<CrateReward> bonusBucket = new WeightedChooser();
	@Expose(serialize = false, deserialize = false)
	private CrateReward[] shuffledRewards;
	@Expose(serialize = false, deserialize = false)
	private CrateReward[] shuffledBonusReel;

	public void init() {
		initRegularReel();
		initBonusReel();
	}

	public void initBonusReel() {
		int upper = (int) bonusBucket.getUpperBound();
		shuffledBonusReel = new CrateReward[upper];
		for (Entry<Pair<Double, Double>, CrateReward> entry : getBonusBucket().entrySet()) {
			Pair<Double, Double> range = entry.getKey();
			CrateReward reward = entry.getValue();
			for (int y = range.getKey().intValue(); y < range.getValue(); y++)
				shuffledBonusReel[y] = reward;
		}
		List<CrateReward> rewards = Arrays.asList(shuffledBonusReel);
		Collections.shuffle(rewards);
		shuffledBonusReel = rewards.toArray(new CrateReward[rewards.size()]);
	}

	public void initRegularReel() {
		int upper = (int) rewardBucket.getUpperBound();
		shuffledRewards = new CrateReward[upper];
		// We can't iterate over .values because we have duplicates, and count on those
		// duplicates for our distribution.
		for (Entry<Pair<Double, Double>, CrateReward> entry : getRewardBucket().entrySet()) {
			Pair<Double, Double> range = entry.getKey();
			CrateReward reward = entry.getValue();
			for (int y = range.getKey().intValue(); y < range.getValue(); y++) {
				shuffledRewards[y] = reward;
			}
		}
		List<CrateReward> rewards = Arrays.asList(shuffledRewards);
		Collections.shuffle(rewards);
		shuffledRewards = rewards.toArray(new CrateReward[rewards.size()]);
	}

	public CrateReward getBonusReward() {
		return rewardBucket.getRandomElement();
	}

	public CrateReward getReward() {
		return rewardBucket.getRandomElement();
	}
}
