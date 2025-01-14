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

import lombok.Getter;
import me.albusthepenguin.metaMorph.misc.Message;
import me.albusthepenguin.metaMorph.MetaMorph;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for creating commands in a Bukkit plugin.
 * <p>
 * This class implements both CommandExecutor and TabCompleter to handle command execution and tab completion.
 * Subclasses must provide implementations for the abstract methods to define command-specific behavior.
 */
@Getter
@SuppressWarnings("unused")
public abstract class MinecraftCommand extends BukkitCommand implements TabExecutor {

    private final MetaMorph metaMorph;

    private final Message message;

    /**
     * Constructor for creating a new command.
     * @param metaMorph        the plugin.
     * @param name          The name of the command.
     * @param permission    The permission for the index command. For sub commands a player will need both this + the sub command permission.
     * @param description   The description of the command.
     * @param usageMessage  The usage message for the command.
     * @param aliases       A list of aliases for the command.
     */
    public MinecraftCommand(MetaMorph metaMorph, String name, String permission, String description, String usageMessage, List<String> aliases) {
        super(name);
        setDescription(description);
        setUsage(usageMessage);
        setAliases(aliases);
        setPermission(permission);
        this.metaMorph = metaMorph;
        this.message = this.metaMorph.getMessage();
    }

    /**
     * Executes the command when it is invoked by a player or the console.
     *
     * @param sender      The sender of the command (player or console).
     * @param commandLabel The label of the command.
     * @param args         The arguments passed to the command.
     * @return True if the command was successful, false otherwise.
     */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.hasPermission(Objects.requireNonNull(getPermission()))) {
            return onCommand(sender, this, commandLabel, args);
        } else {
            sender.sendMessage("You do not have permission to use this command.");
            return false;
        }

    }

    /**
     * Handles tab completion for the command.
     *
     * @param sender      The sender of the command (player or console).
     * @param alias       The alias of the command used.
     * @param args        The arguments passed to the command.
     * @return A list of suggested completions based on the current input.
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        // Ensure that onTabComplete does not return null
        return Objects.requireNonNull(onTabComplete(sender, this, alias, args));
    }

    /**
     * Abstract method for handling command execution.
     * <p>
     * Subclasses must implement this method to define the command's behavior.
     *
     * @param sender      The sender of the command (player or console).
     * @param command     The command object.
     * @param label       The label of the command.
     * @param args        The arguments passed to the command.
     * @return True if the command was successfully executed, false otherwise.
     */
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    /**
     * Abstract method for handling tab completion.
     * <p>
     * Subclasses must implement this method to provide suggestions for command arguments.
     *
     * @param sender      The sender of the command (player or console).
     * @param command     The command object.
     * @param alias       The alias of the command used.
     * @param args        The arguments passed to the command.
     * @return A list of suggested completions based on the current input.
     */
    public abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);

    /**
     * Registers the command with the server.
     * <p>
     * This method should be called during plugin initialization to ensure the command is properly registered.
     *
     * @param plugin The plugin instance used for registration.
     */
    public void register(Plugin plugin) {
        try {
            Field commandMapField = plugin.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(plugin.getServer());

            commandMap.register(getName(), this);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to register command: " + e.getMessage());
        }
    }
}