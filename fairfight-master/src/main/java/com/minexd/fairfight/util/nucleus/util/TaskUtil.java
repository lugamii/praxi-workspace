package com.minexd.fairfight.util.nucleus.util;


import com.minexd.fairfight.FairFight;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

	public static void run(Runnable runnable) {
		FairFight.getInstance().getServer().getScheduler().runTask(FairFight.getInstance(), runnable);
	}

	public static void runTimer(Runnable runnable, long delay, long timer) {
		FairFight.getInstance().getServer().getScheduler().runTaskTimer(FairFight.getInstance(), runnable, delay, timer);
	}

	public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
		runnable.runTaskTimer(FairFight.getInstance(), delay, timer);
	}

	public static void runLater(Runnable runnable, long delay) {
		FairFight.getInstance().getServer().getScheduler().runTaskLater(FairFight.getInstance(), runnable, delay);
	}

	public static void runAsync(Runnable runnable) {
		FairFight.getInstance().getServer().getScheduler().runTaskAsynchronously(FairFight.getInstance(), runnable);
	}

}
