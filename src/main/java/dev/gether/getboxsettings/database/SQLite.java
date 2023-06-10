package dev.gether.getboxsettings.database;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.user.User;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class SQLite extends DatabaseManager{

    private Connection connection;
    private String database;

    private String createTableSQL = "CREATE TABLE IF NOT EXISTS "+table+" (" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    uuid VARCHAR(100) NOT NULL," +
            "    username VARCHAR(100) NOT NULL," +
            "    block_convert INTEGER NOT NULL DEFAULT 0," +
            "    money_convert INTEGER NOT NULL DEFAULT 0," +
            "    sell_money INTEGER NOT NULL DEFAULT 0," +
            "    action_bar INTEGER NOT NULL DEFAULT 0" +
            ");";

    public SQLite(String database){
        super();
        this.database = database;

        openConnection();
        createTable(createTableSQL);
    }

    public void update(String paramString) {
        try {
            Connection connection = getConnection();
            if (connection != null) {
                Statement statement = getConnection().createStatement();
                statement.executeUpdate(paramString);
            }
        } catch (SQLException sQLException) {
            System.out.println("[mysql] wrong update : '" + paramString + "'!");
        }
    }

    public void openConnection() {
        File dataFolder = new File(GetBoxSettings.getInstance().getDataFolder(), database+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                GetBoxSettings.getInstance().getLogger().log(Level.SEVERE, "File write error: "+database+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                this.connection = connection;
            }
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
        } catch (SQLException ex) {
            GetBoxSettings.getInstance().getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            GetBoxSettings.getInstance().getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
    }

    public void createTable(String sqlCreate) {

        update(sqlCreate);
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void loadUser(Player player) {
        if(!playerExists(player.getUniqueId()))
        {
            createUser(player);
            GetBoxSettings.getInstance().getUserManager().getUserData().put(player.getUniqueId(), new User(player));
            return;
        }

        String str = "SELECT * FROM "+table+" WHERE uuid = '" + player.getUniqueId() + "'";
        try {
            ResultSet resultSet = getResult(str);
            while (resultSet.next()) {
                boolean block_convert = resultSet.getBoolean("block_convert");
                boolean money_convert = resultSet.getBoolean("money_convert");
                boolean sell_money = resultSet.getBoolean("sell_money");
                boolean action_bar = resultSet.getBoolean("action_bar");

                GetBoxSettings.getInstance().getUserManager().getUserData().put(
                        player.getUniqueId(), new User(player, block_convert, money_convert, sell_money, action_bar));
            }

        } catch (SQLException | NullPointerException sQLException) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println(sQLException.getMessage());
                    player.kickPlayer("Bląd! Zgłoś sie na discord!");
                }
            }.runTask(GetBoxSettings.getInstance());
        }
    }

    @Override
    public void updateUser(Player player) {
        User user = GetBoxSettings.getInstance().getUserManager().getUserData().get(player.getUniqueId());
        if(user==null)
            return;

        update("UPDATE "+table+" SET " +
                "block_convert = '"+(user.isEnableBlockConv() ? 1 : 0)+"'," +
                "money_convert = '"+(user.isEnableMoneyConv() ? 1 : 0)+"'," +
                "sell_money = '"+(user.isEnableSellMoney() ? 1 : 0)+"', " +
                "action_bar = '"+(user.isActionBarEnable() ? 1 : 0)+"' " +
                "WHERE uuid = '"+player.getUniqueId()+"'" +
                "");
    }

    public void createUser(Player player)
    {
        update("INSERT INTO "+table+" (uuid, username) VALUES ('"+player.getUniqueId()+"', '"+player.getName()+"')");
    }


    public boolean playerExists(UUID uuid) {
        return (getPlayerID(uuid) != 0);
    }

    private int getPlayerID(UUID uuid) {
        return getInt("id", "SELECT id FROM "+table+" WHERE uuid='" + uuid.toString() + "'");
    }
    private int getInt(String paramString1, String paramString2) {
        try {
            ResultSet resultSet = getResult(paramString2);
            if (resultSet.next()) {
                int i = resultSet.getInt(paramString1);
                resultSet.close();
                return i;
            }
        } catch (SQLException sQLException) {
            return 0;
        }
        return 0;
    }

    public ResultSet getResult(String paramString) {
        ResultSet resultSet = null;
        Connection connection = getConnection();
        try {
            if (connection != null) {
                Statement statement = getConnection().createStatement();
                resultSet = statement.executeQuery(paramString);
            }
        } catch (SQLException sQLException) {
            System.out.println("[mysql] wrong when want get result: '" + paramString + "'!");
        }
        return resultSet;
    }
    public boolean isConnected() {
        return (getConnection() != null);
    }


}