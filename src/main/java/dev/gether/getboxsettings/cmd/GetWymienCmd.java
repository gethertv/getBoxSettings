package dev.gether.getboxsettings.cmd;

import dev.gether.getboxsettings.GetBoxSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GetWymienCmd implements TabExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==1)
        {
            return Arrays.asList("bloki", "coins", "money");
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        if(!player.hasPermission("getwymien.use"))
            return false;

        if(args.length==1)
        {
            if(args[0].equalsIgnoreCase("bloki"))
            {
                GetBoxSettings.getInstance().getChangeManager().changeToBlock(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("coins"))
            {
                GetBoxSettings.getInstance().getChangeManager().changeToCoin(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("money"))
            {
                GetBoxSettings.getInstance().getChangeManager().changeToMoney(player);
                return true;
            }
        }
        GetBoxSettings.getInstance().getChangeManager().changeToBlock(player);
        GetBoxSettings.getInstance().getChangeManager().changeToCoin(player);
        return false;
    }
}
