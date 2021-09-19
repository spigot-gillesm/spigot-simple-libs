package com.github.spigot_gillesm.lib_test.craft.dynamic_craft;

import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.craft.CraftRunnable;
import com.github.spigot_gillesm.lib_test.craft.DynamicCraft;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class AnvilCraft implements DynamicCraft {

	private final int requiredHammering;

	private final int mistakeCost;

	private final int timeLaps;

	private AnvilCraft(final Builder builder) {
		this.requiredHammering = builder.requiredHammering;
		this.mistakeCost = builder.mistakeCost;
		this.timeLaps = builder.timeLaps;
	}

	@Override
	public CraftRunnable start(Player player, DynamicCraftMenu anvilMenu) {
		var runnable = new AnvilCraftRunnable(this, player, (DynamicAnvilMenu) anvilMenu);
		runnable.runTaskTimer(LibTest.getInstance(), 30, 1);
		return runnable;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private int requiredHammering = 10;

		private int mistakeCost = 3;

		private int timeLaps = 30;

		private Builder() { }

		public Builder requiredHammering(final int requiredHammering) {
			this.requiredHammering = requiredHammering;
			return this;
		}

		public Builder mistakeCost(final int mistakeCost) {
			this.mistakeCost = mistakeCost;
			return this;
		}

		public Builder timeLaps(final int timeLaps) {
			this.timeLaps = timeLaps;
			return this;
		}

		public AnvilCraft build() {
			return new AnvilCraft(this);
		}

	}

	public static class AnvilCraftRunnable extends CraftRunnable {

		private final AnvilCraft anvilCraft;

		private final Player player;

		private final DynamicAnvilMenu anvilMenu;

		@Getter
		private int score = 0;

		@Getter
		private int nextHitTargetLocation;

		private int globalTimer = 0;

		private AnvilCraftRunnable(final AnvilCraft anvilCraft, final Player player, final DynamicAnvilMenu anvilMenu) {
			this.anvilCraft = anvilCraft;
			this.player = player;
			this.anvilMenu = anvilMenu;
			this.nextHitTargetLocation = new Random().nextInt(27);
		}

		@Override
		public void run() {
			//If the score is high enough -> success
			if(score >= anvilCraft.requiredHammering) {
				//Let the menu knows the craft is over
				anvilMenu.setFinished(true);
				anvilMenu.setCancelReinstantiation(true);
				//And re displays it
				anvilMenu.display(player);
				cancel();

				return;
			}
			//If the score falls below 0 -> failure
			if(score < 0) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
				//Let the menu knows the craft failed
				anvilMenu.setFailed(true);
				anvilMenu.setCancelReinstantiation(true);
				//And re displays it
				anvilMenu.display(player);
				cancel();

				return;
			}
			//If no click has been made during the given time laps -> considered as fail
			if(globalTimer >= anvilCraft.timeLaps) {
				fail();
			}
			globalTimer++;
		}

		public void buildUp() {
			//Increase the score
			score++;
			//Reset the global timer
			globalTimer = 0;
			//Reset the position of the next hit target
			setNextHitTarget();
		}

		public void fail() {
			score -= anvilCraft.mistakeCost;
			globalTimer = 0;
			setNextHitTarget();
		}

		public void setNextHitTarget() {
			//Generate a position within 3 rows for the hit button to appear
			this.nextHitTargetLocation = new Random().nextInt(27);
			anvilMenu.setCancelReinstantiation(true);
			anvilMenu.display(player);
		}

		/**
		 *
		 * @return the score on a scale of 0 to 9 for display purpose
		 */
		public int getProportionalScore() {
			return (int) ((score * 1.0 / anvilCraft.requiredHammering) * 9.0);
		}

	}

}
