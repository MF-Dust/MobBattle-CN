package me.mxtery.mobbattle.helpers;

import me.mxtery.mobbattle.MobBattle;
import me.mxtery.mobbattle.commands.MobBattleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Debug {
    private static MobBattle plugin;
    public static void init(MobBattle plugin){
        Debug.plugin = plugin;
    }
    public static void sendDebugMessage(Player player, String string){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r - &e[DEBUG]&r " + string));
    }
    public static void sendConsoleMessage(String string, Level level){
        plugin.getLogger().log(level, string);
    }
    public static void sendBroadcastMessage(String string){
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r - &e[DEBUG]&r " + string));
    }
}
