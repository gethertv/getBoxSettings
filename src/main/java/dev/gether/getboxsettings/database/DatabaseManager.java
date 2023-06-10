package dev.gether.getboxsettings.database;

import dev.gether.getboxsettings.GetBoxSettings;
import org.bukkit.entity.Player;

public abstract class DatabaseManager {


    public String table = "get_boxsettings";

    public abstract void loadUser(Player player);
    public abstract void updateUser(Player player);


    public abstract boolean isConnected();


}
