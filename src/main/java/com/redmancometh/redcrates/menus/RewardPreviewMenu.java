package com.redmancometh.redcrates.menus;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.redmancometh.redcrates.RedCrates;
import com.redmancometh.redcrates.cfg.Crate;
import com.redmancometh.redcrates.cfg.CrateReward;
import com.redmancometh.redmenus.absraction.Menu;
import com.redmancometh.redmenus.menus.MenuButton;

public class RewardPreviewMenu extends Menu {
	private Crate crate;

	public RewardPreviewMenu(Crate crate) {
		super(RedCrates.getInstance().cfg().getPreviewMenuName(), 54);
		this.crate = crate;
		List<CrateReward> rewards = this.crate.getRewards();
		int x = 0;
		for (; x < rewards.size(); x++) {
			CrateReward reward = rewards.get(x);
			ItemStack item = reward.getItem();
			MenuButton button = new MenuButton();
			button.setConstructor((player) -> item);
			setButton(x, button);
		}
		List<CrateReward> bonusRewards = this.crate.getBonusRewards();
		for (int y = 0; y < bonusRewards.size(); y++) {
			CrateReward reward = bonusRewards.get(y);
			ItemStack item = reward.getItem();
			MenuButton button = new MenuButton();
			button.setConstructor((player) -> item);
			setButton(x + y, button);
		}
	}
}
