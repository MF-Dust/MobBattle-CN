package me.mxtery.mobbattle.helpers;

import me.mxtery.mobbattle.MobBattle;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RunnableHelper {
    private static MobBattle plugin;


    public static void init(MobBattle plugin){
        RunnableHelper.plugin = plugin;
    }
    public static void runTaskLater(Runnable runnable, int delay){
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);

    }






}
