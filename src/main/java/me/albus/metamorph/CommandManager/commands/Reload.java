 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.CommandManager.commands;

import me.albus.metamorph.CommandManager.SubCommands;
import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.config.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Reload extends SubCommands {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "mm.admin";
    }

    @Override
    public String getSyntax() {
        return "/metamorph reload <config | models | messages>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(Messages.chatMessage("syntax").replace("%this%", getSyntax()));
            return;
        }
        MetaMorph metaMorph = MetaMorph.getInstance();
        String arg = args[1];
        boolean reloaded = false;
        if (arg != null && !arg.isEmpty()) {
            switch (arg) {
                case "config":
                    metaMorph.config().reload();
                    reloaded = true;
                    break;
                case "messages":
                    Messages.reload();
                    reloaded = true;
                    break;
                case "models":
                    metaMorph.getModels().reload();
                    reloaded = true;
                    break;
                default:
                    break;
            }
        }
        if (reloaded) {
            player.sendMessage(Messages.chatMessage("reload").replace("%this%", arg));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length > 0) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("config");
            suggestions.add("messages");
            suggestions.add("models");
            return suggestions;
        }
        return null;
    }
}