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
	private List<CrateReward> rewards;
	private WeightedChooser<CrateReward> rewardBucket = new WeightedChooser();
	@Expose(serialize = false, deserialize = false)
	private CrateReward[] shuffledRewards;

	public void init() {
		int upper = (int) getRewardBucket().getUpperBound();
		shuffledRewards = new CrateReward[upper];
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

	public CrateReward getReward() {
		return rewardBucket.getRandomElement();
	}
}
