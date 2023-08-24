package me.mxtery.mobbattle;

import me.mxtery.mobbattle.events.MobBattleWandEvents;
import me.mxtery.mobbattle.helpers.ConfigManager;
import me.mxtery.mobbattle.helpers.Debug;
import me.mxtery.mobbattle.helpers.ItemHelper;
import me.mxtery.mobbattle.helpers.PlayerHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.decimal4j.util.DoubleRounder;

import java.util.logging.Level;

public class ActionBarSender extends BukkitRunnable implements Listener {
private MobBattleWandEvents mobBattleWandEvents;
    public void start(MobBattle plugin, MobBattleWandEvents mobBattleWandEvents){
        this.mobBattleWandEvents = mobBattleWandEvents;
        runTaskTimer(plugin, 0, ConfigManager.getTicksBetweenActionBarMessages());
    }

    @EventHandler
    public void onPlayerSwitchOutItem(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        if (ItemHelper.isPluginItem(player.getInventory().getItemInMainHand())){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(""));
        }
    }
    @EventHandler
    public void onPlayerSwitchToItem(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        if (ItemHelper.isPluginItem(player.getInventory().getItem(event.getNewSlot()))){
            updateActionBar(player, event.getNewSlot());
        }
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if (ItemHelper.isPluginItem(event.getItemDrop().getItemStack())){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(""));
        }
    }



    @Override
    public void run() {
       updateActionBar();
    }



    private void updateActionBar(){
        for (Player player : Bukkit.getOnlinePlayers()){
            StringBuilder builder = new StringBuilder();
            if (PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER)){
                Mob entity1 = null;
                if (mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()) != null){
                    entity1 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()));
                }
                Mob entity2 = null;
                if (mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()) != null){
                    entity2 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()));
                }
                if (entity1 == null || entity2 == null || entity1.isDead() || entity2.isDead()){
                    if (mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId()) != null && mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId())){
                        mobBattleWandEvents.endBattle(entity1, entity2, player);
                    }
                }

                if (mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId()) != null && mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId())){
                    if (entity1 != null && entity2 != null){
                        builder.append(ChatColor.translateAlternateColorCodes('&', "&aBattle Started: &e&l" + entity1.getName() + "&e - &c❤ " + DoubleRounder.round(entity1.getHealth(), 2) + (entity1.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity1.getAbsorptionAmount(), 2))+ "&a vs &e&l" + entity2.getName()+ "&e - &c❤ " + DoubleRounder.round(entity2.getHealth(), 2) + (entity2.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity2.getAbsorptionAmount(), 2))));
                    }
                }else{
                builder.append(ChatColor.translateAlternateColorCodes('&', "&eEntity 1: " + (entity1==null ? "&c&lNOT CHOSEN" : "&a&l" + entity1.getName() + "&e - &c❤ " + DoubleRounder.round(entity1.getHealth(), 2) + (entity1.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity1.getAbsorptionAmount(), 2))))).append(ChatColor.LIGHT_PURPLE + "     |     ");;

                builder.append(ChatColor.translateAlternateColorCodes('&', "&eEntity 2: " + (entity2==null ? "&c&lNOT CHOSEN" : "&a&l" + entity2.getName() + "&e - &c❤ " + DoubleRounder.round(entity2.getHealth(), 2) + (entity2.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity2.getAbsorptionAmount(), 2)))));

                }


            }else if (PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)){

            }else if (PlayerHelper.playerHoldingItem(player, Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER)){

            }else{
                continue;
            }

            if (!builder.toString().equalsIgnoreCase("")){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(builder.toString()));
            }



        }
    }
    private void updateActionBar(Player player, int slot){
            StringBuilder builder = new StringBuilder();
            if (PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER, slot)){
                Mob entity1 = null;
                if (mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()) != null){
                    entity1 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()));
                }
                Mob entity2 = null;
                if (mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()) != null){
                    entity2 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()));
                }

                if (mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId()) != null && mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId())){
                    if (entity1 != null && entity2 != null){
                        builder.append(ChatColor.translateAlternateColorCodes('&', "&aBattle Started: &e&l" + entity1.getName() + "&e - &c❤ " + DoubleRounder.round(entity1.getHealth() , 2) + (entity1.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity1.getAbsorptionAmount(), 2))+ "&a vs &e&l" + entity2.getName()+ "&e - &c❤ " + DoubleRounder.round(entity2.getHealth(), 2) + (entity2.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity2.getAbsorptionAmount(), 2))));
                    }

                }else{



                builder.append(ChatColor.translateAlternateColorCodes('&', "&eEntity 1: " + (entity1==null ? "&c&lNOT CHOSEN" : "&a&l" + entity1.getName() + "&e - &c❤ " + DoubleRounder.round(entity1.getHealth(), 2) + (entity1.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity1.getAbsorptionAmount(), 2))))).append(ChatColor.LIGHT_PURPLE + "     |     ");;

                builder.append(ChatColor.translateAlternateColorCodes('&', "&eEntity 2: " + (entity2==null ? "&c&lNOT CHOSEN" : "&a&l" + entity2.getName() + "&e - &c❤ " + DoubleRounder.round(entity2.getHealth(), 2) + (entity2.getAbsorptionAmount() == 0? "" : "&e, &6❤ " + DoubleRounder.round(entity2.getAbsorptionAmount(), 2)))));

                }


            }else if (PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)){

            }else if (PlayerHelper.playerHoldingItem(player, Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER)){

            }else{
                return;
            }

            if (!builder.toString().equalsIgnoreCase("")){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(builder.toString()));
            }
    }
}
