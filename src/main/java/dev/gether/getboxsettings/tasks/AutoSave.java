package dev.gether.getboxsettings.tasks;

import dev.gether.getboxsettings.GetBoxSettings;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave extends BukkitRunnable {

    GetBoxSettings plugin;
    public AutoSave(GetBoxSettings plugin)
    {
        this.plugin = plugin;
    }
    @Override
    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> plugin.getDatabaseManager().updateUser(player));
            }
        }.runTaskAsynchronously(plugin);
    }
}
