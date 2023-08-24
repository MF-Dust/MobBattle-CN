package me.mxtery.mobbattle.helpers;

import me.mxtery.mobbattle.Keys;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemHelper {
    public static boolean isPluginItem(ItemStack itemStack){
        if (itemStack == null){
            return false;
        }
        if (!itemStack.hasItemMeta()){
            return false;
        }
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER)){
            return false;
        }
        return true;
    }
}
