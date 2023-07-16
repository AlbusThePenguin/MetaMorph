 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.CommandManager.commands;

import me.albus.metamorph.CommandManager.SubCommands;
import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.ModelManager.ModelManager;
import me.albus.metamorph.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Add extends SubCommands {
    @Override
    public String getName() {
        return "add";
    }
    @Override
    public String getPermission() {
        return "mm.admin";
    }
    @Override
    public String getSyntax() {
        return "/mm add";
    }
    @Override
    @SuppressWarnings("ConstantConditions")
    public void perform(Player player, String[] args) {
        if(args.length != 1) {
           player.sendMessage(Messages.chatMessage("syntax").replace("%this%", getSyntax()));
           player.sendMessage(Messages.chatMessage("command_add_description"));
            return;
        } //Todo: check why it returns that id doesn't exists. because it shouldn't when we add it? wut.. also fix reload commands...

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item == null) {
            player.sendMessage(Messages.chatMessage("command_error_item_null"));
            player.sendMessage(Messages.chatMessage("command_add_description"));
            return;
        }

        if(item.getType() == Material.AIR) {
            player.sendMessage(Messages.chatMessage("command_error_item_air"));
            player.sendMessage(Messages.chatMessage("command_add_description"));
            return;
        }

        if(item.getItemMeta() == null || !item.getItemMeta().hasCustomModelData()) {
            player.sendMessage(Messages.chatMessage("command_error_item_no_model"));
            player.sendMessage(Messages.chatMessage("command_add_description"));
            return;
        }

        if(item.getItemMeta().getCustomModelData() <= 0) {
            player.sendMessage(Messages.chatMessage("command_error_item_model_number_invalid"));
            player.sendMessage(Messages.chatMessage("command_add_description"));
            return;
        }

        ModelManager modelManager = MetaMorph.getInstance().getModelManager();

        if(modelManager.ModelExists(item) && modelManager.IDExists(item)) {
            player.sendMessage(Messages.chatMessage("command_error_model_exists"));
            player.sendMessage(Messages.chatMessage("command_add_description"));
            return;
        }

        modelManager.add(item);
        player.sendMessage(Messages.chatMessage("command_add_success"));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
