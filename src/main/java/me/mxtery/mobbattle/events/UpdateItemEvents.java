package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.ItemManager;
import me.mxtery.mobbattle.Keys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class UpdateItemEvents implements Listener {
    private final ItemManager itemManager;

    public UpdateItemEvents(ItemManager itemManager) {
        this.itemManager = itemManager;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateItems(player);
    }


    private void updateItems(Player player) {
        for (ItemStack stack : player.getInventory()) {
            if (stack == null) {
                continue;
            }
            if (!stack.hasItemMeta()) {
                continue;
            }
            if (!stack.getItemMeta().getPersistentDataContainer().has(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER)) {
                continue;
            }
            ItemStack item = null;
            for (NamespacedKey key : Keys.getKeys()) {
                if (stack.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                    for (ItemStack itemStack : itemManager.getItems()) {
                        if (!itemStack.hasItemMeta()) {
                            continue;
                        }
                        if (itemStack.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                            item = itemStack;
                            break;
                        }
                    }
                    if (item == null) {
                        continue;
                    }

                    ItemMeta meta = item.getItemMeta();


                    if (!stack.getItemMeta().equals(meta)) {
                        if (stack.getItemMeta() instanceof Damageable && meta instanceof Damageable) {
                            Damageable stackDamageable = (Damageable) stack.getItemMeta();
                            int durability = stackDamageable.getDamage();
                            Damageable damaged = (Damageable) meta;
                            damaged.setDamage(durability);
                            stack.setItemMeta(damaged);
                        } else {
                            stack.setItemMeta(meta);
                            continue;
                        }
                        ItemMeta meta1 = stack.getItemMeta();
                        stack.setItemMeta(meta1);
                    }

                }
            }

        }
    }

}
