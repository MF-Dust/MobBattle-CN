package me.mxtery.mobbattle.helpers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageHelper {
    public static void sendPluginMessage(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r " + ChatColor.translateAlternateColorCodes('&', msg)));
    }
    public static void sendNonEssentialPluginMessage(CommandSender sender, String msg){
        if (!ConfigManager.getNonEssentialChatMessages()){
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r " + ChatColor.translateAlternateColorCodes('&', msg)));
    }


}
