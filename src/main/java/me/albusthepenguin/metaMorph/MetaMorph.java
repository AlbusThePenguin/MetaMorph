/*
 * This file is part of MetaMorph.
 *
 * MetaMorph is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MetaMorph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with MetaMorph. If not, see <http://www.gnu.org/licenses/>.
 */
package me.albusthepenguin.metaMorph;

import lombok.Getter;
import me.albusthepenguin.metaMorph.Commands.CommandManager;
import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.Configs.Configuration;
import me.albusthepenguin.metaMorph.Hooks.LuckPermsHook;
import me.albusthepenguin.metaMorph.Hooks.VaultHook;
import me.albusthepenguin.metaMorph.Menu.MenuListener;
import me.albusthepenguin.metaMorph.Models.ModelHandler;
import me.albusthepenguin.metaMorph.Preview.Preview;
import me.albusthepenguin.metaMorph.misc.Message;
import me.albusthepenguin.metaMorph.misc.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class MetaMorph extends JavaPlugin {

    private MetaMorph metaMorph;

    private Configuration configuration;

    private ModelHandler modelHandler;

    private LuckPermsHook luckPermsHook;

    private VaultHook vaultHook;

    private Preview preview;

    private Message message;

    @Override
    public void onEnable() {
        this.metaMorph = this;

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        this.configuration = new Configuration(this);
        this.configuration.load();

        this.message = new Message(this.configuration);

        this.modelHandler = new ModelHandler(this);

        this.preview = new Preview(this);

        Plugin luckPerms = getServer().getPluginManager().getPlugin("LuckPerms");
        if(luckPerms == null) {
            this.getLogger().severe("Could not find LuckPerms. This plugin requires 'LuckPerms' to have the give and buy feature.");
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            this.luckPermsHook = new LuckPermsHook();
        }

        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault == null) {
            this.getLogger().severe("Could not find LuckPerms. This plugin requires 'Vault' to have the give and buy feature.");
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            this.vaultHook = new VaultHook(this);
        }

        this.buildInGameCommand();

        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);

        new Metrics(this, 19112);

        if(this.configuration.getConfig(ConfigType.Config).getBoolean("update-checker", true)) {
            new UpdateChecker(this, 109270).getVersion(version -> {
                String yourVersion = this.getDescription().getVersion();
                if(!yourVersion.equals(version)) {
                    this.getLogger().info("There is a new version available. You are using version: " + yourVersion + " and the latest version is " + version);
                }
            });
        }
    }

    public void onReload() {
        this.modelHandler.reloadAll(); //Later problem <--
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

        String permission = section.getString("permission", "mm.use");

        List<String> aliases = section.getStringList("aliases");

        new CommandManager(
                this, commandLabel, permission, description, usageMessage, aliases
        );
    }
}
