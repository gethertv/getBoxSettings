package dev.gether.getboxsettings.hook;

import me.q1zz.combatlog.CombatLogPlugin;
import me.q1zz.combatlog.combat.CombatManager;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.UUID;

public class Q1zZCombatLogHook {

    private CombatLogPlugin combatLogInstance;
    private CombatManager combatManager;

    public Q1zZCombatLogHook(Plugin q1zZCombatLog) {
        combatLogInstance = (CombatLogPlugin) q1zZCombatLog;

        // init combatManager
        initializeCombatManager();
    }

    public void initializeCombatManager() {
        try {
            Field combatManagerField = CombatLogPlugin.class.getDeclaredField("combatManager");
            combatManagerField.setAccessible(true);
            this.combatManager = (CombatManager) combatManagerField.get(combatLogInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isCombat(UUID playerUUID) {
        if (combatManager != null) {
            return combatManager.isFighting(playerUUID);
        }
        return false;
    }

}
