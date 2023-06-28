package dev.gether.getboxsettings.cmd;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.Cuboid;
import dev.gether.getboxsettings.listeners.InteractListener;
import dev.gether.getboxsettings.utils.ColorFixer;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GetChangeCmd implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
            if(args[0].equalsIgnoreCase("selector"))
            {
                player.getInventory().addItem(GetBoxSettings.getInstance().getSelector().clone());
                return true;
            }
        }
        if(args.length==2)
        {
            String name = args[1];
            if(GetBoxSettings.getInstance().getConfig().isSet("disable-regions."+name))
            {
                player.sendMessage(ColorFixer.addColors("&cRegion o podanej nazwie juz istnieje!"));
                return false;
            }
            Location first = InteractListener.getLocFirst().get(player.getUniqueId());
            Location second = InteractListener.getLocSecond().get(player.getUniqueId());
            if(first==null || second==null)
            {
                player.sendMessage(ColorFixer.addColors("&aMusisz miec zaznaczone dwie lokalizacje!"));
                return true;
            }
            GetBoxSettings.getInstance().getConfig().set("disable-regions."+name+".first-loc", first);
            GetBoxSettings.getInstance().getConfig().set("disable-regions."+name+".second-loc", second);
            GetBoxSettings.getInstance().saveConfig();
            GetBoxSettings.getInstance().getDisableTaskRegion().add(new Cuboid(first, second));
            player.sendMessage(ColorFixer.addColors("&aPomyslnie stworzono region!"));
            return true;
        }
        player.sendMessage(ColorFixer.addColors("/"+label+" <coins/money/reload/region/selector>"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==1)
        {
            return Arrays.asList("reload", "coins", "money", "region", "selector");
        }
        return null;
    }
}
