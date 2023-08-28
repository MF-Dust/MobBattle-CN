package me.mxtery.mobbattle.helpers;

import me.mxtery.mobbattle.MobBattle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;

public class ConfigManager {
    private static FileConfiguration config;
    private static MobBattle plugin;
    private static HashSet<String> boolOptions;
    private static HashSet<String> stringOptions;
    private static HashSet<String> intOptions;
    private static HashSet<String> floatOptions;

    public static void setupConfig(MobBattle plugin) {
        ConfigManager.plugin = plugin;
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        boolOptions = new HashSet<>();
        stringOptions = new HashSet<>();
        intOptions = new HashSet<>();
        floatOptions = new HashSet<>();

        boolOptions.add("true1v1");
        boolOptions.add("actionbar.actionbar-messages");
        boolOptions.add("chat.send-non-essential-chat-messages");
        boolOptions.add("sounds.play-sounds");


        stringOptions.add("no-permissions-message");


        intOptions.add("actionbar.ticks-between-actionbar-messages");


        floatOptions.add("sounds.sound-volume");

    }

    public static void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public static boolean getPlaySounds() {
        try {
            return config.getBoolean("sounds.play-sounds");
        } catch (Exception e) {
            return config.getDefaults().getBoolean("sounds.play-sounds");
        }
    }

    public static float getSoundVolume() {
        try {
            return config.getInt("sounds.sound-volume");
        } catch (Exception e) {
            return config.getDefaults().getInt("sounds.sound-volume");
        }
    }


    public static boolean getNonEssentialChatMessages() {
        try {
            return config.getBoolean("chat.send-non-essential-chat-messages");
        } catch (Exception e) {
            return config.getDefaults().getBoolean("chat.send-non-essential-chat-messages");
        }
    }

    public static String getNoPermissionMessage() {
        try {
            return config.getString("no-permissions-message");
        } catch (Exception e) {
            return config.getDefaults().getString("no-permissions-message");
        }
    }

    public static int getTicksBetweenActionBarMessages() {
        try {
            return config.getInt("actionbar.ticks-between-actionbar-messages");
        } catch (Exception e) {
            return config.getDefaults().getInt("actionbar.ticks-between-actionbar-messages");
        }
    }

    public static boolean getActionBarMessagesEnabled() {
        try {
            return config.getBoolean("actionbar.actionbar-messages");
        } catch (Exception e) {
            return config.getDefaults().getBoolean("actionbar.actionbar-messages");
        }
    }

    public static boolean getTrue1v1() {
        try {
            return config.getBoolean("true1v1");
        } catch (Exception e) {
            return config.getDefaults().getBoolean("true1v1");
        }
    }


    public static boolean testConfig() {
        try {
            for (String option : boolOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }

                if (!plugin.getConfig().isBoolean(option)) {
                    return false;
                }
            }
            for (String option : stringOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }

                if (!plugin.getConfig().isString(option)) {
                    return false;
                }
            }
            for (String option : intOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }

                if (!plugin.getConfig().isInt(option)) {
                    return false;
                }
            }
            for (String option : floatOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }

                if (!plugin.getConfig().isDouble(option)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    public static boolean fixConfig() {
        try {
            for (String option : boolOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
                if (!plugin.getConfig().isBoolean(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
            }
            for (String option : stringOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
                if (!plugin.getConfig().isString(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
            }
            for (String option : intOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
                if (!plugin.getConfig().isInt(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
            }
            for (String option : floatOptions) {

                if (!plugin.getConfig().isSet(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
                if (!plugin.getConfig().isDouble(option)) {
                    plugin.getConfig().set(option, plugin.getConfig().getDefaults().get(option));
                    plugin.saveConfig();
                }
            }
            return testConfig();
        } catch (Exception e) {
            return false;
        }
    }


}
