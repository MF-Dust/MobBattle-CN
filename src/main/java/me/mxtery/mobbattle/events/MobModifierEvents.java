package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.Keys;
import me.mxtery.mobbattle.helpers.MessageHelper;
import me.mxtery.mobbattle.helpers.PlayerHelper;
import me.mxtery.mobbattle.helpers.SoundHelper;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

public class MobModifierEvents implements Listener {
    //TODO 2.1:, EQUIP MOB ARMOR, APPLY POTION FX
    @EventHandler
    public void onMobParalyze(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)) {
            return;
        }
        Entity entity = event.getRightClicked();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        event.setCancelled(true);

        LivingEntity livingEntity = (LivingEntity) entity;
        boolean hasAI = livingEntity.hasAI();
        livingEntity.setAI(!hasAI);
        SoundHelper.playDingSound(player);
        hasAI = livingEntity.hasAI();
        MessageHelper.sendPluginMessage(player, ChatColor.GREEN + livingEntity.getName() + "'s &aAI was set to: " + ChatColor.YELLOW + ChatColor.BOLD + hasAI + ".");
    }

    @EventHandler
    public void onMobInvisible(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)) {
            return;
        }
        Entity entity = event.getRightClicked();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        event.setCancelled(true);

        LivingEntity livingEntity = (LivingEntity) entity;
        boolean isInvisible = livingEntity.isInvisible();
        livingEntity.setInvisible(!isInvisible);
        SoundHelper.playDingSound(player);
        isInvisible = livingEntity.isInvisible();
        MessageHelper.sendPluginMessage(player, ChatColor.GREEN + livingEntity.getName() + "'s &ainvisibility was set to: " + ChatColor.YELLOW + ChatColor.BOLD + isInvisible + ".");
    }

    @EventHandler
    public void onMobInvincibility(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        if (player.isSneaking()) {
            return;
        }
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)) {
            return;
        }
        event.setCancelled(true);
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        boolean hasInvulnerability = livingEntity.isInvulnerable();
        livingEntity.setInvulnerable(!hasInvulnerability);
        SoundHelper.playDingSound(player);
        hasInvulnerability = livingEntity.isInvulnerable();
        MessageHelper.sendPluginMessage(player, ChatColor.GREEN + livingEntity.getName() + "'s &a无敌被设置为: " + ChatColor.YELLOW + ChatColor.BOLD + hasInvulnerability + "." + ChatColor.GREEN + (hasInvulnerability ? " 它只能被创造模式的的玩家攻击!" : ""));
    }

    @EventHandler
    public void onMobHeal(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        if (!player.isSneaking()) {
            return;
        }
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)) {
            return;
        }
        event.setCancelled(true);
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        SoundHelper.playDingSound(player);
        MessageHelper.sendPluginMessage(player, ChatColor.GREEN + livingEntity.getName() + "'s 的HP被重置为: " + ChatColor.YELLOW + ChatColor.BOLD + livingEntity.getHealth() + ".");
    }


}
