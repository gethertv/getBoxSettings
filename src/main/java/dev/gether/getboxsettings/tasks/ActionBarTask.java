package dev.gether.getboxsettings.tasks;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.user.User;
import dev.gether.getboxsettings.data.user.UserManager;
import dev.gether.getboxsettings.utils.ColorFixer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarTask extends BukkitRunnable {
    private final UserManager userManager;

    public ActionBarTask(UserManager userManager)
    {
        this.userManager = userManager;
    }
    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if(GetBoxSettings.getInstance().getDisableActionBar().contains(player))
                continue;

            User user = GetBoxSettings.getInstance().getUserManager().getUserData().get(player.getUniqueId());
            if(user==null)
                continue;

            if(!user.isActionBarEnable())
                continue;

            String actionMessage = userManager.FORMAT_ACTION_BAR;
            actionMessage = actionMessage
                    .replace("{block_conv}", userManager.BLOCK_CONV_FORMAT_ACTION_BAR
                            .replace("{status}",user.isEnableBlockConv() ? userManager.STATUS_ON_ACTION_BAR : userManager.STATUS_OFF_ACTION_BAR))
                    .replace("{money_conv}", userManager.MONEY_CONV_FORMAT_ACTION_BAR
                            .replace("{status}",user.isEnableMoneyConv() ? userManager.STATUS_ON_ACTION_BAR : userManager.STATUS_OFF_ACTION_BAR))
                    .replace("{sell_money}", userManager.SELL_MONEY_FORMAT_ACTION_BAR
                            .replace("{status}",user.isEnableSellMoney() ? userManager.STATUS_ON_ACTION_BAR : userManager.STATUS_OFF_ACTION_BAR));

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorFixer.addColors(actionMessage)));
        }
    }
}
