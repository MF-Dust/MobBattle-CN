package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.ItemManager;
import me.mxtery.mobbattle.Keys;
import me.mxtery.mobbattle.MobBattle;
import me.mxtery.mobbattle.enums.TeamBattleOutcomes;
import me.mxtery.mobbattle.helpers.*;
import me.mxtery.mobbattle.instance.TeamBattleInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class TeamBattleWandEvents implements Listener {
    private final HashMap<UUID, HashSet<UUID>> playerToRedTeam;
    private final HashMap<UUID, HashSet<UUID>> playerToBlueTeam;
    private final HashSet<UUID> playerDropItemonTick;
    private final ItemManager itemManager;
    private final MobBattle plugin;
    private final HashSet<UUID> colorChange;
    private final HashSet<UUID> addName;
    private final HashSet<UUID> nameVisibleChange;
    private HashMap<UUID, TeamBattleInstance> playerToBattle;
    private HashSet<UUID> mobsInBattle;
    private MobBattleWandEvents mobBattleWandEvents;

    public TeamBattleWandEvents(MobBattle plugin, ItemManager itemManager) {
        playerToRedTeam = new HashMap<>();
        playerToBlueTeam = new HashMap<>();
        colorChange = new HashSet<>();
        nameVisibleChange = new HashSet<>();
        playerDropItemonTick = new HashSet<>();
        addName = new HashSet<>();
        playerToBattle = new HashMap<>();
        mobsInBattle = new HashSet<>();
        this.itemManager = itemManager;
        this.plugin = plugin;
    }

    public HashMap<UUID, HashSet<UUID>> getPlayerToRedTeam() {
        return playerToRedTeam;
    }

    public HashMap<UUID, HashSet<UUID>> getPlayerToBlueTeam() {
        return playerToBlueTeam;
    }

    public void setBattleWandEvents(MobBattleWandEvents mobBattleWandEvents) {
        this.mobBattleWandEvents = mobBattleWandEvents;
    }

    @EventHandler
    public void onPlayerNameMob(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) event.getRightClicked();
        UUID uuid = mob.getUniqueId();
        if (event.getHand() == EquipmentSlot.HAND) {
            if (player.getInventory().getItem(EquipmentSlot.HAND).getType() == Material.NAME_TAG) {
                nameVisibleChange.remove(uuid);
                colorChange.remove(uuid);
                addName.remove(uuid);
            }

        } else if (event.getHand() == EquipmentSlot.OFF_HAND) {
            if (player.getInventory().getItem(EquipmentSlot.OFF_HAND).getType() == Material.NAME_TAG) {
                nameVisibleChange.remove(uuid);
                colorChange.remove(uuid);
                addName.remove(uuid);
            }
        }
    }

    public HashSet<UUID> getMobsInBattle() {
        return mobsInBattle;
    }

    public void setMobsInBattle(HashSet<UUID> mobInBattle) {
        this.mobsInBattle = mobInBattle;
    }

    @EventHandler
    public void onRedTeamSelect(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!PlayerHelper.playerHoldingItem(player, Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }
        event.setCancelled(true);
        if (player.isSneaking()) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof Mob)) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "Invalid entity! The entity has to be a mob. (zombie, skeleton, pillager, iron golem, etc.)");
            SoundHelper.playErrorSound(player);
            return;
        }
        if (mobsInBattle.contains(entity.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "This mob is already in a battle!");
            SoundHelper.playErrorSound(player);
            return;
        }
        if (playerToBattle.containsKey(player.getUniqueId()) && playerToBattle.get(player.getUniqueId()) != null) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already started a battle!");
            SoundHelper.playErrorSound(player);
            return;
        }


        Mob mob = (Mob) entity;
        playerToRedTeam.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        playerToBlueTeam.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());

        HashSet<UUID> blueTeam = playerToBlueTeam.get(player.getUniqueId());


        if (blueTeam.contains(mob.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, "&c" + mob.getName() + "&c is already on the " + "&9blue " + "&cteam!");
            SoundHelper.playErrorSound(player);
            return;
        }
        HashSet<UUID> redTeam = playerToRedTeam.get(player.getUniqueId());
        for (HashSet<UUID> hashSet : playerToRedTeam.values()) {
            if (hashSet.contains(mob.getUniqueId())) {
                if (!getKey(playerToRedTeam, hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
            }
        }

        for (UUID uuid : mobBattleWandEvents.getPlayerToEntity2().values()) {
            if (uuid.equals(mob.getUniqueId())) {
                if (getKey(mobBattleWandEvents.getPlayerToEntity2(), uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob.getName() + "&c on your " + itemManager.getMobBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }
        for (UUID uuid : mobBattleWandEvents.getPlayerToEntity1().values()) {
            if (uuid.equals(mob.getUniqueId())) {
                if (getKey(mobBattleWandEvents.getPlayerToEntity1(), uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob.getName() + "&c on your " + itemManager.getMobBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }

        for (HashSet<UUID> hashSet : playerToBlueTeam.values()) {
            if (hashSet.contains(mob.getUniqueId())) {
                if (!getKey(playerToBlueTeam, hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
            }
        }


        if (redTeam.contains(mob.getUniqueId())) {
            redTeam.remove(mob.getUniqueId());
            playerToRedTeam.put(player.getUniqueId(), redTeam);
            SoundHelper.playDingSound(player);
            MessageHelper.sendPluginMessage(player, "&aRemoved " + mob.getName() + "&a from the " + "&cred " + "&ateam!");
            if (colorChange.contains(mob.getUniqueId())) {
                if (mob.getCustomName() != null) {
                    mob.setCustomName(ChatColor.stripColor(mob.getCustomName()));
                } else {
                    mob.setCustomName(ChatColor.stripColor(mob.getName()));
                }
                colorChange.remove(mob.getUniqueId());
            }
            if (nameVisibleChange.contains(mob.getUniqueId())) {
                mob.setCustomNameVisible(false);
                nameVisibleChange.remove(mob.getUniqueId());
            }
            if (addName.contains(mob.getUniqueId())) {
                mob.setCustomName(null);
                addName.remove(mob.getUniqueId());
            }
        } else {
            redTeam.add(mob.getUniqueId());
            playerToRedTeam.put(player.getUniqueId(), redTeam);
            SoundHelper.playDingSound(player);
            MessageHelper.sendPluginMessage(player, "&aAdded " + mob.getName() + "&a to the " + "&cred " + "&ateam!");
            if (!mob.isCustomNameVisible()) {
                mob.setCustomNameVisible(true);
                nameVisibleChange.add(mob.getUniqueId());
            }
            if (mob.getCustomName() == null) {
                addName.add(mob.getUniqueId());
                if (!mob.getName().contains("\u00A7")) {
                    mob.setCustomName(ChatColor.RED + mob.getName());
                    colorChange.add(mob.getUniqueId());
                }
            } else {
                if (!mob.getCustomName().contains("\u00A7")) {
                    mob.setCustomName(ChatColor.RED + mob.getCustomName());
                    colorChange.add(mob.getUniqueId());
                }
            }
        }

    }


    @EventHandler
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (playerToBlueTeam.containsKey(player.getUniqueId())) {
            HashSet<UUID> blueTeam = playerToBlueTeam.get(player.getUniqueId());

            if (!blueTeam.isEmpty()) {
                for (UUID uuid : playerToBlueTeam.get(player.getUniqueId())) {
                    if (uuid == null) {
                        continue;
                    }
                    Entity mob = Bukkit.getEntity(uuid);
                    if (mob == null) {
                        continue;
                    }
                    if (colorChange.contains(mob.getUniqueId())) {
                        if (mob.getCustomName() != null) {
                            mob.setCustomName(ChatColor.stripColor(mob.getCustomName()));
                        } else {
                            mob.setCustomName(ChatColor.stripColor(mob.getName()));
                        }
                        colorChange.remove(mob.getUniqueId());
                    }
                    if (nameVisibleChange.contains(mob.getUniqueId())) {
                        mob.setCustomNameVisible(false);
                        nameVisibleChange.remove(mob.getUniqueId());
                    }
                    if (addName.contains(mob.getUniqueId())) {
                        mob.setCustomName(null);
                        addName.remove(mob.getUniqueId());
                    }
                }
                playerToBlueTeam.remove(player.getUniqueId());
                MessageHelper.sendPluginMessage(player, "&cYour &9blue team&c selection was reset because you switched worlds!");
            }
        }
        if (playerToRedTeam.containsKey(player.getUniqueId())) {
            HashSet<UUID> redTeam = playerToRedTeam.get(player.getUniqueId());
            if (!redTeam.isEmpty()) {
                for (UUID uuid : playerToRedTeam.get(player.getUniqueId())) {
                    if (uuid == null) {
                        continue;
                    }
                    Entity mob = Bukkit.getEntity(uuid);
                    if (mob == null) {
                        continue;
                    }
                    if (colorChange.contains(mob.getUniqueId())) {
                        if (mob.getCustomName() != null) {
                            mob.setCustomName(ChatColor.stripColor(mob.getCustomName()));
                        } else {
                            mob.setCustomName(ChatColor.stripColor(mob.getName()));
                        }
                        colorChange.remove(mob.getUniqueId());
                    }
                    if (nameVisibleChange.contains(mob.getUniqueId())) {
                        mob.setCustomNameVisible(false);
                        nameVisibleChange.remove(mob.getUniqueId());
                    }
                    if (addName.contains(mob.getUniqueId())) {
                        mob.setCustomName(null);
                        addName.remove(mob.getUniqueId());
                    }
                }
                playerToRedTeam.remove(player.getUniqueId());
                MessageHelper.sendPluginMessage(player, "&cYour &4red&c team selection was reset because you switched worlds!");
            }
        }
    }


    @EventHandler
    public void onBlueTeamSelect(PlayerInteractEntityEvent event) {

        Player player = event.getPlayer();
        if (!PlayerHelper.playerHoldingItem(player, Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }


        event.setCancelled(true);
        if (player.isSneaking()) {
            return;
        }
        Entity entity = event.getRightClicked();
        if (!(entity instanceof Mob)) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "Invalid entity! The entity has to be a mob. (zombie, skeleton, pillager, iron golem, etc.)");
            SoundHelper.playErrorSound(player);
            return;
        }
        if (mobsInBattle.contains(entity.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "This mob is already in a battle!");
            SoundHelper.playErrorSound(player);
            return;
        }
        if (playerToBattle.containsKey(player.getUniqueId()) && playerToBattle.get(player.getUniqueId()) != null) {
            MessageHelper.sendPluginMessage(player, ChatColor.RED + "You have already started a battle!");
            SoundHelper.playErrorSound(player);
            return;
        }
        Mob mob = (Mob) entity;
        playerToRedTeam.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        playerToBlueTeam.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());


        HashSet<UUID> blueTeam = playerToBlueTeam.get(player.getUniqueId());
        HashSet<UUID> redTeam = playerToRedTeam.get(player.getUniqueId());

        if (redTeam.contains(mob.getUniqueId())) {
            MessageHelper.sendPluginMessage(player, "&c" + mob.getName() + "&c is already on the " + "&cred " + "&cteam!");
            SoundHelper.playErrorSound(player);
            return;
        }

        for (HashSet<UUID> hashSet : playerToRedTeam.values()) {
            if (hashSet.contains(mob.getUniqueId())) {
                if (!getKey(playerToRedTeam, hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
            }
        }

        for (HashSet<UUID> hashSet : playerToBlueTeam.values()) {
            if (hashSet.contains(mob.getUniqueId())) {
                if (!getKey(playerToBlueTeam, hashSet).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                    SoundHelper.playErrorSound(player);
                    return;
                }
            }
        }
        for (UUID uuid : mobBattleWandEvents.getPlayerToEntity2().values()) {
            if (uuid.equals(mob.getUniqueId())) {
                if (getKey(mobBattleWandEvents.getPlayerToEntity2(), uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob.getName() + "&c on your " + itemManager.getMobBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }
        for (UUID uuid : mobBattleWandEvents.getPlayerToEntity1().values()) {
            if (uuid.equals(mob.getUniqueId())) {
                if (getKey(mobBattleWandEvents.getPlayerToEntity1(), uuid).equals(player.getUniqueId())) {
                    MessageHelper.sendPluginMessage(player, "&cYou have already selected " + mob.getName() + "&c on your " + itemManager.getMobBattleWand().getItemMeta().getDisplayName() + "&c!");
                } else {
                    MessageHelper.sendPluginMessage(player, mob.getName() + "&c has already been selected by another player!");
                }
                SoundHelper.playErrorSound(player);
                return;
            }
        }


        if (blueTeam.contains(mob.getUniqueId())) {
            blueTeam.remove(mob.getUniqueId());
            playerToBlueTeam.put(player.getUniqueId(), blueTeam);
            SoundHelper.playDingSound(player);
            MessageHelper.sendPluginMessage(player, "&aRemoved " + mob.getName() + "&a from the " + "&9blue " + "&ateam!");
            if (colorChange.contains(mob.getUniqueId())) {
                if (mob.getCustomName() != null) {
                    mob.setCustomName(ChatColor.stripColor(mob.getCustomName()));
                } else {
                    mob.setCustomName(ChatColor.stripColor(mob.getName()));
                }
                colorChange.remove(mob.getUniqueId());
            }
            if (nameVisibleChange.contains(mob.getUniqueId())) {
                mob.setCustomNameVisible(false);
                nameVisibleChange.remove(mob.getUniqueId());
            }
            if (addName.contains(mob.getUniqueId())) {
                mob.setCustomName(null);
                addName.remove(mob.getUniqueId());
            }
        } else {
            blueTeam.add(mob.getUniqueId());
            playerToBlueTeam.put(player.getUniqueId(), blueTeam);
            SoundHelper.playDingSound(player);
            MessageHelper.sendPluginMessage(player, "&aAdded " + mob.getName() + "&a to the " + "&9blue " + "&ateam!");
            if (!mob.isCustomNameVisible()) {
                mob.setCustomNameVisible(true);
                nameVisibleChange.add(mob.getUniqueId());
            }
            if (mob.getCustomName() == null) {
                addName.add(mob.getUniqueId());
                if (!mob.getName().contains("\u00A7")) {
                    mob.setCustomName(ChatColor.BLUE + mob.getName());
                    colorChange.add(mob.getUniqueId());
                }
            } else {
                if (!mob.getCustomName().contains("\u00A7")) {
                    mob.setCustomName(ChatColor.BLUE + mob.getCustomName());
                    colorChange.add(mob.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        playerDropItemonTick.add(event.getPlayer().getUniqueId());
        RunnableHelper.runTaskLater(() -> playerDropItemonTick.remove(event.getPlayer().getUniqueId()), 1);

    }

    @EventHandler
    public void onRedTeamReset(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!PlayerHelper.playerHoldingItem(player, Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (playerDropItemonTick.contains(player.getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        if (!player.isSneaking()) {
            return;
        }
        if (playerToBattle.containsKey(player.getUniqueId())){
            MessageHelper.sendPluginMessage(player, "&cYou have already started a battle!");
            return;
        }

        playerToRedTeam.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());


        HashSet<UUID> redTeam = playerToRedTeam.get(player.getUniqueId());

        SoundHelper.playDingSound(player);
        for (UUID uuid : redTeam) {
            if (Bukkit.getEntity(uuid) == null || Bukkit.getEntity(uuid).isDead()) {
                continue;
            }
            Mob mob = (Mob) Bukkit.getEntity(uuid);
            if (colorChange.contains(mob.getUniqueId())) {
                if (mob.getCustomName() != null) {
                    mob.setCustomName(ChatColor.stripColor(mob.getCustomName()));
                } else {
                    mob.setCustomName(ChatColor.stripColor(mob.getName()));
                }
                colorChange.remove(mob.getUniqueId());
            }
            if (nameVisibleChange.contains(mob.getUniqueId())) {
                mob.setCustomNameVisible(false);
                nameVisibleChange.remove(mob.getUniqueId());
            }
            if (addName.contains(mob.getUniqueId())) {
                mob.setCustomName(null);
                addName.remove(mob.getUniqueId());
            }
        }
        redTeam.clear();
        playerToRedTeam.put(player.getUniqueId(), redTeam);
        MessageHelper.sendPluginMessage(player, "&aReset the &cred &ateam!");

    }

    @EventHandler
    public void onBlueTeamReset(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!PlayerHelper.playerHoldingItem(player, Keys.TEAM_BATTLE_WAND, PersistentDataType.INTEGER)) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        event.setCancelled(true);
        if (!player.isSneaking()) {
            return;
        }
        if (playerToBattle.containsKey(player.getUniqueId())){
            MessageHelper.sendPluginMessage(player, "&cYou have already started a battle!");
            return;
        }
        playerToRedTeam.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        playerToBlueTeam.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());


        HashSet<UUID> blueTeam = playerToBlueTeam.get(player.getUniqueId());

        SoundHelper.playDingSound(player);
        for (UUID uuid : blueTeam) {
            if (Bukkit.getEntity(uuid) == null || Bukkit.getEntity(uuid).isDead()) {
                continue;
            }
            Mob mob = (Mob) Bukkit.getEntity(uuid);
            if (colorChange.contains(mob.getUniqueId())) {
                if (mob.getCustomName() != null) {
                    mob.setCustomName(ChatColor.stripColor(mob.getCustomName()));
                } else {
                    mob.setCustomName(ChatColor.stripColor(mob.getName()));
                }
                colorChange.remove(mob.getUniqueId());
            }
            if (nameVisibleChange.contains(mob.getUniqueId())) {
                mob.setCustomNameVisible(false);
                nameVisibleChange.remove(mob.getUniqueId());
            }
            if (addName.contains(mob.getUniqueId())) {
                mob.setCustomName(null);
                addName.remove(mob.getUniqueId());
            }
        }


        blueTeam.clear();
        playerToBlueTeam.put(player.getUniqueId(), blueTeam);
        MessageHelper.sendPluginMessage(player, "&aReset the &9blue &ateam!");

    }

    public HashMap<UUID, TeamBattleInstance> getPlayerToBattle() {
        return playerToBattle;
    }

    public void setPlayerToBattle(HashMap<UUID, TeamBattleInstance> playerToBattle) {
        this.playerToBattle = playerToBattle;
    }


    private void endBattle(Player player) {
        TeamBattleInstance teamBattleInstance = playerToBattle.get(player.getUniqueId());
        playerToRedTeam.remove(player.getUniqueId());
        playerToBlueTeam.remove(player.getUniqueId());

        if (teamBattleInstance != null) {
            playerToRedTeam.put(player.getUniqueId(), teamBattleInstance.getRedTeam());
            playerToBlueTeam.put(player.getUniqueId(), teamBattleInstance.getBlueTeam());
            teamBattleInstance.end(TeamBattleOutcomes.END_BY_PLAYER);
        }

    }

    private void startBattle(Player player) {
        if ((playerToRedTeam.get(player.getUniqueId()) == null || playerToRedTeam.get(player.getUniqueId()).isEmpty()) && (playerToBlueTeam.get(player.getUniqueId()) == null || playerToBlueTeam.get(player.getUniqueId()).isEmpty())) {
            MessageHelper.sendPluginMessage(player, "&cYou have not selected a &4red &cor &9blue &cteam yet!");
            SoundHelper.playErrorSound(player);
            return;
        }
        if (playerToRedTeam.get(player.getUniqueId()) == null || playerToRedTeam.get(player.getUniqueId()).isEmpty()) {
            MessageHelper.sendPluginMessage(player, "&cYou have not selected a &4red &cteam yet!");
            SoundHelper.playErrorSound(player);
            return;
        }
        if (playerToBlueTeam.get(player.getUniqueId()) == null || playerToBlueTeam.get(player.getUniqueId()).isEmpty()) {
            MessageHelper.sendPluginMessage(player, "&cYou have not selected a &9blue &cteam yet!");
            SoundHelper.playErrorSound(player);
            return;
        }


        TeamBattleInstance teamBattleInstance = new TeamBattleInstance(this, plugin, player, playerToRedTeam.get(player.getUniqueId()), playerToBlueTeam.get(player.getUniqueId()));
        playerToBattle.put(player.getUniqueId(), teamBattleInstance);
        plugin.getServer().getPluginManager().registerEvents(teamBattleInstance, plugin);
        SoundHelper.playDragonSound(player);
        MessageHelper.sendGradientPluginMessage(player, MessageHelper.createGradient("The team battle has begun!", 312, 0, 92));
    }


    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Mob)) {
            return;
        }
        for (HashSet<UUID> hashSet : playerToBlueTeam.values()) {
            if (hashSet.contains(entity.getUniqueId())) {
                Player player = null;
                if (getKey(playerToBlueTeam, hashSet) != null) {
                    player = Bukkit.getPlayer(getKey(playerToBlueTeam, hashSet));

                }
                if (player == null) {
                    return;
                }
                if (!playerToBattle.containsKey(player.getUniqueId()) || playerToBattle.get(player.getUniqueId()) == null) {
                    hashSet.remove(entity.getUniqueId());
                    playerToBlueTeam.put(player.getUniqueId(), hashSet);
                }
                MessageHelper.sendPluginMessage(player, "&e" + entity.getName() + " &ehas died!");
            }
        }
        for (HashSet<UUID> hashSet : playerToRedTeam.values()) {
            if (hashSet.contains(entity.getUniqueId())) {
                Player player = Bukkit.getPlayer(getKey(playerToRedTeam, hashSet));
                if (player == null) {
                    return;
                }
                if (!playerToBattle.containsKey(player.getUniqueId()) || playerToBattle.get(player.getUniqueId()) == null) {
                    hashSet.remove(entity.getUniqueId());
                    playerToRedTeam.put(player.getUniqueId(), hashSet);
                }

                MessageHelper.sendPluginMessage(player, "&e" + entity.getName() + " &ehas died!");
            }
        }
    }

    @EventHandler
    public void onStartEndBattle(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!ItemHelper.hasNamespace(event.getItemDrop().getItemStack(), Keys.TEAM_BATTLE_WAND)) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        event.setCancelled(true);

        if (playerToBattle.get(player.getUniqueId()) == null) {
            startBattle(player);
        } else {
            endBattle(player);
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

        for (HashSet<UUID> blueTeam : playerToBlueTeam.values()) {
            if (blueTeam.contains(mob.getUniqueId())) {
                UUID playerUUID = getKey(playerToBlueTeam, blueTeam);
                Player player;
                if (playerUUID != null) {
                    player = Bukkit.getPlayer(playerUUID);
                } else {
                    continue;
                }
                if (player == null) {
                    continue;
                }
                if (!playerToBattle.containsKey(player.getUniqueId()) || playerToBattle.get(player.getUniqueId()) == null) {
                    blueTeam.remove(mob.getUniqueId());
                    blueTeam.add(transformedEntity.getUniqueId());
                }


                if (nameVisibleChange.contains(mob.getUniqueId())) {
                    nameVisibleChange.add(transformedEntity.getUniqueId());
                    nameVisibleChange.remove(mob.getUniqueId());
                }
                if (addName.contains(mob.getUniqueId())) {
                    addName.add(transformedEntity.getUniqueId());
                    addName.remove(mob.getUniqueId());
                }
                if (colorChange.contains(mob.getUniqueId())) {
                    colorChange.add(transformedEntity.getUniqueId());
                    colorChange.remove(mob.getUniqueId());
                }
                if (!playerToBattle.containsKey(player.getUniqueId()) || playerToBattle.get(player.getUniqueId()) == null) {
                    playerToBlueTeam.put(player.getUniqueId(), blueTeam);
                }


            }
        }

        for (HashSet<UUID> redTeam : playerToRedTeam.values()) {
            if (redTeam.contains(mob.getUniqueId())) {
                UUID playerUUID = getKey(playerToRedTeam, redTeam);
                Player player;
                if (playerUUID != null) {
                    player = Bukkit.getPlayer(playerUUID);
                } else {
                    continue;
                }
                if (player == null) {
                    continue;
                }
                if (!playerToBattle.containsKey(player.getUniqueId()) || playerToBattle.get(player.getUniqueId()) == null) {
                    redTeam.remove(mob.getUniqueId());
                    redTeam.add(transformedEntity.getUniqueId());
                }
                if (nameVisibleChange.contains(mob.getUniqueId())) {
                    nameVisibleChange.add(transformedEntity.getUniqueId());
                    nameVisibleChange.remove(mob.getUniqueId());
                }
                if (addName.contains(mob.getUniqueId())) {
                    addName.add(transformedEntity.getUniqueId());
                    addName.remove(mob.getUniqueId());
                }
                if (colorChange.contains(mob.getUniqueId())) {
                    colorChange.add(transformedEntity.getUniqueId());
                    colorChange.remove(mob.getUniqueId());
                }
                if (!playerToBattle.containsKey(player.getUniqueId()) || playerToBattle.get(player.getUniqueId()) == null) {
                    playerToRedTeam.put(player.getUniqueId(), redTeam);
                }

            }
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
