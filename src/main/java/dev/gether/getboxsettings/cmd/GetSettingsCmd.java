package dev.gether.getboxsettings.cmd;

import dev.gether.getboxsettings.GetBoxSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetSettingsCmd implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        if(!player.hasPermission("getboxsettings.use"))
            return false;

        GetBoxSettings.getInstance().getUserManager().openSettings(player);
        return true;
    }
}
