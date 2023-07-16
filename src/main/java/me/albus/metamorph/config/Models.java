package me.albus.metamorph.config;

import me.albus.metamorph.MetaMorph;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Models {

    private final MetaMorph metaMorph;
    private File file;

    private YamlConfiguration config;

    public Models() {
        metaMorph = MetaMorph.getInstance();
    }

    public void setup() {
        file = metaMorph.getModel();
        if(!file.exists()) {
            try {
                boolean ignored = file.createNewFile();
            } catch(IOException e) {
                Bukkit.getLogger().severe("Could not generate models.yml because " + e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public YamlConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            Bukkit.getLogger().warning(file.getAbsolutePath().toString() + " save..");
            config.save(file);
        } catch(IOException e) {
            Bukkit.getLogger().severe("Could not save models.yml because " + e.getMessage());
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
