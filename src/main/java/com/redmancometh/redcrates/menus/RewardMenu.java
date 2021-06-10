package com.redmancometh.redcrates.menus;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.redmancometh.redcrates.RedCrates;
import com.redmancometh.redcrates.cfg.Crate;
import com.redmancometh.redcrates.cfg.CrateReward;
import com.redmancometh.redcrates.cfg.CratesConfig;
import com.redmancometh.redmenus.absraction.Menu;
import com.redmancometh.redmenus.menus.MenuButton;
import com.redmancometh.warcore.util.ItemUtil;

public class RewardMenu extends Menu {
	private Crate crate;
	private int[][] regularRows = new int[][] { { 10, 19, 28 }, { 12, 21, 30 }, { 14, 23, 32 }, { 16, 25, 34 } };
	private CrateReward[] spinItems;
	private AtomicInteger reelX, reelY, reelZ, reelB;
	private Random rand = new Random();
	private BukkitTask spinner;

	public RewardMenu(Crate crate) {
		super(RedCrates.getInstance().cfg().getRewardMenuName(), RedCrates.getInstance().cfg().getSlotTemplate(), 45);
		this.crate = crate;
		initializeSpinItems();
		constructMenu();
	}

	@Override
	public void open(Player player) {
		super.open(player);
		setReels();
	}

	public void initializeSpinItems() {
		this.spinItems = crate.getShuffledRewards();
		RewardMenu.this.reelX = new AtomicInteger(rand.nextInt(spinItems.length));
		RewardMenu.this.reelY = new AtomicInteger(rand.nextInt(spinItems.length));
		RewardMenu.this.reelZ = new AtomicInteger(rand.nextInt(spinItems.length));
		RewardMenu.this.reelB = new AtomicInteger(rand.nextInt(spinItems.length));
	}

	public void constructMenu() {
		MenuButton button = new MenuButton();
		button.setConstructor((player) -> new ItemStack(Material.LEVER));
		button.setAction((click, player) -> {
			if (spinner != null)
				return;
			spinner = new SpinRunnable().runTaskTimer(RedCrates.getInstance(), 0,
					RedCrates.getInstance().cfg().getUpdatePeriod());
		});
		setButton(40, button);
	}

	public void giveReward(Player p) {
		CratesConfig cfg = RedCrates.getInstance().cfg();
		CrateReward reward = crate.getReward();
		List<String> commands = reward.getCommands();
		commands.forEach(
				(command) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%p%", p.getName())));
		cfg.getMessages().getGotRewardMessage()
				.forEach((line) -> line.replace("%p%", p.getName()).replace("%n%", reward.getName()));
	}

	public void setReels() {
		for (int x = 0; x < 3; x++) {
			if (reelX.incrementAndGet() >= spinItems.length - 1)
				reelX.set(0);
			inv.setItem(regularRows[0][x], spinItems[reelX.get()].getItem());
			if (reelY.incrementAndGet() >= spinItems.length - 1)
				reelY.set(0);
			inv.setItem(regularRows[1][x], spinItems[reelY.get()].getItem());
			if (reelZ.incrementAndGet() >= spinItems.length - 1)
				reelZ.set(0);
			inv.setItem(regularRows[2][x], spinItems[reelZ.get()].getItem());
			if (reelB.incrementAndGet() >= spinItems.length - 1)
				reelB.set(0);
			inv.setItem(regularRows[3][x], spinItems[reelB.get()].getItem());
		}
		inv.getViewers().forEach((viewer) -> ((Player) viewer).updateInventory());
	}

	public class SpinRunnable extends BukkitRunnable {
		private int elapsed = 0;

		@Override
		public void run() {
			elapsed++;
			CratesConfig cfg = RedCrates.getInstance().cfg();
			for (int x = 0; x < 3; x++) {
				if (reelX.incrementAndGet() >= spinItems.length - 1)
					reelX.set(0);
				inv.setItem(regularRows[0][x], spinItems[reelX.get()].getItem());
				if (!(elapsed > cfg.getReelTwoInterval()))
					continue;
				if (reelY.incrementAndGet() >= spinItems.length - 1)
					reelY.set(0);
				inv.setItem(regularRows[1][x], spinItems[reelY.get()].getItem());
				if (!(elapsed > cfg.getReelThreeInterval()))
					continue;
				if (reelZ.incrementAndGet() >= spinItems.length - 1)
					reelZ.set(0);
				inv.setItem(regularRows[2][x], spinItems[reelZ.get()].getItem());
				if (!(elapsed > cfg.getReelBonusInterval()))
					continue;
				if (reelB.incrementAndGet() >= spinItems.length - 1)
					reelB.set(0);
				inv.setItem(regularRows[3][x], spinItems[reelB.get()].getItem());
			}
			inv.getViewers().forEach((viewer) -> ((Player) viewer).updateInventory());
			if (elapsed > cfg.getTotalIntervals())
				this.cancel();
		}
	}

}
