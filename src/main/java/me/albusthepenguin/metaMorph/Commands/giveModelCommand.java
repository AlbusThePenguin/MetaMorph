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
package me.albusthepenguin.metaMorph.Commands;

import me.albusthepenguin.metaMorph.MetaMorph;
import me.albusthepenguin.metaMorph.Models.Model;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class giveModelCommand extends MinecraftSubCommand {

    protected giveModelCommand(MetaMorph metaMorph, String name, String permission, String syntax) {
        super(metaMorph, name, permission, syntax);
    }

    @Override
    public void perform(Player player, String[] args) {
        if(!player.hasPermission(super.getPermission())) {
            player.sendMessage(super.getMessage().get("no-permission", null, true));
            return;
        }

        if(super.getMetaMorph().getLuckPermsHook() == null) {
            player.sendMessage(super.getMessage().get("ops-issue", null, true));
            return;
        }

        if(args.length != 3) {
            player.sendMessage(super.getMessage().get("wrong-syntax", Map.of("{syntax}", super.getSyntax()), true));
            return;
        }

        OfflinePlayer target = Bukkit.getPlayer(args[1]);
        if(target == null || !target.hasPlayedBefore()) {
            player.sendMessage(super.getMessage().get("no-player", Map.of("{player}", args[1]), true));
            return;
        }

        String modelName = args[2];

        Model model = null;

        for(Map.Entry<String, Model> entry : this.getMetaMorph().getModelHandler().getModels().entrySet()) {
            if(entry.getValue().getId().equalsIgnoreCase(modelName)) {
                model = entry.getValue();
            }
        }

        //no-model

        if(model == null) {
            player.sendMessage(super.getMessage().get("no-model", Map.of("{name}", modelName), true));
        } else {
            if (super.getMetaMorph().getLuckPermsHook().givePermission(target, model)) {
                player.sendMessage(super.getMessage().get("give-model", Map.of("{player}", target.getName(), "{name}", model.getDisplayName()), true));
            } else {
                player.sendMessage(super.getMessage().get("ops-issue", null, true));
            }
        }
    }

    @Override
    public void perform(ConsoleCommandSender console, String[] args) {
        if(super.getMetaMorph().getLuckPermsHook() == null) {
            console.sendMessage("This command is not usable.");
            return;
        }

        if(args.length != 3) {
            console.sendMessage(this.getSyntax());
            return;
        }

        OfflinePlayer target = Bukkit.getPlayer(args[1]);
        if(target == null || !target.hasPlayedBefore()) {
            console.sendMessage("Could not find '" + args[1] + "' online!");
            return;
        }

        String modelName = args[2];

        Model model = null;

        for(Map.Entry<String, Model> entry : this.getMetaMorph().getModelHandler().getModels().entrySet()) {
            if(entry.getValue().getId().equalsIgnoreCase(modelName)) {
                model = entry.getValue();
            }
        }

        if(model == null) {
            console.sendMessage("Could not find a model called " + modelName);
        } else {
            if (super.getMetaMorph().getLuckPermsHook().givePermission(target, model)) {
                console.sendMessage("Gave " + target.getName() + " the model " + model.getDisplayName());
            } else {
                console.sendMessage("Something went wrong!");
            }
        }
    }
    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        int length = args.length;

        if (length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList();
        }

        if (length == 3) {
            return this.getMetaMorph().getModelHandler().getModels().values().stream()
                    .map(Model::getId)
                    .toList();
        }

        return List.of();
    }
}
