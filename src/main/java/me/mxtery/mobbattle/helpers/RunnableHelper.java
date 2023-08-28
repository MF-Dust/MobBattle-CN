package me.mxtery.mobbattle.helpers;

import me.mxtery.mobbattle.MobBattle;
import org.bukkit.Bukkit;

public class RunnableHelper {
    private static MobBattle plugin;


    public static void init(MobBattle plugin) {
        RunnableHelper.plugin = plugin;
    }

    public static void runTaskLater(Runnable runnable, int delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);

    }

    public static void runTaskTimer(Runnable runnable, int delay, int period) {
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);

    }
}
