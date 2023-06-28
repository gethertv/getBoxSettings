package dev.gether.getboxsettings.listeners;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.utils.ColorFixer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.UUID;

public class InteractListener implements Listener {

    private static HashMap<UUID, Location> locFirst = new HashMap<>();
    private static HashMap<UUID, Location> locSecond = new HashMap<>();

    private final GetBoxSettings plugin;
    public InteractListener(GetBoxSettings plugin)
    {
        this.plugin = plugin;
        //plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if(!player.hasPermission("getboxsettings.admin"))
            return;

        if(event.getAction()== Action.LEFT_CLICK_BLOCK) {
            if (player.getItemInHand().isSimilar(plugin.getSelector())) {
                event.setCancelled(true);
                locFirst.put(player.getUniqueId(), event.getClickedBlock().getLocation());
                player.sendMessage(ColorFixer.addColors("&aZaznaczono FIRST location!"));
            }
        }
        if(event.getAction()== Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().isSimilar(plugin.getSelector())) {
                if(event.getHand()== EquipmentSlot.OFF_HAND)
                    return;

                event.setCancelled(true);
                locSecond.put(player.getUniqueId(), event.getClickedBlock().getLocation());
                player.sendMessage(ColorFixer.addColors("&aZaznaczono SECOND location!"));
            }
        }
    }

    public static HashMap<UUID, Location> getLocSecond() {
        return locSecond;
    }

    public static HashMap<UUID, Location> getLocFirst() {
        return locFirst;
    }
}
