package dev.gether.getboxsettings.database;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.Properties;
import java.util.UUID;

public class MySQL extends DatabaseManager {
    private String host;
    private String username;
    private String password;
    private String database;
    private String port;
    private boolean ssl;
    private boolean isFinished;

    private Connection connection;

    public String createTableSQL = "CREATE TABLE IF NOT EXISTS "+table+" (" +
            "`id` INT(10) AUTO_INCREMENT, PRIMARY KEY (id)," +
            "`uuid` varchar(100) NOT NULL," +
            "`username` varchar(100) NOT NULL," +
            "`block_convert` BOOLEAN NOT NULL DEFAULT '0'," +
            "`money_convert` BOOLEAN NOT NULL DEFAULT '0'," +
            "`sell_money` BOOLEAN NOT NULL DEFAULT '0'," +
            "`action_bar` BOOLEAN NOT NULL DEFAULT '1'" +
            ");";

    public MySQL(String host, String username, String password, String database, String port, boolean ssl) {
        super();
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;
        this.ssl = ssl;

        openConnection();
        createTable(createTableSQL);
    }


    private void openConnection() {
        try {
            long l1 = System.currentTimeMillis();
            long l2 = 0L;
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");
            Properties properties = new Properties();
            properties.setProperty("user", getUsername());
            properties.setProperty("password", getPassword());
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("useSSL", String.valueOf(useSSL()));
            properties.setProperty("requireSSL", String.valueOf(useSSL()));
            properties.setProperty("verifyServerCertificate", "false");
            String str = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase();
            this.connection = DriverManager.getConnection(str, properties);

            l2 = System.currentTimeMillis();
            this.isFinished = true;
            System.out.println("[mysql] Connected successfully");
        } catch (ClassNotFoundException classNotFoundException) {
            this.isFinished = false;
            System.out.println("[mysql] Check your configuration.");
            Bukkit.getPluginManager().disablePlugin(GetBoxSettings.getInstance());
        } catch (SQLException sQLException) {
            this.isFinished = false;
            System.out.println("[mysql] (" + sQLException.getLocalizedMessage() + "). Check your configuration.");
            Bukkit.getPluginManager().disablePlugin(GetBoxSettings.getInstance());
        }
    }

    private void validateConnection() {
        if (!this.isFinished)
            return;
        try {
            if (this.connection == null) {
                System.out.println("[mysql] aborted. Connecting again");
                reConnect();
            }
            if (!this.connection.isValid(4)) {
                System.out.println("[mysql] timeout.");
                reConnect();
            }
            if (this.connection.isClosed()) {
                System.out.println("[mysql] closed. Connecting again");
                reConnect();
            }
        } catch (Exception exception) {
        }
    }

    private void reConnect() {
        System.out.println("[mysql] connection again");
        openConnection();
    }

    public void closeConnection() {
        if (getConnection() != null) {
            try {
                getConnection().close();
                System.out.println("[mysql] connection closed");
            } catch (SQLException sQLException) {
                System.out.println("[mysql] error when try close connection");
            }
        }
    }



    public void update(String paramString) {
        try {
            Connection connection = getConnection();
            if (connection != null) {
                Statement statement = getConnection().createStatement();
                statement.executeUpdate(paramString);
            }
        } catch (SQLException sQLException) {
            System.out.println("[mysql] wrong update : '" + paramString + "'! "+sQLException.getMessage());
        }
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


    public void createUser(Player player)
    {
        update("INSERT INTO "+table+" (uuid, username) VALUES ('"+player.getUniqueId()+"', '"+player.getName()+"')");
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
                "WHERE uuid = '"+player.getUniqueId()+"'"
                );
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


    public void createTable(String createSql) {
        update(createSql);
    }

    private String getUsername() {
        return this.username;
    }

    private String getPassword() {
        return this.password;
    }

    private String getHost() {
        return this.host;
    }

    private String getPort() {
        return this.port;
    }

    private String getDatabase() {
        return this.database;
    }

    private boolean useSSL() {
        return this.ssl;
    }

    public boolean isConnected() {
        return (getConnection() != null);
    }

    public Connection getConnection() {
        validateConnection();
        return this.connection;
    }


}
