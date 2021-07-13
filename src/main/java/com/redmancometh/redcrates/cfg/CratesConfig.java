package com.redmancometh.redcrates.cfg;

import java.util.List;

import com.redmancometh.redmenus.config.MenuTemplate;

import lombok.Data;

@Data
public class CratesConfig {
	private List<Crate> crates;
	private List<String> leverLore;
	private CrateReward noReward;
	private MessageConfig messages;
	private MenuTemplate slotTemplate;
	private String rewardMenuName, leverName, previewMenuName;
	private int updatePeriod, reelTwoInterval, reelThreeInterval, reelBonusInterval, totalIntervals;
}
