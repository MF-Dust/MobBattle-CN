package me.mxtery.mobbattle;

import org.bukkit.plugin.java.JavaPlugin;

public final class MobBattle extends JavaPlugin {
    private ItemManager itemManager;
    @Override
    public void onEnable() {
        Keys.init(this);
        itemManager = new ItemManager(this);

    }
    public ItemManager getItemManager(){
        return itemManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
