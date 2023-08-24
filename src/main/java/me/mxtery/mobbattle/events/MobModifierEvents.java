package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.Keys;
import me.mxtery.mobbattle.helpers.MessageHelper;
import me.mxtery.mobbattle.helpers.PlayerHelper;
import org.bukkit.ChatColor;
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
@EventHandler
    public void onMobParalyze(PlayerInteractEntityEvent event){
    Player player = event.getPlayer();
    if (event.getHand() != EquipmentSlot.HAND){
        return;
    }
    if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)){
        return;
    }
    Entity entity = event.getRightClicked();
    if (!(entity instanceof LivingEntity)){
return;
    }
if (player.isSneaking()){
    return;
}
    event.setCancelled(true);

    LivingEntity livingEntity = (LivingEntity) entity;
    boolean hasAI = livingEntity.hasAI();
    if (hasAI){
        livingEntity.setAI(false);
    }else{
        livingEntity.setAI(true);
    }
    hasAI = livingEntity.hasAI();
    MessageHelper.sendPluginMessage(player, ChatColor.GREEN + livingEntity.getName() + "'s AI was set to: " + ChatColor.YELLOW + ChatColor.BOLD + hasAI + ".");
}
@EventHandler
    public void onMobInvisible(PlayerInteractEntityEvent event){
    Player player = event.getPlayer();
    if (event.getHand() != EquipmentSlot.HAND){
        return;
    }
    if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)){
        return;
    }
    Entity entity = event.getRightClicked();
    if (!(entity instanceof LivingEntity)){
return;
    }
if (!player.isSneaking()){
    return;
}
    event.setCancelled(true);

    LivingEntity livingEntity = (LivingEntity) entity;
    boolean isInvisible = livingEntity.isInvisible();
    if (isInvisible){
        livingEntity.setInvisible(false);
    }else{
        livingEntity.setInvisible(true);
    }
    isInvisible = livingEntity.isInvisible();
    MessageHelper.sendPluginMessage(player, ChatColor.GREEN + livingEntity.getName() + "'s invisibility was set to: " + ChatColor.YELLOW + ChatColor.BOLD + isInvisible + ".");
}
    @EventHandler
    public void onMobInvincibility(EntityDamageByEntityEvent event){
    if (!(event.getDamager() instanceof Player)){
        return;
    }

        Player player = (Player) event.getDamager();
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_MODIFIER, PersistentDataType.INTEGER)){
            return;
        }
        event.setCancelled(true);
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)){
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        boolean hasInvulnerability = livingEntity.isInvulnerable();
        if (hasInvulnerability){
            livingEntity.setInvulnerable(false);
        }else{
            livingEntity.setInvulnerable(true);
        }
        hasInvulnerability = livingEntity.isInvulnerable();
        MessageHelper.sendPluginMessage(player, ChatColor.GREEN + livingEntity.getName() + "'s invincibility was set to: " + ChatColor.YELLOW + ChatColor.BOLD + hasInvulnerability + "." + ChatColor.GREEN  + (hasInvulnerability ? " They can now only be damaged by players in creative mode!" : ""));
    }




}
