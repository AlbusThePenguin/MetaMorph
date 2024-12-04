package me.albusthepenguin.metaMorph.Commands;

import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.Configs.Configuration;
import me.albusthepenguin.metaMorph.MetaMorph;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class reloadCommand extends MinecraftSubCommand {

    protected reloadCommand(MetaMorph metaMorph, String name, String permission, String syntax) {
        super(metaMorph, name, permission, syntax);
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length != 1) {
            player.sendMessage(super.getMessage().get("wrong-syntax", Map.of("{syntax}", super.getSyntax()), true));
            return;
        }

        Configuration configuration = super.getMetaMorph().getConfiguration();

        configuration.reload(ConfigType.Messages);
        configuration.save(ConfigType.Messages);

        configuration.reload(ConfigType.Config);
        configuration.save(ConfigType.Messages);

        player.sendMessage(super.getMessage().get("reload-command", null, true));
    }

    @Override
    public void perform(ConsoleCommandSender console, String[] args) {
        if(args.length != 1) {
            console.sendMessage("Wrong syntax! Please use: " + super.getSyntax());
            return;
        }

        Configuration configuration = super.getMetaMorph().getConfiguration();

        configuration.reload(ConfigType.Messages);
        configuration.save(ConfigType.Messages);

        configuration.reload(ConfigType.Config);
        configuration.save(ConfigType.Messages);

        console.sendMessage("Reloaded config.yml and messages.yml");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return List.of();
    }
}
