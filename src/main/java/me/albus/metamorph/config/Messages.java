 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.config;

import me.albus.metamorph.MetaMorph;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Messages {
    public static void addDefaults() {
        get().addDefault("plugin_prefix", "&8[&eMetaMorph&8]");

        get().addDefault("reload", "&aYou reloaded &e%this%");

        get().addDefault("syntax", "&cWrong syntax: &e%this%");

        get().addDefault("command_error_item_null", "&cYou have no item in your hand.");
        get().addDefault("command_error_item_air", "&cYou have no item in your hand.");
        get().addDefault("command_error_item_no_model", "&cThe item in your hand has no model.");
        get().addDefault("command_error_item_model_number_invalid", "&cThe item in your hand has no model.");
        get().addDefault("command_error_model_exists", "&cThe item model on the item in your hand doesn't exists in the &bmodels.yml");

        get().addDefault("command_add_description", "&eAdds the item in your hand into the &bmodels.yml &eThe item requires a model on it to be added.");
        get().addDefault("command_add_success", "&aYou added this model into your &bmodels.yml");

        get().addDefault("command_remove_description", "&eRemove the item model on the item in your hand from &bmodels.yml");
        get().addDefault("command_remove_success", "&aThe model has been removed from &bmodels.yml");

        get().addDefault("command_clean_description", "&eRemove the model from the item in your hand.");
        get().addDefault("command_clean_success", "&aYou washed away the model from the item in your hand.");

        get().addDefault("command_set_description", "&eSelect the model you would like to set on your item.");
        get().addDefault("command_set_success", "&aYou set a new item model on your item.");

        get().addDefault("gui_button_clean_click", "&eWooosh! washed away the model");
        get().addDefault("gui_button_model_click", "&aSet a new model to your item!");

        get().addDefault("permission", "&cYou're missing permission &e%this%");

        get().addDefault("error_missing_item", "&cWe do not have any models for the item in your hand.");

    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public static String translateColorCodes(String text) {
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));
        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    private static File file;
    private static FileConfiguration messageFile;
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    public static void setup() {
        file = MetaMorph.getInstance().getMessage();
        if (!file.exists()) {
            try {
                boolean ignored = file.createNewFile();
            } catch (IOException ex) {
                MetaMorph.getInstance().getLogger().severe("Unable to create messages.yml: " + ex.getMessage());
            }
        }
        messageFile = YamlConfiguration.loadConfiguration(file);

        messageFile.options().copyDefaults(true);
    }

    public static FileConfiguration get() {
        return messageFile;
    }

    public static void save() {
        try {
            messageFile.save(file);
        }
        catch (IOException e) {
            Bukkit.getServer().getLogger().info("Couldn't save messages.yml");
        }
    }
    public static void reload() {
        messageFile = YamlConfiguration.loadConfiguration(file);
    }

    public static String chatMessage(final String path) {
        return translateColorCodes(Objects.requireNonNull(get().getString("plugin_prefix"))) + translateColorCodes("&r ") + translateColorCodes(Objects.requireNonNull(get().getString(path)));
    }

    public static String message(final String path) {
        return translateColorCodes(Objects.requireNonNull(get().getString(path)));
    }
}
