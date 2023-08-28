package me.mxtery.mobbattle.commands;

import me.mxtery.mobbattle.Keys;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MobBattleTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("give", "help", "config"), new ArrayList<>());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                List<String> players = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    players.add(player.getName());
                }
                return StringUtil.copyPartialMatches(args[1], players, new ArrayList<>());
            } else if (args[0].equalsIgnoreCase("config")) {
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("reload", "fix", "reset"), new ArrayList<>());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                List<String> keys = new ArrayList<>();
                for (NamespacedKey key : Keys.getKeys()) {
                    keys.add(key.getKey());
                }
                return StringUtil.copyPartialMatches(args[2], keys, new ArrayList<>());
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("give")) {

                return StringUtil.copyPartialMatches(args[3], Arrays.asList("1", "2", "3", "4", "5"), new ArrayList<>());
            }
        }
        return Collections.singletonList("");
    }
}
