package me.albus.metamorph.CommandManager.commands;

import me.albus.metamorph.CommandManager.SubCommands;
import me.albus.metamorph.MenuManager.MenuUtilities;
import me.albus.metamorph.MenuManager.Menus.ModelMenu;
import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.ModelManager.ModelManager;
import me.albus.metamorph.config.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Set extends SubCommands {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getPermission() {
        return "mm.player";
    }

    @Override
    public String getSyntax() {
        return "/mm set";
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void perform(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(Messages.chatMessage("syntax").replace("%this%", getSyntax()));
            player.sendMessage(Messages.chatMessage("command_set_description"));
            return;
        }

        MetaMorph metaMorph = MetaMorph.getInstance();

        ModelManager modelManager = metaMorph.getModelManager();

        ItemStack item = player.getInventory().getItemInMainHand();
        String name = item.getType().name();

        boolean hasPermission = false;

        if (modelManager.defined(name)) {
            String basePermission = "mm." + name.toLowerCase() + ".";
            hasPermission = player.getEffectivePermissions().stream()
                    .anyMatch(pai -> pai.getPermission().startsWith(basePermission));
        } else {
            player.sendMessage(Messages.chatMessage("error_missing_item"));
            return;
        }

        if (!hasPermission) {
            player.sendMessage(Messages.chatMessage("error_missing_item"));
            return;
        } else {
            MenuUtilities menuUtilities = metaMorph.menuUtilities(player);
            menuUtilities.setItem(player.getItemInHand());
            new ModelMenu(metaMorph.menuUtilities(player)).open();
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
