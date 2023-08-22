package me.mxtery.mobbattle;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.K;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManager {
    private MobBattle plugin;
    private ItemStack bossBattleWand;
    private ItemStack mobBattleWand;
    private ItemStack mobParalyzer;
    public ItemManager(MobBattle plugin){
        this.plugin = plugin;
        makeBossBattleWand();
        makeMobBattleWand();
        makeMobParalyzer();
    }

    public ItemStack getBossBattleWand() {
        return bossBattleWand;
    }

    public ItemStack getMobBattleWand() {
        return mobBattleWand;
    }

    public ItemStack getMobParalyzer() {
        return mobParalyzer;
    }

    private void makeBossBattleWand(){
        bossBattleWand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = bossBattleWand.getItemMeta();
        meta.getPersistentDataContainer().set(Keys.BOSSBATTLEWAND, PersistentDataType.INTEGER, 1);
        meta.setDisplayName(ChatColor.AQUA + "Boss Mob Battle Wand");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Team Select &e&lLEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Adds the targeted monster to the team!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Boss Select &e&lRIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Sets the targeted monster as the boss!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Team Reset &e&lSHIFT + LEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Resets the team!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Boss Reset &e&lSHIFT + RIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Resets the boss!"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bossBattleWand.setItemMeta(meta);
        bossBattleWand.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }
    private void makeMobBattleWand(){
        mobBattleWand = new ItemStack(Material.STICK);
        ItemMeta meta = mobBattleWand. getItemMeta();
        meta.getPersistentDataContainer().set(Keys.MOBBATTLEWAND, PersistentDataType.INTEGER, 1);
        meta.setDisplayName(ChatColor.YELLOW + "Mob Battle Wand");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Selection 1 &e&lLEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Sets the target as entity 1!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Selection 2 &e&lRIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Sets the target as entity 2!"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobBattleWand.setItemMeta(meta);
        mobBattleWand.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }

    private void makeMobParalyzer(){
        mobParalyzer = new ItemStack(Material.BONE);
        ItemMeta meta = mobParalyzer.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Mob Paralyzer");
        meta.getPersistentDataContainer().set(Keys.MOBPARALYZER, PersistentDataType.INTEGER, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Paralyze Mob &e&lRIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Paralyzes the mob targeted!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Deselect Mob &e&lLEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Un-paralyzes the mob targeted!"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobParalyzer.setItemMeta(meta);
        mobParalyzer.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }





}
