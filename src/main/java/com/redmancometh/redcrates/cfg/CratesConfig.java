package com.redmancometh.redcrates.cfg;

import java.util.List;

import com.redmancometh.redmenus.config.MenuTemplate;

import lombok.Data;

@Data
public class CratesConfig {
	private List<Crate> crates;
	private MessageConfig messages;
	private MenuTemplate slotTemplate;
	private String rewardMenuName;
	private int updatePeriod, reelTwoInterval, reelThreeInterval, reelBonusInterval, totalIntervals;
}
