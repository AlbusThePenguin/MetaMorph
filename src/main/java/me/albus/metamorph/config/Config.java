 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.config;

import me.albus.metamorph.MetaMorph;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private YamlConfiguration config;

    private final File file;

    public Config() {

        this.file = new File(MetaMorph.getInstance().getDataFolder(), "config.yml");
    }

    public YamlConfiguration get() {
        return this.config;
    }

    public void setup() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch(IOException e) {
            Bukkit.getServer().getLogger().info("Couldn't save config.yml");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
