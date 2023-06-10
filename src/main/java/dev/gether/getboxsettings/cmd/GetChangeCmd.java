package dev.gether.getboxsettings.cmd;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.utils.ColorFixer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GetChangeCmd extends Command {
    public GetChangeCmd(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        if(!player.hasPermission("getchange.admin"))
            return false;

        if(args.length==1)
        {
            if(args[0].equalsIgnoreCase("reload"))
            {
                GetBoxSettings.getInstance().reloadPlugin(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("coins"))
            {
                GetBoxSettings.getInstance().getChangeManager().openAdminCoinsInv(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("money"))
            {
                GetBoxSettings.getInstance().getChangeManager().openAdminMoneyInv(player);
                return true;
            }
        }
        player.sendMessage(ColorFixer.addColors("/"+commandLabel+" <coins/money/reload>"));
        return true;
    }


}
