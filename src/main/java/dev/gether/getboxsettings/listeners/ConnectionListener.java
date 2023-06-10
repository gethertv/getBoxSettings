package dev.gether.getboxsettings.listeners;

import dev.gether.getboxsettings.GetBoxSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                GetBoxSettings.getInstance().getDatabaseManager().loadUser(player);
            }
        }.runTaskAsynchronously(GetBoxSettings.getInstance());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                GetBoxSettings.getInstance().getDatabaseManager().updateUser(player);
                GetBoxSettings.getInstance().getUserManager().getUserData().remove(player.getUniqueId());
            }
        }.runTaskAsynchronously(GetBoxSettings.getInstance());
    }
}
