package dev.gether.getboxsettings.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class HookManager {

    private Q1zZCombatLogHook q1zZCombatLogHook;
    public HookManager() {


        Plugin q1zZCombatLog = Bukkit.getServer().getPluginManager().getPlugin("q1zZ-CombatLog");


        if(q1zZCombatLog!=null) {
            q1zZCombatLogHook = new Q1zZCombatLogHook(q1zZCombatLog);
        }
    }

    public boolean isQ1zZCombatLogHook() {
        return q1zZCombatLogHook != null;
    }

    public Q1zZCombatLogHook getQ1zZCombatLogHook() {
        return q1zZCombatLogHook;
    }
}
