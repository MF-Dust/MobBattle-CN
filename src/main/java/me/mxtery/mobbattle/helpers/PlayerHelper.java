package me.mxtery.mobbattle.helpers;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PlayerHelper {
    public static boolean playerHoldingItem(Player player, NamespacedKey item, PersistentDataType dataType) {

        if (!player.getInventory().getItemInMainHand().hasItemMeta()) {
            return false;
        }
        return player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(item, dataType);
    }

    public static boolean playerHoldingItem(Player player, NamespacedKey item, PersistentDataType dataType, int slot) {

        if (player.getInventory().getItem(slot) == null) {
            return false;
        }

        if (!player.getInventory().getItem(slot).hasItemMeta()) {
            return false;
        }
        return player.getInventory().getItem(slot).getItemMeta().getPersistentDataContainer().has(item, dataType);
    }
}
