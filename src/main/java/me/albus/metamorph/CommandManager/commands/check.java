package me.albus.metamorph.CommandManager.commands;

import me.albus.metamorph.CommandManager.SubCommands;
import me.albus.metamorph.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class check extends SubCommands {
    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String getPermission() {
        return "mm.admin";
    }

    @Override
    public String getSyntax() {
        return "/mm check - Checks the model ID of the item in your hand.";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length != 1) {
            player.sendMessage(Messages.chatMessage("syntax").replace("%this%", getSyntax()));
            player.sendMessage(Messages.chatMessage("command_check_description"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().isItem() || !item.hasItemMeta()) {
            player.sendMessage(Messages.chatMessage("command_check_error_invalid"));
            player.sendMessage(Messages.chatMessage("command_check_description"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            player.sendMessage(Messages.chatMessage("command_check_error_invalid"));
            player.sendMessage(Messages.chatMessage("command_check_description"));
            return;
        }

        if(item.getType() == Material.AIR) {
            player.sendMessage(Messages.chatMessage("command_check_error_invalid"));
            player.sendMessage(Messages.chatMessage("command_check_description"));
            return;
        }

        if(!meta.hasCustomModelData()) {
            player.sendMessage(Messages.chatMessage("command_check_error_invalid"));
            player.sendMessage(Messages.chatMessage("command_check_description"));
            return;
        }

        int modelID = meta.getCustomModelData();
        if(modelID < 1) {
            player.sendMessage(Messages.chatMessage("command_check_error_invalid"));
            player.sendMessage(Messages.chatMessage("command_check_description"));
            return;
        }

        player.sendMessage(Messages.chatMessage("command_check_success").replace("%this%", String.valueOf(modelID)).replace("%item%", item.getType().name()));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
