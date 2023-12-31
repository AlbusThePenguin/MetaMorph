 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.CommandManager;

import me.albus.metamorph.CommandManager.commands.*;
import me.albus.metamorph.MenuManager.MenuUtilities;
import me.albus.metamorph.MenuManager.Menus.ModelMenu;
import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.ModelManager.ModelManager;
import me.albus.metamorph.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")
public class CommandManager implements TabExecutor {
    private final ArrayList<SubCommands> subcommands = new ArrayList<>();
    public CommandManager(){
        subcommands.add(new Reload());
        subcommands.add(new Add());
        subcommands.add(new Clean());
        subcommands.add(new Remove());
        subcommands.add(new check());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        if (player.hasPermission(getSubcommands().get(i).getPermission()) || player.hasPermission("mm.admin") || player.isOp()) {
                            getSubcommands().get(i).perform(player, args);
                        } else {
                            player.sendMessage(Messages.chatMessage("permission").replace("%this%", getSubcommands().get(i).getPermission()));
                        }
                    }
                }
            } else {
                MetaMorph metaMorph = MetaMorph.getInstance();
                ModelManager modelManager = metaMorph.getModelManager();
                ItemStack item = player.getInventory().getItemInMainHand();
                String name = item.getType().name();

                boolean hasPermission = false;
                boolean isOpOrAdmin = player.isOp() || player.hasPermission("mm.admin");

                if (modelManager.defined(name)) {
                    String basePermission = "mm." + name.toLowerCase() + ".";
                    hasPermission = isOpOrAdmin || player.getEffectivePermissions().stream()
                            .anyMatch(pai -> pai.getPermission().startsWith(basePermission));
                } else {
                    player.sendMessage(Messages.chatMessage("error_missing_item"));
                    return false;
                }

                if (!hasPermission) {
                    player.sendMessage(Messages.chatMessage("error_missing_item"));
                    return false;
                } else {
                    MenuUtilities menuUtilities = metaMorph.menuUtilities(player);
                    menuUtilities.setItem(player.getItemInHand());
                    new ModelMenu(metaMorph.menuUtilities(player)).open();
                }
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