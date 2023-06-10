package dev.gether.getboxsettings.file;

import dev.gether.getboxsettings.GetBoxSettings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ChangeFile {

    private static File file;
    private static FileConfiguration config;

    public static void setup() {
        file = new File(GetBoxSettings.getInstance().getDataFolder(), "change.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            GetBoxSettings.getInstance().saveResource("change.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("Nie mozna zapisac pliku!");
        }
    }


    public static void loadFile() {
        setup();
        save();
    }

}
