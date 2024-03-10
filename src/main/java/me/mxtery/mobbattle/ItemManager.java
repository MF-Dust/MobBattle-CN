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
    private ItemStack teamBattleWand;
    private ItemStack mobBattleWand;
    private ItemStack mobModifier;
    private ItemStack mobVaporizer;

    public ItemManager() {
        makeTeamBattleWand();
        makeMobBattleWand();
        makeMobModifier();
        makeMobVaporizer();
    }

    public ItemStack getTeamBattleWand() {
        return teamBattleWand;
    }

    public ItemStack getMobBattleWand() {
        return mobBattleWand;
    }


    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(teamBattleWand);
        items.add(mobBattleWand);
        items.add(mobModifier);
        items.add(mobVaporizer);
        return items;
    }


    private void makeTeamBattleWand() {
        teamBattleWand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = teamBattleWand.getItemMeta();
        meta.getPersistentDataContainer().set(Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        meta.setDisplayName(ChatColor.AQUA + "团队战斗杖");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 选择红队 &e&l左键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7将目标怪物添加到/移出红队!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 选择蓝队 &e&l右键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7将目标怪物添加到/移出蓝队!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 重置红队 &e&lShift + 左键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7重置红队!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 重置蓝队 &e&lShift + 右键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7重置蓝队!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 开始/结束战斗 &e&lShift + 丢弃"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7开始/结束战斗!"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        teamBattleWand.setItemMeta(meta);
        teamBattleWand.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }

    private void makeMobBattleWand() {
        mobBattleWand = new ItemStack(Material.STICK);
        ItemMeta meta = mobBattleWand.getItemMeta();
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        meta.setDisplayName(ChatColor.YELLOW + "怪物战斗杖");
        List<String> lore = new ArrayList<>();


        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 选择1 &e&l右键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7将目标设为实体1!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 选择2 &e&l左键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7将目标设为实体2!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 重置 &e&lShift + 右键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7重置你的选择!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 开始/结束 &e&lShift + 左键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7开始/结束战斗!"));

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobBattleWand.setItemMeta(meta);
        mobBattleWand.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }

    private void makeMobModifier() {
        mobModifier = new ItemStack(Material.BONE);
        ItemMeta meta = mobModifier.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "怪物修改杖");
        meta.getPersistentDataContainer().set(Keys.MOB_MODIFIER, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 冻结怪物 &e&l右键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7切换选定怪物的AI!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 怪物无敌 &e&l左键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7切换选定怪物的无敌状态!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 怪物隐身 &e&lShift + 右键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7切换选定怪物的隐身状态!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 治疗怪物 &e&lShift + 左键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7将选定怪物治疗至满血!!"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobModifier.setItemMeta(meta);
        mobModifier.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }

    private void makeMobVaporizer() {
        mobVaporizer = new ItemStack(Material.BAMBOO);
        ItemMeta meta = mobVaporizer.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "怪物清除杖");
        meta.getPersistentDataContainer().set(Keys.MOB_VAPORIZER, PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(Keys.MOB_BATTLE_ITEM, PersistentDataType.INTEGER, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 杀死怪物 &e&l左键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7立即杀死目标怪物!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6物品能力: 杀死怪物（范围） &e&l右键点击"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7立即杀死5格范围内的所有怪物!"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7(不包括玩家)"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        mobVaporizer.setItemMeta(meta);
        mobVaporizer.addUnsafeEnchantment(Enchantment.LUCK, 1);
    }


}