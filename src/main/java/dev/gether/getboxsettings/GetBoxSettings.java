package dev.gether.getboxsettings;

import dev.gether.getboxsettings.cmd.GetChangeCmd;
import dev.gether.getboxsettings.cmd.GetSettingsCmd;
import dev.gether.getboxsettings.cmd.GetWymienCmd;
import dev.gether.getboxsettings.data.change.ChangeManager;
import dev.gether.getboxsettings.data.user.UserManager;
import dev.gether.getboxsettings.database.DatabaseManager;
import dev.gether.getboxsettings.database.DatabaseType;
import dev.gether.getboxsettings.database.MySQL;
import dev.gether.getboxsettings.database.SQLite;
import dev.gether.getboxsettings.file.ChangeFile;
import dev.gether.getboxsettings.listeners.BreakBlockListener;
import dev.gether.getboxsettings.listeners.ConnectionListener;
import dev.gether.getboxsettings.listeners.InventoryClickListener;
import dev.gether.getboxsettings.tasks.ActionBarTask;
import dev.gether.getboxsettings.tasks.AutoChange;
import dev.gether.getboxsettings.tasks.AutoSave;
import dev.gether.getboxsettings.utils.ColorFixer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class GetBoxSettings extends JavaPlugin {

    private static GetBoxSettings instance;
    private DatabaseManager databaseManager;
    private UserManager userManager;
    private ChangeManager changeManager;

    private static Economy econ = null;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ChangeFile.loadFile();

        implementsSql();
        if(!databaseManager.isConnected())
        {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupEconomy() ) {
            getServer().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        userManager = new UserManager(this);
        changeManager = new ChangeManager();

        loadingUsersOnline();

        String settings = getConfig().getString("command.settings");
        String wymien = getConfig().getString("command.wymien");
        getServer().getCommandMap().register(settings, new GetSettingsCmd(settings));
        getServer().getCommandMap().register(wymien, new GetWymienCmd(wymien));
        getServer().getCommandMap().register("getchange", new GetChangeCmd("getchange"));

        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new BreakBlockListener(userManager, changeManager), this);

        new ActionBarTask(userManager).runTaskTimer(this, 10L, 10L);
        new AutoSave(this).runTaskTimer(this, 20L*300, 20L*300);

        if(getConfig().getBoolean("auto-sell.enable"))
            new AutoChange(changeManager).runTaskTimer(this, 20L*getConfig().getInt("auto-sell.time"), 20L*getConfig().getInt("auto-sell.time"));

    }

    public void reloadPlugin(Player player)
    {
        reloadConfig();
        ChangeFile.loadFile();
        getUserManager().injectConfig();
        changeManager.injectData();
        player.sendMessage(ColorFixer.addColors("&aPomyslnie przeladowano plugin!"));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void loadingUsersOnline() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> getDatabaseManager().loadUser(player));
            }
        }.runTaskAsynchronously(this);
    }

    private void implementsSql() {
        DatabaseType databaseType = DatabaseType.valueOf(getConfig().getString("database"));
        if(databaseType==DatabaseType.MYSQL)
            setupMysql();

        if(databaseType==DatabaseType.SQLITE)
            databaseManager = new SQLite(getConfig().getString("sqlite.name"));

    }

    private void setupMysql() {
        String host = getConfig().getString("mysql.host");
        String username = getConfig().getString("mysql.username");
        String password = getConfig().getString("mysql.password");
        String database = getConfig().getString("mysql.database");
        String port = getConfig().getString("mysql.port");

        boolean ssl = false;
        if (getConfig().get("mysql.ssl") != null) {
            ssl = getConfig().getBoolean("mysql.ssl");
        }

        databaseManager = new MySQL(host, username, password, database, port, ssl);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ChangeManager getChangeManager() {
        return changeManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static GetBoxSettings getInstance() {
        return instance;
    }

    public static Economy getEcon() {
        return econ;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
