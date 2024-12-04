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

import lombok.NonNull;

import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.Configs.Configuration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;

public class Message {

    private final Configuration configuration;

    public Message(Configuration configuration) {
        this.configuration = configuration;
    }


    public String get(String path, Map<String, String> replacements, boolean colored) {
        YamlConfiguration config = this.configuration.getConfig(ConfigType.Messages);
        if(config == null) {
            throw new IllegalArgumentException("configuration is null. Please verify that messages.yml is located in /plugins/metamorph folder.");
        }
        String message = config.getString(path);
        if(message == null) {
            message = path;
        }

        if(replacements != null && !replacements.isEmpty()) {
            for(Map.Entry<String, String> entry : replacements.entrySet()) {
                if(message.contains(entry.getKey())) {
                    message = message.replace(entry.getKey(), entry.getValue());
                }
            }
        }

        if(colored) {
            message = setColor(message);
        }
        return message;
    }

    public String setColor(@NonNull String text) {
        String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }

}
