package me.albus.metamorph.CommandManager;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommands {
    public abstract String getName();
    public abstract String getPermission();

    public abstract String getSyntax();
    @SuppressWarnings("customWarningCode")
    public abstract void perform(Player player, String[] args);
    @SuppressWarnings("unused")
    public abstract List<String> getSubcommandArguments(Player player, String[] args);

}
