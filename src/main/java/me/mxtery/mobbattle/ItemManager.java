package me.mxtery.mobbattle;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private MobBattle plugin;
    private ItemStack teamBattleWand;
    private ItemStack mobBattleWand;
    private ItemStack mobParalyzer;
    private ItemStack mobVaporizer;
    public ItemManager(MobBattle plugin){
        this.plugin = plugin;
        makeTeamBattleWand();
        makeMobBattleWand();
        makeMobParalyzer();
        makeMobVaporizer();
    }

    public ItemStack getTeamBattleWand() {
        return teamBattleWand;
    }

    public ItemStack getMobBattleWand() {
        return mobBattleWand;
    }

    public ItemStack getMobParalyzer() {
        return mobParalyzer;
    }

    public ItemStack getMobVaporizer() {
        return mobVaporizer;
    }

    public List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<>();
        items.add(teamBattleWand);
        items.add(mobBattleWand);
        items.add(mobParalyzer);
        items.add(mobVaporizer);
        return items;
    }



    private void makeTeamBattleWand(){
        teamBattleWand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = teamBattleWand.getItemMeta();
        meta.getPersistentDataContainer().set(Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        meta.setDisplayName(ChatColor.AQUA + "Team Battle Wand");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Red Team Select &e&lLEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Adds/removes the targeted monster to/from the red team!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Blue Team Select &e&lRIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Adds/removes the targeted monster to/from the blue team!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Red Team Reset &e&lSHIFT + LEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Resets the red team!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Blue Team Reset &e&lSHIFT + RIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Resets the blue team!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Start Battle &e&lDROP"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Starts the battle!"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        teamBattleWand.setItemMeta(meta);
        teamBattleWand.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }
    private void makeMobBattleWand(){
        mobBattleWand = new ItemStack(Material.STICK);
        ItemMeta meta = mobBattleWand. getItemMeta();
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        meta.setDisplayName(ChatColor.YELLOW + "Mob Battle Wand");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Selection 1 &e&lRIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Sets the target as entity 1!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Selection 2 &e&lLEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Sets the target as entity 2!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Reset &e&lSHIFT + RIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Resets your selections!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Start/End &e&lSHIFT + LEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Start/Ends the battle!"));

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobBattleWand.setItemMeta(meta);
        mobBattleWand.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }

    private void makeMobParalyzer(){
        mobParalyzer = new ItemStack(Material.BONE);
        ItemMeta meta = mobParalyzer.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Mob Modifier");
        meta.getPersistentDataContainer().set(Keys.MOB_MODIFIER, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Paralyze Mob &e&lRIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Toggles the AI of the selected mob!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Mob Invincibility &e&lLEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Toggles the invincibility of the selected mob!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Mob Invisibility &e&lSHIFT + RIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Toggles the invisibility of the selected mob!"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobParalyzer.setItemMeta(meta);
        mobParalyzer.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }
    private void makeMobVaporizer(){
        mobVaporizer = new ItemStack(Material.BAMBOO);
        ItemMeta meta = mobVaporizer.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mob Vaporizer");
        meta.getPersistentDataContainer().set(Keys.MOB_VAPORIZER, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Kill Mob &e&lLEFT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Instantly kills the targeted mob!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Item Ability: Kill Mobs (Area) &e&lRIGHT CLICK"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Instantly kills all mobs in a 5-block radius!"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7(Excludes players)"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobVaporizer.setItemMeta(meta);
        mobVaporizer.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }





}
