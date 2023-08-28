package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.ItemManager;
import me.mxtery.mobbattle.Keys;
import me.mxtery.mobbattle.helpers.ConfigManager;
import me.mxtery.mobbattle.helpers.MessageHelper;
import me.mxtery.mobbattle.helpers.PlayerHelper;
import me.mxtery.mobbattle.helpers.SoundHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class MobBattleWandEvents implements Listener {
    private final HashMap<UUID, UUID> playerToEntity1;
    private final HashMap<UUID, UUID> playerToEntity2;
    private final HashMap<UUID, UUID> entity1ToEntity2;
    private final HashMap<UUID, Boolean> playerToInBattle;
    private final TeamBattleWandEvents teamBattleWandEvents;
    private final ItemManager itemManager;

    public MobBattleWandEvents(TeamBattleWandEvents teamBattleWandEvents, ItemManager itemManager) {
        playerToEntity1 = new HashMap<>();
        playerToEntity2 = new HashMap<>();
        entity1ToEntity2 = new HashMap<>();
        playerToInBattle = new HashMap<>();
        this.teamBattleWandEvents = teamBattleWandEvents;
        this.itemManager = itemManager;

    }

    public HashMap<UUID, UUID> getPlayerToEntity1() {
        return playerToEntity1;
    }

    public HashMap<UUID, UUID> getPlayerToEntity2() {
        return playerToEntity2;
    }

    public HashMap<UUID, Boolean> getPlayerToInBattle() {
        return playerToInBattle;
    }

    @EventHandler
    public void onStartEnd(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }

        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }

        event.setCancelled(true);
        if (playerToInBattle.containsKey(player.getUniqueId())) {
            if (playerToInBattle.get(player.getUniqueId())) {

                Mob entity1 = (Mob) Bukkit.getEntity(playerToEntity1.get(player.getUniqueId()));
                Mob entity2 = (Mob) Bukkit.getEntity(playerToEntity2.get(player.getUniqueId()));

                if (entity1 != null) {
                    entity1ToEntity2.remove(entity1.getUniqueId());
                    entity1.setTarget(null);
                }
                if (entity2 != null) {
                    entity2.setTarget(null);
                }
                playerToEntity2.remove(player.getUniqueId());
                playerToEntity1.remove(player.getUniqueId());
                playerToInBattle.put(player.getUniqueId(), false);
                SoundHelper.playLevelUpSound(player);
                MessageHelper.sendPluginMessage(player, ChatColor.GREEN + "Battle ended!");
                return;
            }
        }

        if (playerToEntity1.get(player.getUniqueId()) == null) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have not set entity 1 yet!");
            SoundHelper.playErrorSound(player);
            return;
        }
        if (playerToEntity2.get(player.getUniqueId()) == null) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have not set entity 2 yet!");
            SoundHelper.playErrorSound(player);
            return;
        }


        MessageHelper.sendPluginMessage(player, ChatColor.LIGHT_PURPLE + "Let the fight commence!");
        SoundHelper.playDragonSound(player);
        Mob mob2 = (Mob) Bukkit.getEntity(playerToEntity2.get(player.getUniqueId()));
        Mob mob1 = (Mob) Bukkit.getEntity(playerToEntity1.get(player.getUniqueId()));
        if (mob2 == null || mob2.isDead()) {
            if (mob2 != null) {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + mob2.getName() + "&e has died!");
                SoundHelper.playLevelUpSound(player);
            } else {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + "Entity 2 has died!");
                SoundHelper.playLevelUpSound(player);
            }
            playerToEntity2.remove(player.getUniqueId());
            playerToInBattle.put(player.getUniqueId(), false);
        }
        if (mob1 == null || mob1.isDead()) {
            if (mob1 != null) {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + mob1.getName() + "&e has died!");
                SoundHelper.playLevelUpSound(player);
            } else {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + "Entity 1 has died!");
                SoundHelper.playLevelUpSound(player);
            }
            playerToEntity1.remove(player.getUniqueId());
            playerToInBattle.put(player.getUniqueId(), false);
        }
        try {
            mob2.setTarget(mob1);
            mob1.setTarget(mob2);
            entity1ToEntity2.put(mob1.getUniqueId(), mob2.getUniqueId());
            playerToInBattle.put(player.getUniqueId(), true);

        } catch (Exception ignored) {

        }
    }


    public void endBattle(Mob mob1, Mob mob2, Player player) {


        if (mob1 == null || mob1.isDead()) {
            if (mob1 != null) {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + mob1.getName() + "&e has died!");
                SoundHelper.playLevelUpSound(player);
            } else {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + "Entity 1 has died!");
                SoundHelper.playLevelUpSound(player);
            }
            playerToInBattle.put(player.getUniqueId(), false);
            if (playerToEntity1.get(player.getUniqueId()) != null) {
                entity1ToEntity2.remove(playerToEntity1.get(player.getUniqueId()));
            } else if (playerToEntity2.get(player.getUniqueId()) != null) {
                entity1ToEntity2.remove(getKey(entity1ToEntity2, playerToEntity2.get(player.getUniqueId())));
            }
            playerToEntity1.remove(player.getUniqueId());
            playerToEntity2.remove(player.getUniqueId());

            return;
        }
        if (mob2 == null || mob2.isDead()) {
            if (mob2 != null) {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + mob2.getName() + "&e has died!");
                SoundHelper.playLevelUpSound(player);
            } else {
                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + "Entity 2 has died!");
                SoundHelper.playLevelUpSound(player);
            }
            playerToInBattle.put(player.getUniqueId(), false);
            if (playerToEntity1.get(player.getUniqueId()) != null) {
                entity1ToEntity2.remove(playerToEntity1.get(player.getUniqueId()));
            } else if (playerToEntity2.get(player.getUniqueId()) != null) {
                entity1ToEntity2.remove(getKey(entity1ToEntity2, playerToEntity2.get(player.getUniqueId())));
            }

            playerToEntity2.remove(player.getUniqueId());
            playerToEntity1.remove(player.getUniqueId());

        }


    }


    @EventHandler
    public void onReset(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }
        event.setCancelled(true);
        if (playerToInBattle.containsKey(player.getUniqueId())) {
            if (playerToInBattle.get(player.getUniqueId())) {
                MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already started a fight.");
                SoundHelper.playErrorSound(player);
                return;

            }
        }
        playerToEntity1.remove(player.getUniqueId());
        playerToEntity2.remove(player.getUniqueId());
        MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + "Selections reset!");
        SoundHelper.playDingSound(player);


    }


    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {

        Player player = event.getPlayer();
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        event.setCancelled(true);


        if (playerToInBattle.containsKey(player.getUniqueId())) {
            if (playerToInBattle.get(player.getUniqueId())) {
                MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already started a fight.");
                SoundHelper.playErrorSound(player);
                return;
            }
        }

        if (playerToEntity1.containsKey(player.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already selected entity 1. " + ChatColor.YELLOW + ChatColor.BOLD + "SHIFT + RIGHT CLICK " + ChatColor.RED + "to reset teams!");
            SoundHelper.playErrorSound(player);

            return;
        }


        Entity entity1 = event.getRightClicked();
        if (!(entity1 instanceof Mob)) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "Invalid entity! The entity has to be a mob. (zombie, skeleton, pillager, iron golem, etc.)");
            SoundHelper.playErrorSound(player);

            return;
        }
        Mob mob1 = (Mob) entity1;
        if (entity1ToEntity2.containsValue(entity1.getUniqueId()) || entity1ToEntity2.containsKey(entity1.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "This mob is already in a fight!");
            SoundHelper.playErrorSound(player);
            return;
        }

        for (UUID uuid : playerToEntity2.values()) {
            if (uuid.equals(mob1.getUniqueId())) {
                if (getKey(playerToEntity2, uuid) != null && getKey(playerToEntity2, uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already selected this mob!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
                SoundHelper.playErrorSound(player);
                MessageHelper.sendPluginMessage(player, ChatColor.RED + "Another player has already selected this mob!");
                return;
            }
        }


        for (UUID uuid : playerToEntity1.values()) {
            if (uuid.equals(mob1.getUniqueId())) {
                if (getKey(playerToEntity1, uuid) != null && getKey(playerToEntity1, uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already selected this mob!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
                SoundHelper.playErrorSound(player);
                MessageHelper.sendPluginMessage(player, ChatColor.RED + "Another player has already selected this mob!");
                return;
            }
        }
        for (HashSet<UUID> hashSet : teamBattleWandEvents.getPlayerToBlueTeam().values()) {
            if (hashSet.contains(mob1.getUniqueId())) {
                if (getKey(teamBattleWandEvents.getPlayerToBlueTeam(), hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob1.getName() + "&c on your " + itemManager.getTeamBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob1.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }
        for (HashSet<UUID> hashSet : teamBattleWandEvents.getPlayerToRedTeam().values()) {
            if (hashSet.contains(mob1.getUniqueId())) {
                if (getKey(teamBattleWandEvents.getPlayerToRedTeam(), hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob1.getName() + "&c on your " + itemManager.getTeamBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob1.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }


        playerToEntity1.put(player.getUniqueId(), mob1.getUniqueId());
        MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + mob1.getName() + "&e has been set as entity 1!");
        SoundHelper.playDingSound(player);
        if (playerToEntity2.containsKey(player.getUniqueId())) {
            MessageHelper.sendNonEssentialPluginMessage(player, ChatColor.GREEN + "Entity 1 and 2 selected! Press " + ChatColor.YELLOW + ChatColor.BOLD + "SHIFT + LEFT CLICK " + ChatColor.GREEN + "to start the battle!");
        } else {
            MessageHelper.sendNonEssentialPluginMessage(player, ChatColor.YELLOW + "Right click a mob to select its target!");
        }
    }

    @EventHandler
    public void onLeftClick(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }
        event.setCancelled(true);
        if (playerToInBattle.containsKey(player.getUniqueId())) {
            if (playerToInBattle.get(player.getUniqueId())) {
                MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already started a fight.");
                SoundHelper.playErrorSound(player);

                return;
            }
        }

        if (playerToEntity2.containsKey(player.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already selected entity 2. " + ChatColor.YELLOW + ChatColor.BOLD + "SHIFT + RIGHT CLICK " + ChatColor.RED + "to reset teams!");
            SoundHelper.playErrorSound(player);

            return;
        }


        Entity entity2 = event.getEntity();
        if (!(entity2 instanceof Mob)) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "Invalid entity! The entity has to be a mob. (zombie, skeleton, pillager, iron golem, etc.)");
            SoundHelper.playErrorSound(player);
            return;
        }
        Mob mob2 = (Mob) entity2;
        if (entity1ToEntity2.containsValue(entity2.getUniqueId()) || entity1ToEntity2.containsKey(entity2.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "This mob is already in a fight!");
            SoundHelper.playErrorSound(player);
            return;
        }


        for (UUID uuid : playerToEntity1.values()) {
            if (uuid.equals(mob2.getUniqueId())) {
                if (getKey(playerToEntity1, uuid) != null && getKey(playerToEntity1, uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already selected this mob!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
                SoundHelper.playErrorSound(player);
                MessageHelper.sendPluginMessage(player, ChatColor.RED + "Another player has already selected this mob!");
                return;
            }
        }
        for (UUID uuid : playerToEntity2.values()) {
            if (uuid.equals(mob2.getUniqueId())) {
                if (getKey(playerToEntity2, uuid) != null && getKey(playerToEntity2, uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already selected this mob!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
                SoundHelper.playErrorSound(player);
                MessageHelper.sendPluginMessage(player, ChatColor.RED + "Another player has already selected this mob!");
                return;
            }
        }
        for (HashSet<UUID> hashSet : teamBattleWandEvents.getPlayerToBlueTeam().values()) {
            if (hashSet.contains(mob2.getUniqueId())) {
                if (getKey(teamBattleWandEvents.getPlayerToBlueTeam(), hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob2.getName() + "&c on your " + itemManager.getTeamBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob2.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }
        for (HashSet<UUID> hashSet : teamBattleWandEvents.getPlayerToRedTeam().values()) {
            if (hashSet.contains(mob2.getUniqueId())) {
                if (getKey(teamBattleWandEvents.getPlayerToRedTeam(), hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob2.getName() + "&c on your " + itemManager.getTeamBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob2.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }
        playerToEntity2.put(player.getUniqueId(), mob2.getUniqueId());
        MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + mob2.getName() + "&e has been set as entity 2!");
        SoundHelper.playDingSound(player);
        if (playerToEntity1.containsKey(player.getUniqueId())) {
            MessageHelper.sendNonEssentialPluginMessage(player, ChatColor.GREEN + "Entity 1 and 2 selected! Press " + ChatColor.YELLOW + ChatColor.BOLD + "SHIFT + LEFT CLICK " + ChatColor.GREEN + "to start the battle!");
        } else {
            MessageHelper.sendNonEssentialPluginMessage(player, ChatColor.YELLOW + "Right click a mob to select its target!");
        }
    }

    @EventHandler
    public void onMobChangeTarget(EntityTargetEvent event) {
        if (entity1ToEntity2.containsKey(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
            Mob target = (Mob) Bukkit.getEntity(entity1ToEntity2.get(event.getEntity().getUniqueId()));
            if (target == null || target.isDead()) {
                if ((getKey(playerToEntity1, event.getEntity().getUniqueId()) == null)) {
                    return;
                }
                Player player = Bukkit.getPlayer(getKey(playerToEntity1, event.getEntity().getUniqueId()));
                if (player == null) {
                    return;
                }

                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + event.getEntity().getName() + "&e has died!");
                SoundHelper.playLevelUpSound(player);
                if (playerToEntity1.get(player.getUniqueId()) != null) {
                    entity1ToEntity2.remove(playerToEntity1.get(player.getUniqueId()));
                } else if (playerToEntity2.get(player.getUniqueId()) != null) {
                    entity1ToEntity2.remove(getKey(entity1ToEntity2, playerToEntity2.get(player.getUniqueId())));
                }

                playerToEntity2.remove(player.getUniqueId());
                playerToEntity1.remove(player.getUniqueId());
                playerToInBattle.put(player.getUniqueId(), false);
                Mob entity = (Mob) event.getEntity();
                entity.setTarget(null);
            }

        }
        if (entity1ToEntity2.containsValue(event.getEntity().getUniqueId())) {
            event.setCancelled(true);

            Mob target = (Mob) Bukkit.getEntity(getKey(entity1ToEntity2, event.getEntity().getUniqueId()));
            if (target == null || target.isDead()) {
                Player player = Bukkit.getPlayer(getKey(playerToEntity2, event.getEntity().getUniqueId()));
                if (player == null) {
                    return;
                }

                MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + event.getEntity().getName() + "&e has died!");
                SoundHelper.playLevelUpSound(player);
                if (playerToEntity1.get(player.getUniqueId()) != null) {
                    entity1ToEntity2.remove(playerToEntity1.get(player.getUniqueId()));
                } else if (playerToEntity2.get(player.getUniqueId()) != null) {
                    entity1ToEntity2.remove(getKey(entity1ToEntity2, playerToEntity2.get(player.getUniqueId())));
                }

                playerToEntity2.remove(player.getUniqueId());
                playerToEntity1.remove(player.getUniqueId());
                playerToInBattle.put(player.getUniqueId(), false);
            }

        }
        if (ConfigManager.getTrue1v1()) {
            if (event.getTarget() != null) {
                if (entity1ToEntity2.containsValue(event.getTarget().getUniqueId()) || entity1ToEntity2.containsKey(event.getTarget().getUniqueId())) {
                    event.setCancelled(true);
                }
            }

        }

    }


    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (playerToEntity2.containsValue(entity.getUniqueId())) {

            Player player = Bukkit.getPlayer(getKey(playerToEntity2, entity.getUniqueId()));
            if (player == null) {
                return;
            }

            MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + entity.getName() + "&e has died!");

            if (getKey(entity1ToEntity2, event.getEntity().getUniqueId()) != null) {
                Mob entity2 = (Mob) Bukkit.getEntity(getKey(entity1ToEntity2, event.getEntity().getUniqueId()));
                if (entity2 != null) {
                    entity2.setTarget(null);
                }
            }


            SoundHelper.playLevelUpSound(player);
            playerToEntity2.remove(player.getUniqueId());
            playerToEntity1.remove(player.getUniqueId());
            playerToInBattle.put(player.getUniqueId(), false);
            entity1ToEntity2.remove(getKey(entity1ToEntity2, event.getEntity().getUniqueId()));
        }

        if (playerToEntity1.containsValue(entity.getUniqueId())) {

            Player player = Bukkit.getPlayer(getKey(playerToEntity1, entity.getUniqueId()));
            if (player == null) {
                return;
            }

            MessageHelper.sendPluginMessage(player, ChatColor.YELLOW + entity.getName() + "&e has died!");
            if (entity1ToEntity2.get(entity.getUniqueId()) != null) {
                Mob entity2 = (Mob) Bukkit.getEntity(entity1ToEntity2.get(entity.getUniqueId()));
                if (entity2 != null) {
                    entity2.setTarget(null);
                }
            }

            SoundHelper.playLevelUpSound(player);
            playerToEntity1.remove(player.getUniqueId());
            playerToEntity2.remove(player.getUniqueId());
            entity1ToEntity2.remove(event.getEntity().getUniqueId());
            playerToInBattle.put(player.getUniqueId(), false);

        }
    }

    @EventHandler
    public void onMobTransform(EntityTransformEvent event) {
        Entity entity = event.getEntity();
        Entity transformedEntity = event.getTransformedEntity();
        if (!(entity instanceof Mob)) {
            return;
        }
        if (!(transformedEntity instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) entity;
        Mob transformedMob = (Mob) transformedEntity;
        if (entity1ToEntity2.containsKey(mob.getUniqueId())) {
            if (entity1ToEntity2.get(mob.getUniqueId()) == null) {
                return;
            }
            Mob entity2 = (Mob) Bukkit.getEntity(entity1ToEntity2.get(mob.getUniqueId()));
            if (entity2 == null) {
                return;
            }

            entity1ToEntity2.remove(mob.getUniqueId());
            if (getKey(playerToEntity1, mob.getUniqueId()) == null) {
                return;
            }
            Player player = Bukkit.getPlayer(getKey(playerToEntity1, mob.getUniqueId()));
            if (player == null) {
                return;
            }
            playerToEntity1.remove(mob.getUniqueId());
            entity1ToEntity2.put(transformedMob.getUniqueId(), entity2.getUniqueId());
            playerToEntity1.put(player.getUniqueId(), transformedEntity.getUniqueId());
            try {
                transformedMob.setTarget(entity2);
                entity2.setTarget(transformedMob);
            } catch (Exception ignored) {
            }


        } else if (entity1ToEntity2.containsValue(mob.getUniqueId())) {
            if (getKey(entity1ToEntity2, mob.getUniqueId()) == null) {
                return;
            }
            Mob entity1 = (Mob) Bukkit.getEntity(getKey(entity1ToEntity2, mob.getUniqueId()));
            if (entity1 == null) {
                return;
            }
            entity1ToEntity2.remove(entity1.getUniqueId());
            if (getKey(playerToEntity2, mob.getUniqueId()) == null) {
                return;
            }

            Player player = Bukkit.getPlayer(getKey(playerToEntity2, mob.getUniqueId()));
            if (player == null) {
                return;
            }
            playerToEntity2.remove(mob.getUniqueId());
            entity1ToEntity2.put(entity1.getUniqueId(), transformedEntity.getUniqueId());
            playerToEntity2.put(player.getUniqueId(), transformedEntity.getUniqueId());
            try {
                transformedMob.setTarget(entity1);
                entity1.setTarget(transformedMob);
            } catch (Exception ignored) {
            }

        }


    }

    @EventHandler
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (playerToEntity1.containsKey(player.getUniqueId())) {
            playerToEntity1.remove(player.getUniqueId());
            MessageHelper.sendPluginMessage(player, "&cYour &eentity1&c selection was reset because you switched worlds!");
        }
        if (playerToEntity2.containsKey(player.getUniqueId())) {
            playerToEntity2.remove(player.getUniqueId());
            MessageHelper.sendPluginMessage(player, "&cYour &eentity2&c selection was reset because you switched worlds!");
        }
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
