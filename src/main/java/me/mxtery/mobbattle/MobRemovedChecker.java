package me.mxtery.mobbattle;

import me.mxtery.mobbattle.events.MobBattleWandEvents;
import me.mxtery.mobbattle.helpers.PlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class MobRemovedChecker extends BukkitRunnable {
    private MobBattleWandEvents mobBattleWandEvents;

    public void start(MobBattle plugin, MobBattleWandEvents mobBattleWandEvents) {
        this.mobBattleWandEvents = mobBattleWandEvents;
        this.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PlayerHelper.playerHoldingItem(player, Keys.MOB_BATTLE_WAND, PersistentDataType.INTEGER)) {
                Mob entity1 = null;
                if (mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()) != null) {
                    entity1 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity1().get(player.getUniqueId()));
                }
                Mob entity2 = null;
                if (mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()) != null) {
                    entity2 = (Mob) Bukkit.getEntity(mobBattleWandEvents.getPlayerToEntity2().get(player.getUniqueId()));
                }
                if (entity1 == null || entity2 == null || entity1.isDead() || entity2.isDead()) {
                    if (mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId()) != null && mobBattleWandEvents.getPlayerToInBattle().get(player.getUniqueId())) {
                        mobBattleWandEvents.endBattle(entity1, entity2, player);
                    }
                }
            }
        }
    }


}