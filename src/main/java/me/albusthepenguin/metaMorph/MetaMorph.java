package me.albusthepenguin.metaMorph;

import lombok.Getter;
import me.albusthepenguin.metaMorph.Commands.CommandManager;
import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.Configs.Configuration;
import me.albusthepenguin.metaMorph.Hooks.LuckPermsHook;
import me.albusthepenguin.metaMorph.Menu.MenuListener;
import me.albusthepenguin.metaMorph.Models.ModelHandler;
import me.albusthepenguin.metaMorph.Preview.Preview;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class MetaMorph extends JavaPlugin {

    private MetaMorph metaMorph;

    private Configuration configuration;

    private ModelHandler modelHandler;

    /**
     * This is @nullable.
     */
    private LuckPermsHook luckPermsHook;

    private Preview preview;

    @Override
    public void onEnable() {
        this.metaMorph = this;

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        this.configuration = new Configuration(this);
        this.configuration.load();

        this.modelHandler = new ModelHandler(this, this.configuration);

        this.preview = new Preview(this);

        Plugin luckPerms = getServer().getPluginManager().getPlugin("LuckPerms");
        if(luckPerms != null) {
            luckPermsHook = new LuckPermsHook();
        }

        this.buildInGameCommand();

        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    private void buildInGameCommand() {
        ConfigurationSection section = this.configuration.getConfig(ConfigType.Config).getConfigurationSection("Command");

        if (section == null) {
            throw new IllegalArgumentException("Could not find Commands section in config.yml. Cannot load default commands.");
        }

        String commandLabel = section.getString("name");
        if (commandLabel == null || commandLabel.isEmpty()) {
            throw new IllegalArgumentException("The 'name' field is missing or empty in the Commands section of config.yml.");
        }

        String description = section.getString("description");
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("The 'description' field is missing or empty in the Commands section of config.yml.");
        }

        String usageMessage = section.getString("usage");
        if (usageMessage == null || usageMessage.isEmpty()) {
            throw new IllegalArgumentException("The 'usage' field is missing or empty in the Commands section of config.yml.");
        }

        List<String> aliases = section.getStringList("aliases");

        new CommandManager(
                this, commandLabel, "mm.admin", description, usageMessage, aliases
        );
    }
}
