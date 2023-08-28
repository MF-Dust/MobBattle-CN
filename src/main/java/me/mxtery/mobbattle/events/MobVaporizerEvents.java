package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.Keys;
import me.mxtery.mobbattle.helpers.PlayerHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

public class MobVaporizerEvents implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_VAPORIZER, PersistentDataType.INTEGER)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_VAPORIZER, PersistentDataType.INTEGER)) {
            return;
        }
        event.setDamage(Integer.MAX_VALUE);
    }


    @EventHandler
    public void onAreaKill(PlayerInteractEvent event) {


        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_VAPORIZER, PersistentDataType.INTEGER)) {
            return;
        }
        event.setCancelled(true);

        for (Entity entity : event.getPlayer().getWorld().getNearbyEntities(player.getTargetBlock(null, 5).getLocation(), 5, 5, 5)) {
            if (entity instanceof Player) {
                continue;
            }
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setHealth(0);
            } else {
                entity.remove();
            }
        }
    }

}
