package me.mxtery.mobbattle;

import me.mxtery.mobbattle.events.MobBattleWandEvents;
import me.mxtery.mobbattle.events.TeamBattleWandEvents;
import me.mxtery.mobbattle.helpers.*;
import me.mxtery.mobbattle.instance.TeamBattleInstance;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


public class ActionBarSender extends BukkitRunnable implements Listener {
    private MobBattleWandEvents mobBattleWandEvents;
    private TeamBattleWandEvents teamBattleWandEvents;

    public void start(MobBattle plugin, MobBattleWandEvents mobBattleWandEvents, TeamBattleWandEvents teamBattleWandEvents) {
        this.mobBattleWandEvents = mobBattleWandEvents;
        this.teamBattleWandEvents = teamBattleWandEvents;
        runTaskTimer(plugin, 0, ConfigManager.getTicksBetweenActionBarMessages());
    }

    @EventHandler
    public void onPlayerSwitchOutItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (ItemHelper.isPluginItem(player.getInventory().getItemInMainHand())) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
        }
    }

    @EventHandler
    public void onPlayerSwitchToItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (ItemHelper.isPluginItem(player.getInventory().getItem(event.getNewSlot()))) {
            updateActionBar(player, event.getNewSlot());
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (ItemHelper.isPluginItem(event.getItemDrop().getItemStack())) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
        }
    }


    @Override
    public void run() {
        updateActionBar();
    }


    private void updateActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateActionBar(player, player.getInventory().getHeldItemSlot());
        }
    }

    private void updateActionBar(Player player, int slot) {
        StringBuilder builder = new StringBuilder();
        if (PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER, slot)) {
            Mob entity1 = null;
            if (mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()) != null) {
                entity1 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()));
            }
            Mob entity2 = null;
            if (mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()) != null) {
                entity2 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()));
            }

            if (mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId()) != null && mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId())) {
                if (entity1 != null && entity2 != null) {
                    builder.append(ChatColor.translateAlternateColorCodes('&', "&aBattle Started: &e&l" + entity1.getName() + "&e - &c❤ " + MathHelper.round(entity1.getHealth(), 2) + (entity1.getAbsorptionAmount() == 0 ? "" : "&e, &6❤ " + MathHelper.round(entity1.getAbsorptionAmount(), 2)) + "&a vs &e&l" + entity2.getName() + "&e - &c❤ " + MathHelper.round(entity2.getHealth(), 2) + (entity2.getAbsorptionAmount() == 0 ? "" : "&e, &6❤ " + MathHelper.round(entity2.getAbsorptionAmount(), 2))));
                }

            } else {


                builder.append(ChatColor.translateAlternateColorCodes('&', "&eEntity 1: " + (entity1 == null ? "&c&lNOT CHOSEN" : "&a&l" + entity1.getName() + "&e - &c❤ " + MathHelper.round(entity1.getHealth(), 2) + (entity1.getAbsorptionAmount() == 0 ? "" : "&e, &6❤ " + MathHelper.round(entity1.getAbsorptionAmount(), 2))))).append(ChatColor.LIGHT_PURPLE + "     |     ");

                builder.append(ChatColor.translateAlternateColorCodes('&', "&eEntity 2: " + (entity2 == null ? "&c&lNOT CHOSEN" : "&a&l" + entity2.getName() + "&e - &c❤ " + MathHelper.round(entity2.getHealth(), 2) + (entity2.getAbsorptionAmount() == 0 ? "" : "&e, &6❤ " + MathHelper.round(entity2.getAbsorptionAmount(), 2)))));

            }


        } else if (PlayerHelper.playerHoldingItem(player, Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER)) {
            HashSet<UUID> redTeam;
            HashSet<UUID> blueTeam;
            if (!teamBattleWandEvents.getPlayerToBattle().containsKey(player.getUniqueId()) || teamBattleWandEvents.getPlayerToBattle() == null) {
                redTeam = teamBattleWandEvents.getPlayerToRedTeam().get(player.getUniqueId());
                blueTeam = teamBattleWandEvents.getPlayerToBlueTeam().get(player.getUniqueId());
            } else {
                TeamBattleInstance teamBattleInstance = teamBattleWandEvents.getPlayerToBattle().get(player.getUniqueId());
                if (teamBattleInstance == null) {
                    return;
                }
                redTeam = teamBattleInstance.getRedTeam();
                blueTeam = teamBattleInstance.getBlueTeam();
            }
            List<String> redTeamNames = new ArrayList<>();
            List<String> blueTeamNames = new ArrayList<>();
            if (redTeam != null) {
                for (UUID uuid : redTeam) {
                    if (uuid == null) {
                        continue;
                    }
                    Entity entity = Bukkit.getEntity(uuid);
                    if (!(entity instanceof Mob)) {
                        continue;
                    }
                    Mob mob = (Mob) entity;
                    redTeamNames.add(mob.getName());
                }
            }
            if (blueTeam != null) {
                for (UUID uuid : blueTeam) {
                    if (uuid == null) {
                        continue;
                    }
                    Entity entity = Bukkit.getEntity(uuid);
                    if (!(entity instanceof Mob)) {
                        continue;
                    }
                    Mob mob = (Mob) entity;
                    blueTeamNames.add(mob.getName());
                }
            }
            String redTeamList = StringFormatter.condenseString(redTeamNames);
            String blueTeamList = StringFormatter.condenseString(blueTeamNames);
            builder.append("&c&lRed Team: ").append(redTeamList).append(ChatColor.LIGHT_PURPLE + "     |     ").append("&9&lBlue Team: ").append(blueTeamList);

        } else {
            return;
        }

        if (!builder.toString().equalsIgnoreCase("")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', builder.toString())));
        }
    }
}
