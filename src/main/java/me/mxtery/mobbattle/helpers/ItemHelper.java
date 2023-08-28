package me.mxtery.mobbattle.helpers;

import me.mxtery.mobbattle.Keys;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemHelper {
    public static boolean isPluginItem(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        return itemStack.getItemMeta().getPersistentDataContainer().has(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER);
    }

    public static boolean hasNamespace(ItemStack itemStack, NamespacedKey namespacedKey) {
        if (itemStack == null) {
            return false;
        }
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        return itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.INTEGER);
    }

    public static boolean hasNamespace(ItemStack itemStack, NamespacedKey namespacedKey, PersistentDataType persistentDataType) {
        if (itemStack == null) {
            return false;
        }
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        return itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, persistentDataType);
    }
}
