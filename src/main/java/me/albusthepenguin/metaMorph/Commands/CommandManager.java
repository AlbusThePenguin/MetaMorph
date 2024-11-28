package me.albusthepenguin.metaMorph.Commands;

import me.albusthepenguin.metaMorph.Models.CategoryMenu;
import me.albusthepenguin.metaMorph.Menu.MenuUtilities;
import me.albusthepenguin.metaMorph.Message;
import me.albusthepenguin.metaMorph.MetaMorph;
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
                        hook -> this.subcommands.add(new giveModelCommand(metaMorph, "give", "mm.give", "metamorph give <player> <model>")),
                        () -> metaMorph.getLogger().info("LuckPerms support was not found disabling the command /metamorph give <player> <model>")
                );

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
                            player.sendMessage(Message.setColor("&cYou don't have permission to perform this command!"));
                        }
                        return true; // Command handled successfully
                    } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
                        subCommand.perform(consoleCommandSender, args);
                        return true; // Command handled successfully
                    }
                }
            }

            sender.sendMessage(Message.setColor("&cFound no command by that name."));
            return true; // Indicate that the command was not recognized x
        }

        if(sender instanceof Player player) {
            MenuUtilities menuUtilities = new MenuUtilities(player);
            new CategoryMenu(super.getMetaMorph(), menuUtilities, super.getMetaMorph().getModelHandler()).open();
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
