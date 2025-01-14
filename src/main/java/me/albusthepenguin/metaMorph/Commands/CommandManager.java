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

import me.albusthepenguin.metaMorph.Menu.MenuUtilities;
import me.albusthepenguin.metaMorph.MetaMorph;
import me.albusthepenguin.metaMorph.Models.Model;
import me.albusthepenguin.metaMorph.Models.ModelMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager extends MinecraftCommand {

    private final ArrayList<MinecraftSubCommand> subcommands = new ArrayList<>();
    /**
     * Constructor for creating a new command.
     *
     * @param metaMorph       the plugin.
     * @param name         The name of the command.
     * @param permission   The permission for the index command. For sub commands a player will need both this + the sub command permission.
     * @param description  The description of the command.
     * @param usageMessage The usage message for the command.
     * @param aliases      A list of aliases for the command.
     */
    public CommandManager(MetaMorph metaMorph, String name, String permission, String description, String usageMessage, List<String> aliases) {
        super(metaMorph, name, permission, description, usageMessage, aliases);

        Optional.ofNullable(metaMorph.getLuckPermsHook())
                .ifPresentOrElse(
                        hook -> this.subcommands.add(new giveModelCommand(metaMorph, "give", "mm.admin", "metamorph give <player> <model>")),
                        () -> metaMorph.getLogger().info("LuckPerms support was not found disabling the command /metamorph give <player> <model>")
                );

        this.subcommands.add(new reloadCommand(
                this.getMetaMorph(),
                "reload",
                "mm.admin",
                "metamorph reload"
        ));

        this.register(metaMorph);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && !subcommands.isEmpty()) {
            for (MinecraftSubCommand subCommand : subcommands) {
                // If the command matches, handle it
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    if (sender instanceof Player player) {
                        if (player.hasPermission(subCommand.getPermission())) {
                            subCommand.perform(player, args);
                        } else {
                            player.sendMessage(super.getMessage().get("no-permission", null, true));
                        }
                        return true; // Command handled successfully
                    } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
                        subCommand.perform(consoleCommandSender, args);
                        return true; // Command handled successfully
                    }
                }
            }

            sender.sendMessage(super.getMessage().get("no-command", null, true));
            return true;
        }

        if(sender instanceof Player player) {
            List<Model> models = super.getMetaMorph().getModelHandler().getModels(player.getInventory().getItemInMainHand().getType());
            if (!models.isEmpty()) {
                MenuUtilities menuUtilities = new MenuUtilities(player);
                new ModelMenu(super.getMetaMorph(), menuUtilities, super.getMetaMorph().getModelHandler(), models).open();
            } else {
                player.sendMessage(super.getMessage().get("no-models-material", null, true));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(sender instanceof Player player) {
            if (!player.hasPermission(Objects.requireNonNull(getPermission()))) {
                return Collections.emptyList();
            }

            List<String> arguments = new ArrayList<>();

            if (args.length == 0) {
                return arguments;
            }

            for (MinecraftSubCommand subCommand : subcommands) {
                if (subCommand == null) {
                    throw new IllegalArgumentException("Failed to execute '" + args[0] + "' because sub command is null.");
                }

                if (!player.hasPermission(subCommand.getPermission())) {
                    continue;
                }

                if (args.length == 1) {
                    arguments.add(subCommand.getName());
                } else if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    List<String> subArgs = subCommand.getSubcommandArguments(player, args);
                    arguments = subArgs != null ? subArgs : Collections.emptyList();
                    break;
                }
            }

            return arguments;
        }
        return Collections.emptyList();
    }
}
