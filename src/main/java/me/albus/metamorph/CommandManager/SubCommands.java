 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
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
