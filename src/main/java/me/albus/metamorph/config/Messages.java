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

        get().addDefault("command_error_item_null", "&eThe item in your hand cannot be a &bnull &eitem.");
        get().addDefault("command_error_item_air", "");
        get().addDefault("command_error_item_no_model", "");
        get().addDefault("command_error_item_model_number_invalid", "");
        get().addDefault("command_error_model_exists", "");

        get().addDefault("command_add_description", "&eAdds the item in your hand into the &bmodels.yml &eThe item requires a model on it to be added.");
        get().addDefault("command_add_success", "");

        get().addDefault("permission", "&cYou're missing permission &e%this%");
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
}
