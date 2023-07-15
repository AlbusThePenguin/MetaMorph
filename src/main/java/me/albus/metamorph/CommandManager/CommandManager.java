package me.albus.metamorph.CommandManager;

import me.albus.metamorph.CommandManager.commands.Add;
import me.albus.metamorph.CommandManager.commands.Reload;
import me.albus.metamorph.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    private final ArrayList<SubCommands> subcommands = new ArrayList<>();
    public CommandManager(){
        subcommands.add(new Reload());
        subcommands.add(new Add());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        if(player.hasPermission(getSubcommands().get(i).getPermission())) {
                            getSubcommands().get(i).perform(player, args);
                        } else {
                            player.sendMessage(Messages.chatMessage("permission").replace("%this%", getSubcommands().get(i).getPermission()));
                        }
                    }
                }
            } else {
                //Todo: open menu with all available skins if any.
            }
        }
        return true;
    }

    public ArrayList<SubCommands> getSubcommands(){
        return subcommands;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1){
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++){
                if(sender.hasPermission(getSubcommands().get(i).getPermission())) {
                    subcommandsArguments.add(getSubcommands().get(i).getName());
                }
            }

            return subcommandsArguments;
        }
        return null;
    }

}