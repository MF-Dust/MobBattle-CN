package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.Keys;
import me.mxtery.mobbattle.helpers.Debug;
import me.mxtery.mobbattle.helpers.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TeamBattleWandEvents implements Listener {

    @EventHandler
    public void onRedTeamSelect(EntityDamageByEntityEvent event){

    }
    @EventHandler
    public void onBlueTeamSelect(PlayerInteractEntityEvent event){

    }
    @EventHandler
    public void onRedTeamReset(PlayerInteractEvent event){

    }
    @EventHandler
    public void onBlueTeamReset(PlayerInteractEvent event){

    }
    @EventHandler
    public void onStartBattle(PlayerDropItemEvent event){

    }












}
