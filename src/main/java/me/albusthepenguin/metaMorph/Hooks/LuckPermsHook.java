/*
 * This file is part of MetaMorph.
 *
 * MetaMorph is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MetaMorph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with MetaMorph. If not, see <http://www.gnu.org/licenses/>.
 */
package me.albusthepenguin.metaMorph.Hooks;

import lombok.NonNull;
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

    public boolean givePermission(@NonNull OfflinePlayer player, @NonNull Model model) {
        String permission = model.getPermission();
        User user = getUser(player);
        if(user == null) return false;

        if(givePermission(user, permission)) {
            save(user);
            return true;
        }
        return false;
    }

    private boolean givePermission(User user, String permission) {
        return user.data().add(Node.builder(permission).build()).wasSuccessful();
    }

    private void save(User user) {
        this.luckPerms.getUserManager().saveUser(user);
    }

    public User getUser(OfflinePlayer player) {
        return this.luckPerms.getUserManager().getUser(player.getUniqueId());
    }

}
