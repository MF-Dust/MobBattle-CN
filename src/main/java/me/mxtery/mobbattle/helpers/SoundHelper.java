package me.mxtery.mobbattle.helpers;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundHelper {
    public static void playErrorSound(Player player) {
        if (!ConfigManager.getPlaySounds()) {
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, ConfigManager.getSoundVolume(), 0.5F);
    }

    public static void playDingSound(Player player) {
        if (!ConfigManager.getPlaySounds()) {
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, ConfigManager.getSoundVolume(), 5F);
    }

    public static void playLevelUpSound(Player player) {
        if (!ConfigManager.getPlaySounds()) {
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, ConfigManager.getSoundVolume(), 1F);
    }

    public static void playDragonSound(Player player) {
        if (!ConfigManager.getPlaySounds()) {
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, ConfigManager.getSoundVolume() / 2, 1F);
    }
}
