package com.redmancometh.redcrates.menus;

import java.lang.ref.WeakReference;
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

import lombok.Getter;

public class RewardMenu extends Menu {
	private Crate crate;
	private int[][] regularRows = new int[][] { { 10, 19, 28 }, { 12, 21, 30 }, { 14, 23, 32 }, { 16, 25, 34 } };
	private WeakReference<CrateReward[]> spinItems;
	private WeakReference<CrateReward[]> bonusItems;
	private int[] rewards = new int[4];
	private AtomicInteger reelX, reelY, reelZ, reelB;
	private Random rand = new Random();
	private BukkitTask spinner;
	private boolean finishedSpin = false;

	public RewardMenu(Crate crate) {
		super(RedCrates.getInstance().cfg().getRewardMenuName(), RedCrates.getInstance().cfg().getSlotTemplate(), 45);
		this.crate = crate;
		initializeSpinItems();
		constructMenu();
	}

	@Override
	public void close(Player player) {
		super.close(player);
		if (!finishedSpin) {
			for (int x = 0; x < 3; x++) {
				giveReward(player, false);
			}
			giveReward(player, true);
			return;
		}
		giveSlotRewards(player);
	}

	@Override
	public void open(Player player) {
		super.open(player);
		setReels();
	}

	public void initializeSpinItems() {
		this.spinItems = new WeakReference(crate.getShuffledRewards());
		this.bonusItems = new WeakReference(crate.getShuffledBonusReel());
		RewardMenu.this.reelX = new AtomicInteger(rand.nextInt(spinItems.get().length));
		RewardMenu.this.reelY = new AtomicInteger(rand.nextInt(spinItems.get().length));
		RewardMenu.this.reelZ = new AtomicInteger(rand.nextInt(spinItems.get().length));
		RewardMenu.this.reelB = new AtomicInteger(rand.nextInt(bonusItems.get().length));
	}

	public void constructMenu() {
		MenuButton button = new MenuButton();
		button.setConstructor((player) -> new ItemStack(Material.LEVER));
		button.setAction((click, player) -> {
			if (spinner != null)
				return;
			spinner = new SpinRunnable(player).runTaskTimer(RedCrates.getInstance(), 0,
					RedCrates.getInstance().cfg().getUpdatePeriod());
		});
		setButton(40, button);
	}

	// TODO: All these reward methods probably shouldn't be in the code for a
	// menu...

	/**
	 * 
	 * @param p
	 * @param bonus
	 */
	public void giveReward(Player p, boolean bonus) {
		CratesConfig cfg = RedCrates.getInstance().cfg();
		CrateReward reward = bonus ? crate.getBonusReward() : crate.getReward();
		List<String> commands = reward.getCommands();
		commands.forEach(
				(command) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%p%", p.getName())));
		if (!bonus)
			cfg.getMessages().getGotRewardMessage().forEach(
					(line) -> p.sendMessage(line.replace("%p%", p.getName()).replace("%n%", reward.getName())));
		else
			cfg.getMessages().getGotBonusMessage().forEach(
					(line) -> p.sendMessage(line.replace("%p%", p.getName()).replace("%n%", reward.getName())));
	}

	public void giveReward(Player p, CrateReward reward, boolean bonus) {
		CratesConfig cfg = RedCrates.getInstance().cfg();
		List<String> commands = reward.getCommands();
		commands.forEach(
				(command) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%p%", p.getName())));
		System.out.println(cfg.getMessages().getGotRewardMessage().get(0));
		if (!bonus)
			cfg.getMessages().getGotRewardMessage().forEach(
					(line) -> p.sendMessage(line.replace("%p%", p.getName()).replace("%n%", reward.getName())));
		else
			cfg.getMessages().getGotBonusMessage().forEach(
					(line) -> p.sendMessage(line.replace("%p%", p.getName()).replace("%n%", reward.getName())));
	}

	public void giveSlotRewards(Player p) {
		CrateReward rewardOne = spinItems.get()[rewards[0]];
		CrateReward rewardTwo = spinItems.get()[rewards[1]];
		CrateReward rewardThree = spinItems.get()[rewards[2]];
		CrateReward rewardFour = bonusItems.get()[rewards[3]];
		giveReward(p, rewardOne, false);
		giveReward(p, rewardTwo, false);
		giveReward(p, rewardThree, false);
		giveReward(p, rewardFour, true);
	}

	public void setReels() {
		for (int x = 0; x < 3; x++) {
			if (reelX.incrementAndGet() >= spinItems.get().length - 1)
				reelX.set(0);
			inv.setItem(regularRows[0][x], spinItems.get()[reelX.get()].getItem());
			if (reelY.incrementAndGet() >= spinItems.get().length - 1)
				reelY.set(0);
			inv.setItem(regularRows[1][x], spinItems.get()[reelY.get()].getItem());
			if (reelZ.incrementAndGet() >= spinItems.get().length - 1)
				reelZ.set(0);
			inv.setItem(regularRows[2][x], bonusItems.get()[reelZ.get()].getItem());
			if (reelB.incrementAndGet() >= bonusItems.get().length - 1)
				reelB.set(0);
			inv.setItem(regularRows[3][x], bonusItems.get()[reelB.get()].getItem());
		}
		inv.getViewers().forEach((viewer) -> ((Player) viewer).updateInventory());
	}

	public class SpinRunnable extends BukkitRunnable {
		private int elapsed = 0;
		@Getter
		private Player p;

		public SpinRunnable(Player p) {
			this.p = p;
		}

		@Override
		public void run() {
			elapsed++;
			CratesConfig cfg = RedCrates.getInstance().cfg();
			if (reelX.incrementAndGet() >= spinItems.get().length - 3)
				reelX.set(0);
			if (reelY.incrementAndGet() >= spinItems.get().length - 3)
				reelY.set(0);
			if (reelZ.incrementAndGet() >= spinItems.get().length - 3)
				reelZ.set(0);
			if (reelB.incrementAndGet() >= bonusItems.get().length - 3)
				reelB.set(0);
			for (int x = 0; x < 3; x++) {
				inv.setItem(regularRows[0][x], spinItems.get()[reelX.get() + x].getItem());
				if (!(elapsed > cfg.getReelTwoInterval()))
					continue;
				inv.setItem(regularRows[1][x], spinItems.get()[reelY.get() + x].getItem());
				if (!(elapsed > cfg.getReelThreeInterval()))
					continue;
				inv.setItem(regularRows[2][x], spinItems.get()[reelZ.get() + x].getItem());
				if (!(elapsed > cfg.getReelBonusInterval()))
					continue;
				inv.setItem(regularRows[3][x], bonusItems.get()[reelB.get() + x].getItem());
			}
			rewards[0] = reelX.get() + 1;
			rewards[1] = reelY.get() + 1;
			rewards[2] = reelZ.get() + 1;
			rewards[3] = reelB.get() + 1;
			inv.getViewers().forEach((viewer) -> ((Player) viewer).updateInventory());
			if (elapsed > cfg.getTotalIntervals()) {
				this.cancel();
				finishedSpin = true;
			}
		}
	}

}
