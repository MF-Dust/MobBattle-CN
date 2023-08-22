package me.mxtery.mobbattle;

import org.bukkit.NamespacedKey;

public class Keys {
    public static NamespacedKey BOSSBATTLEWAND;
    public static NamespacedKey MOBBATTLEWAND;
    public static NamespacedKey MOBPARALYZER;
    public static void init(MobBattle plugin){
        BOSSBATTLEWAND = new NamespacedKey(plugin, "bossbattlewand");
        MOBBATTLEWAND = new NamespacedKey(plugin, "mobbattlewand");
        MOBPARALYZER = new NamespacedKey(plugin, "mobparalyzer");
    }



}
