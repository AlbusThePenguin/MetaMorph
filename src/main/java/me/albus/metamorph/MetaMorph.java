 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph;

import me.albus.metamorph.CommandManager.CommandManager;
import me.albus.metamorph.MenuManager.MenuListener;
import me.albus.metamorph.MenuManager.MenuUtilities;
import me.albus.metamorph.ModelManager.ModelManager;
import me.albus.metamorph.config.Config;
import me.albus.metamorph.config.Messages;
import me.albus.metamorph.config.Models;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public final class MetaMorph extends JavaPlugin {
    private static MetaMorph instance;
    private Models models;
    private Config config;
    private File message;
    private File model;
    private ModelManager modelManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        config = new Config();
        config.setup();

        message = new File(getDataFolder(), "messages.yml");
        if(!message.exists()) {
            saveResource("messages.yml", false);
        }

        model = new File(getDataFolder(), "models.yml");
        if(!model.exists()) {
            saveResource("models.yml", false);
        }

        Messages.setup();
        Messages.addDefaults();
        Messages.save();

        models = new Models();
        models.setup();

        modelManager = new ModelManager();

        CommandManager commandManager = new CommandManager();
        Objects.requireNonNull(getCommand("metamorph")).setExecutor(commandManager);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        new Metrics(this, 19112);
    }

    public File getMessage() {
        return message;
    }

    public File getModel() {
        return model;
    }

    public static MetaMorph getInstance() {
        return instance;
    }

    public Config config() {
        return config;
    }

    public Models getModels() {
        return models;
    }

    public ModelManager getModelManager() {
        return modelManager;
    }

    private static final HashMap<Player, MenuUtilities> menuUtilitiesMap = new HashMap<>();

    public static MenuUtilities menuUtilities(Player p) {
        MenuUtilities playerMenuUtility;
        if (!(menuUtilitiesMap.containsKey(p))) {
            playerMenuUtility = new MenuUtilities(p);
            menuUtilitiesMap.put(p, playerMenuUtility);
            return playerMenuUtility;
        } else {
            return menuUtilitiesMap.get(p);
        }
    }
}
