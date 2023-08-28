package me.mxtery.mobbattle.helpers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.awt.*;

public class MessageHelper {
    public static String getPluginName() {
        return ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r ");
    }

    public static void sendPluginMessage(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r " + ChatColor.translateAlternateColorCodes('&', msg)));
    }

    public static void sendNonEssentialPluginMessage(CommandSender sender, String msg) {
        if (!ConfigManager.getNonEssentialChatMessages()) {
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r " + ChatColor.translateAlternateColorCodes('&', msg)));
    }

    public static void sendGradientPluginMessage(CommandSender sender, String msg) {
        sender.sendMessage(getPluginName() + msg);
    }

    public static String createGradient(String string, float hue, float saturation, float brightness) {
        hue = hue / 365;
        saturation = saturation / 100;
        brightness = brightness / 100;
        Color color = Color.getHSBColor(hue, saturation, brightness);
        char[] charString = string.toCharArray();
        StringBuilder toReturn = new StringBuilder();
        float currentSaturation = saturation;
        for (char c : charString) {
            toReturn.append(net.md_5.bungee.api.ChatColor.of(color)).append(c);
            currentSaturation = currentSaturation + (((100f - saturation * 100) / charString.length) / 100);
            color = Color.getHSBColor(hue, currentSaturation, brightness);
        }

        return toReturn.toString();
    }


}
