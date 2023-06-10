package dev.gether.getboxsettings.listeners;

import dev.gether.getboxsettings.data.change.ChangeManager;
import dev.gether.getboxsettings.data.user.User;
import dev.gether.getboxsettings.data.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockListener implements Listener {

    private final UserManager userManager;
    private final ChangeManager changeManager;

    public BreakBlockListener(UserManager userManager, ChangeManager changeManager)
    {
        this.userManager = userManager;
        this.changeManager = changeManager;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakBlock(BlockBreakEvent event)
    {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();
        User user = userManager.getUserData().get(player.getUniqueId());

        if(user==null)
            return;

        if(user.isEnableBlockConv())
            changeManager.changeToBlock(player);

        if(user.isEnableMoneyConv())
            changeManager.changeToCoin(player);

        if(user.isEnableSellMoney())
            changeManager.changeToMoney(player);

    }
}
