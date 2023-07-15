package me.albus.metamorph.CommandManager.commands;

import me.albus.metamorph.CommandManager.SubCommands;
import me.albus.metamorph.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Clean extends SubCommands {
    @Override
    public String getName() {
        return "clean";
    }

    @Override
    public String getPermission() {
        return "mm.player";
    }

    @Override
    public String getSyntax() {
        return "/mm clean";
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void perform(Player player, String[] args) {
        if(args.length != 1) {
            player.sendMessage(Messages.chatMessage("syntax").replace("%this%", getSyntax()));
            player.sendMessage(Messages.chatMessage("command_clean_description"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if(item == null) {
            player.sendMessage(Messages.chatMessage("command_error_item_null"));
            player.sendMessage(Messages.chatMessage("command_clean_description"));
            return;
        }

        if(item.getType() == Material.AIR) {
            player.sendMessage(Messages.chatMessage("command_error_item_air"));
            player.sendMessage(Messages.chatMessage("command_clean_description"));
            return;
        }

        if(meta == null || meta.hasCustomModelData()) {
            player.sendMessage(Messages.chatMessage("command_error_item_no_model"));
            player.sendMessage(Messages.chatMessage("command_clean_description"));
            return;
        }

        meta.clone();
        meta.setCustomModelData(0);
        item.setItemMeta(meta);
        player.sendMessage(Messages.chatMessage("command_clean_success"));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
