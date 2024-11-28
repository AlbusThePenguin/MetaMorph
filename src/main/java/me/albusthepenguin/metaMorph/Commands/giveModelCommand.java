package me.albusthepenguin.metaMorph.Commands;

import me.albusthepenguin.metaMorph.Message;
import me.albusthepenguin.metaMorph.MetaMorph;
import me.albusthepenguin.metaMorph.Models.Category;
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
        if(super.getMetaMorph().getLuckPermsHook() == null) {
            player.sendMessage(Message.setColor("&cThis command is not usable."));
            return;
        }

        if(args.length != 3) {
            player.sendMessage(Message.setColor(this.getSyntax()));
            return;
        }

        OfflinePlayer target = Bukkit.getPlayer(args[1]);
        if(target == null || !target.hasPlayedBefore()) {
            player.sendMessage(Message.setColor("&cCould not find &e" + args[1] + "&c online!"));
            return;
        }

        String modelName = args[2];

        Model model = null;

        for(Map.Entry<Category, List<Model>> entry : this.getMetaMorph().getModelHandler().getModels().entrySet()) {
            List<Model> models = entry.getValue();
            for(Model modelIndex : models) {
                if(modelIndex.getId().equalsIgnoreCase(modelName)) {
                    model = modelIndex;
                }
            }
        }

        if(model == null) {
            player.sendMessage(Message.setColor("&cCould not find a model called " + modelName));
        } else {
            boolean worked = super.getMetaMorph().getLuckPermsHook().givePermission(target, model);
            if (worked) {
                player.sendMessage(Message.setColor("&aGave &e" + target.getName() + " the model &e" + model.getDisplayName()));
            } else {
                player.sendMessage(Message.setColor("&cSomething went wrong!"));
            }
        }
    }

    @Override
    public void perform(ConsoleCommandSender console, String[] args) {

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return List.of();
    }
}
