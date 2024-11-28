package me.albusthepenguin.metaMorph.Hooks;

import me.albusthepenguin.metaMorph.Models.Model;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsHook {

    private final LuckPerms luckPerms;

    public LuckPermsHook() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

        if(provider == null) {
            throw new IllegalArgumentException("Could not hook properly into LuckPerms.");
        }

        this.luckPerms = provider.getProvider();
    }

    public boolean givePermission(OfflinePlayer player, Model model) {
        return getUser(player).data().add(Node.builder(model.getPermission()).build()).wasSuccessful();
    }

    public User getUser(OfflinePlayer player) {
        return this.luckPerms.getUserManager().getUser(player.getUniqueId());
    }

}
