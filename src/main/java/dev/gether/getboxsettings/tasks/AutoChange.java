package dev.gether.getboxsettings.tasks;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.change.ChangeManager;
import dev.gether.getboxsettings.data.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoChange extends BukkitRunnable {

    private final ChangeManager changeManager;

    public AutoChange(ChangeManager changeManager)
    {
        this.changeManager = changeManager;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = GetBoxSettings.getInstance().getUserManager().getUserData().get(player.getUniqueId());

            if(user!=null)
            {
                if(user.isEnableBlockConv())
                    changeManager.changeToBlock(player);

                if(user.isEnableMoneyConv())
                    changeManager.changeToCoin(player);

                if(user.isEnableSellMoney())
                    changeManager.changeToMoney(player);
            }
        });
    }
}
