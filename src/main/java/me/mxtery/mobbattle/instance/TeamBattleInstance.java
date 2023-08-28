package me.mxtery.mobbattle.instance;

import me.mxtery.mobbattle.MobBattle;
import me.mxtery.mobbattle.enums.TeamBattleOutcomes;
import me.mxtery.mobbattle.events.TeamBattleWandEvents;
import me.mxtery.mobbattle.helpers.ConfigManager;
import me.mxtery.mobbattle.helpers.MessageHelper;
import me.mxtery.mobbattle.helpers.SoundHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class TeamBattleInstance extends BukkitRunnable implements Listener {
    private final HashSet<UUID> redTeam;
    private final HashSet<UUID> blueTeam;
    private final Player player;
    private final MobBattle plugin;
    private final TeamBattleWandEvents teamBattleWandEvents;

    public TeamBattleInstance(TeamBattleWandEvents teamBattleWandEvents, MobBattle plugin, Player player, HashSet<UUID> redTeam, HashSet<UUID> blueTeam) {
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
        this.player = player;
        this.plugin = plugin;
        this.teamBattleWandEvents = teamBattleWandEvents;
        start();
    }

    public void start() {
        HashSet<UUID> allMobs = new HashSet<>();
        allMobs.addAll(redTeam);
        allMobs.addAll(blueTeam);
        teamBattleWandEvents.setMobsInBattle(allMobs);


        runTaskTimer(plugin, 0, 20);
    }

    private Entity getClosestEntity(Location location, HashSet<UUID> entities) {
        Entity closestEntity = null;
        double shortestDistance = Double.MAX_VALUE;

        for (UUID uuid : entities) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null) {
                return null;
            }
            double distance = location.distance(entity.getLocation());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestEntity = entity;
            }
        }

        return closestEntity;
    }

    private void cleanTeam(HashSet<UUID> team) {
        if (team != null && !team.isEmpty()) {
            Iterator<UUID> iterator = team.iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                Entity entity = Bukkit.getEntity(uuid);
                if (entity == null || entity.isDead() || !(entity instanceof Mob)) {
                    iterator.remove();
                }
            }
        }
    }

    public void end(TeamBattleOutcomes outcome) {
        cancel();
        HashSet<UUID> mobsInBattle = teamBattleWandEvents.getMobsInBattle();
        for (UUID uuid : blueTeam) {
            if (uuid == null) {
                continue;
            }
            Entity entity = Bukkit.getEntity(uuid);
            if (!(entity instanceof Mob)) {
                blueTeam.remove(uuid);
                mobsInBattle.remove(uuid);
                continue;
            }
            Mob mob = (Mob) entity;
            mob.setTarget(null);
            mobsInBattle.remove(uuid);
        }
        for (UUID uuid : redTeam) {
            if (uuid == null) {
                continue;
            }
            Entity entity = Bukkit.getEntity(uuid);
            if (!(entity instanceof Mob)) {
                redTeam.remove(uuid);
                mobsInBattle.remove(uuid);
                continue;
            }
            Mob mob = (Mob) entity;
            mobsInBattle.remove(uuid);
            mob.setTarget(null);
        }
        teamBattleWandEvents.setMobsInBattle(mobsInBattle);

        HashMap<UUID, TeamBattleInstance> playerToBattle = teamBattleWandEvents.getPlayerToBattle();
        playerToBattle.remove(player.getUniqueId());
        teamBattleWandEvents.setPlayerToBattle(playerToBattle);

        HandlerList.unregisterAll(this);


        StringBuilder builder = new StringBuilder();
        builder.append("&eThe battle ended!");

        switch (outcome) {
            case TIE:
                builder.append(" It was a tie!");
                break;
            case RED_TEAM_WIN:
                builder.append(" The &cred &eteam won!");
                break;
            case BLUE_TEAM_WIN:
                builder.append(" The &9blue &eteam won!");
                break;
            case END_BY_PLAYER:
                break;
            default:
                builder.append(" &cSomething went wrong! There was no outcome.");
                break;
        }
        MessageHelper.sendPluginMessage(player, builder.toString());
        SoundHelper.playLevelUpSound(player);


    }

    private TeamBattleOutcomes checkForWin() {
        if (blueTeam.isEmpty() && redTeam.isEmpty()) {
            return TeamBattleOutcomes.TIE;
        } else if (blueTeam.isEmpty()) {
            return TeamBattleOutcomes.RED_TEAM_WIN;
        } else if (redTeam.isEmpty()) {
            return TeamBattleOutcomes.BLUE_TEAM_WIN;
        } else {
            return TeamBattleOutcomes.NO_OUTCOME;
        }
    }

    @EventHandler
    public void onMobDie(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Mob))
            return;

        Mob deadMob = (Mob) event.getEntity();
        if (!redTeam.contains(deadMob.getUniqueId()) && !blueTeam.contains(deadMob.getUniqueId())) {
            return;
        }


        redTeam.remove(deadMob.getUniqueId());
        blueTeam.remove(deadMob.getUniqueId());
        HashSet<UUID> mobsInBattle = teamBattleWandEvents.getMobsInBattle();
        mobsInBattle.remove(deadMob.getUniqueId());
        teamBattleWandEvents.setMobsInBattle(mobsInBattle);
        if (checkForWin() != TeamBattleOutcomes.NO_OUTCOME) {
            end(checkForWin());
        }
    }

    @EventHandler
    public void onMobSwitchTargets(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Mob))
            return;
        Mob targetingMob = (Mob) event.getEntity();
        if (redTeam.contains(targetingMob.getUniqueId()) || blueTeam.contains(targetingMob.getUniqueId())) {
            event.setCancelled(true);
            if (redTeam.contains(targetingMob.getUniqueId())) {
                targetingMob.setTarget((LivingEntity) getClosestEntity(targetingMob.getLocation(), blueTeam));

            } else if (blueTeam.contains(targetingMob.getUniqueId())) {
                targetingMob.setTarget((LivingEntity) getClosestEntity(targetingMob.getLocation(), redTeam));
            }


        }
    }

    @EventHandler
    public void onMobTargetMobInBattle(EntityTargetEvent event) {
        if (!ConfigManager.getTrue1v1()) {
            return;
        }
        if (!(event.getTarget() instanceof Mob))
            return;
        Mob targetedMob = (Mob) event.getTarget();
        if (redTeam.contains(targetedMob.getUniqueId()) || blueTeam.contains(targetedMob.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobTransform(EntityTransformEvent event) {
        if (!(event.getEntity() instanceof Mob)) {
            return;
        }
        if (!(event.getTransformedEntity() instanceof Mob)) {
            return;
        }
        Mob originalMob = (Mob) event.getEntity();
        Mob transformedMob = (Mob) event.getTransformedEntity();

        HashSet<UUID> mobsInBattle = teamBattleWandEvents.getMobsInBattle();
        if (blueTeam.contains(originalMob.getUniqueId())) {
            blueTeam.remove(originalMob.getUniqueId());
            blueTeam.add(transformedMob.getUniqueId());
            transformedMob.setTarget((LivingEntity) getClosestEntity(transformedMob.getLocation(), redTeam));
            mobsInBattle.remove(originalMob.getUniqueId());
            mobsInBattle.add(transformedMob.getUniqueId());
        } else if (redTeam.contains(originalMob.getUniqueId())) {
            redTeam.remove(originalMob.getUniqueId());
            redTeam.add(transformedMob.getUniqueId());
            transformedMob.setTarget((LivingEntity) getClosestEntity(transformedMob.getLocation(), blueTeam));
            mobsInBattle.remove(originalMob.getUniqueId());
            mobsInBattle.add(transformedMob.getUniqueId());
        }
        teamBattleWandEvents.setMobsInBattle(mobsInBattle);

    }

    @Override
    public void run() {
        cleanTeam(blueTeam);
        cleanTeam(redTeam);
        for (UUID uuid : blueTeam) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);
            mob.setTarget((LivingEntity) getClosestEntity(mob.getLocation(), redTeam));
        }
        for (UUID uuid : redTeam) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);
            mob.setTarget((LivingEntity) getClosestEntity(mob.getLocation(), blueTeam));
        }
        if (checkForWin() != TeamBattleOutcomes.NO_OUTCOME) {
            end(checkForWin());
        }
    }

    public HashSet<UUID> getRedTeam() {
        return redTeam;
    }

    public HashSet<UUID> getBlueTeam() {
        return blueTeam;
    }
}
