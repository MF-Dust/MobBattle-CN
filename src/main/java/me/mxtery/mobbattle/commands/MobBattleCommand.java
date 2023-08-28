package me.mxtery.mobbattle.commands;

import me.mxtery.mobbattle.MobBattle;
import me.mxtery.mobbattle.helpers.ConfigManager;
import me.mxtery.mobbattle.helpers.MessageHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Objects;

public class MobBattleCommand implements CommandExecutor {
    private final MobBattle plugin;

    public MobBattleCommand(MobBattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("mobbattle.commands")) {
            MessageHelper.sendPluginMessage(sender, Objects.requireNonNull(ConfigManager.getNoPermissionMessage()));
            return true;
        }

        if (args.length < 1) {
            TextComponent pluginName = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r "));
            TextComponent invalidArgs = new TextComponent(ChatColor.RED + "Invalid arguments! ");

            TextComponent help = new TextComponent(ChatColor.YELLOW + "/mb help");
            help.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb help"));
            help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb help")));


            TextComponent forMoreInfo = new TextComponent(ChatColor.RED + " for more info!");

            sender.spigot().sendMessage(pluginName, invalidArgs, help, forMoreInfo);
            return true;
        }

        switch (args[0]) {
            case "give":
                ItemStack toGive = null;
                int amount;

                if (args.length < 3) {
                    MessageHelper.sendPluginMessage(sender, "&cInvalid arguments! /mb give <player> <item> <amount>");
                    break;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    MessageHelper.sendPluginMessage(sender, "&cInvalid arguments! /mb give <player> <item> <amount>");
                    break;
                }

                for (ItemStack item : plugin.getItemManager().getItems()) {

                    if (item.getItemMeta().getPersistentDataContainer().getKeys().contains(NamespacedKey.fromString(args[2], plugin))) {
                        toGive = item;
                        break;
                    }
                }
                if (toGive == null) {
                    MessageHelper.sendPluginMessage(sender, "&cInvalid arguments! /mb give <player> <item> <amount>");
                    break;
                }
                try {
                    if (args.length == 3) {
                        amount = 1;
                    } else {
                        amount = Integer.parseInt(args[3]);
                    }
                    if (amount <= 0) {
                        break;
                    }
                    toGive.setAmount(amount);
                    target.getInventory().addItem(toGive);

                    MessageHelper.sendPluginMessage(sender, "&eGave " + amount + " " + toGive.getItemMeta().getDisplayName() + "&e" + (amount > 1 ? "s" : "") + " to " + target.getName());
                } catch (Exception e) {
                    MessageHelper.sendPluginMessage(sender, ChatColor.RED + "Invalid arguments! /mb give <player> <item> <amount>");
                    break;
                }


                break;
            case "help":
                sender.sendMessage(ChatColor.GREEN + "----------------------------------------");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&6 List of commands"));

                TextComponent dash = new TextComponent(ChatColor.GREEN + "- ");

                TextComponent help = new TextComponent(ChatColor.YELLOW + "/mb help");
                help.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb help"));
                help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb help")));

                TextComponent config = new TextComponent(ChatColor.YELLOW + "/mb config");
                config.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb config"));
                config.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb config")));

                TextComponent give = new TextComponent(ChatColor.YELLOW + "/mb give");
                give.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb give"));
                give.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb give")));


                sender.spigot().sendMessage(dash, config);
                sender.spigot().sendMessage(dash, give);
                sender.spigot().sendMessage(dash, help);
                sender.sendMessage(ChatColor.GREEN + "----------------------------------------");
                break;
            case "config":
                if (args.length < 2) {
                    MessageHelper.sendPluginMessage(sender, "&cInvalid arguments! /mb config <fix/reload/reset>");
                    break;
                }
                switch (args[1]) {
                    case "fix":
                        if (ConfigManager.fixConfig()) {
                            MessageHelper.sendPluginMessage(sender, "&aYour config has been fixed!");
                        } else {
                            TextComponent pluginName = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r "));
                            TextComponent wrong = new TextComponent(ChatColor.RED + "Something went wrong! If you cannot fix the error, try ");

                            TextComponent reset = new TextComponent(ChatColor.YELLOW + "/mb config reset!");
                            reset.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb config reset"));
                            reset.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb config reset")));

                            sender.spigot().sendMessage(pluginName, wrong, reset);

                        }
                        break;
                    case "reload":
                        ConfigManager.reloadConfig();
                        MessageHelper.sendPluginMessage(sender, "&aConfig has been reloaded!");
                        if (!ConfigManager.testConfig()) {
                            TextComponent pluginName = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r "));

                            TextComponent wrong = new TextComponent(ChatColor.RED + "Something is wrong with the config! To fix it, try ");

                            TextComponent fix = new net.md_5.bungee.api.chat.TextComponent(ChatColor.YELLOW + "/mb config fix");
                            fix.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb config fix"));
                            fix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb config fix")));

                            TextComponent persist = new TextComponent(ChatColor.RED + ". If this problem persists, try ");

                            TextComponent reset = new TextComponent(ChatColor.YELLOW + "/mb config reset!");
                            reset.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb config reset"));
                            reset.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb config reset")));

                            sender.spigot().sendMessage(pluginName, wrong, fix, persist, reset);
                        }
                        break;
                    case "reset":
                        File file = new File(plugin.getDataFolder(), "config.yml");
                        file.delete();
                        plugin.saveDefaultConfig();
                        ConfigManager.reloadConfig();
                        MessageHelper.sendPluginMessage(sender, "&aConfig has been reset!");
                        break;
                    default:
                        MessageHelper.sendPluginMessage(sender, "&cInvalid arguments! /mb config <fix/reload/reset>");
                        break;
                }


                break;
            default:
                TextComponent pluginName = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&l[&bMobBattle&6&l]&r "));
                TextComponent invalidArgs = new TextComponent(ChatColor.RED + "Invalid arguments! ");

                TextComponent help1 = new TextComponent(ChatColor.YELLOW + "/mb help");
                help1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mb help"));
                help1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Run" + ChatColor.YELLOW + " /mb help")));


                TextComponent forMoreInfo = new TextComponent(ChatColor.RED + " for more info!");

                sender.spigot().sendMessage(pluginName, invalidArgs, help1, forMoreInfo);
        }


        return true;
    }
}
