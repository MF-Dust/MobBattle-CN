package me.mxtery.mobbattle;

import me.mxtery.mobbattle.bstats.Metrics;
import me.mxtery.mobbattle.commands.MobBattleCommand;
import me.mxtery.mobbattle.commands.MobBattleTabCompleter;
import me.mxtery.mobbattle.events.*;
import me.mxtery.mobbattle.helpers.ConfigManager;
import me.mxtery.mobbattle.helpers.RunnableHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobBattle extends JavaPlugin {
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        new UpdateChecker(this, 100660);
        ConfigManager.setupConfig(this);
        Keys.init(this);
        RunnableHelper.init(this);
        itemManager = new ItemManager();
        getCommand("mobbattle").setExecutor(new MobBattleCommand(this));
        getCommand("mobbattle").setTabCompleter(new MobBattleTabCompleter());
        MobBattleWandEvents mobBattleWandEvents;
        TeamBattleWandEvents teamBattleWandEvents;

        teamBattleWandEvents = new TeamBattleWandEvents(this, itemManager);
        mobBattleWandEvents = new MobBattleWandEvents(teamBattleWandEvents, itemManager);
        teamBattleWandEvents.setBattleWandEvents(mobBattleWandEvents);

        getServer().getPluginManager().registerEvents(mobBattleWandEvents, this);
        getServer().getPluginManager().registerEvents(new MobModifierEvents(), this);
        getServer().getPluginManager().registerEvents(new MobVaporizerEvents(), this);
        getServer().getPluginManager().registerEvents(teamBattleWandEvents, this);
        getServer().getPluginManager().registerEvents(new UpdateItemEvents(itemManager), this);
        if (ConfigManager.getActionBarMessagesEnabled()) {
            ActionBarSender actionBarSender = new ActionBarSender();
            getServer().getPluginManager().registerEvents(actionBarSender, this);
            actionBarSender.start(this, mobBattleWandEvents, teamBattleWandEvents);
        }
        MobRemovedChecker mobRemovedChecker = new MobRemovedChecker();
        mobRemovedChecker.start(this, mobBattleWandEvents);


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
            getServer().getConsoleSender().spigot().sendMessage(pluginName, wrong, fix, persist, reset);
            for (Player player : getServer().getOnlinePlayers()) {
                if (player.isOp()) {
                    player.spigot().sendMessage(pluginName, wrong, fix, persist, reset);
                }
            }
        }


//////////////////////////////////////////////////////////////////////////////////////// METRICS
        int pluginId = 14677; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        // Optional: Add custom charts
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));


    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
