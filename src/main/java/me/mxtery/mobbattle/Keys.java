package me.mxtery.mobbattle;

import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public class Keys {
    public static NamespacedKey TEAM_BATTLE_WAND;
    public static NamespacedKey MOB_BATTLE_WAND;
    public static NamespacedKey MOB_MODIFIER;
    public static NamespacedKey MOB_BATTLE_ITEM;
    public static NamespacedKey MOB_VAPORIZER;

    public static void init(MobBattle plugin) {
        MOB_BATTLE_ITEM = new NamespacedKey(plugin, "mob_battle_item");
        TEAM_BATTLE_WAND = new NamespacedKey(plugin, "team_battle_wand");
        MOB_BATTLE_WAND = new NamespacedKey(plugin, "mob_battle_wand");
        MOB_MODIFIER = new NamespacedKey(plugin, "mob_modifier");
        MOB_VAPORIZER = new NamespacedKey(plugin, "mob_vaporizer");
    }

    public static List<NamespacedKey> getKeys() {
        List<NamespacedKey> keys = new ArrayList<>();
        keys.add(TEAM_BATTLE_WAND);
        keys.add(MOB_BATTLE_WAND);
        keys.add(MOB_MODIFIER);
        keys.add(MOB_VAPORIZER);
        return keys;

    }


}
