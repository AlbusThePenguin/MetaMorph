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

import me.albusthepenguin.metaMorph.MetaMorph;
import me.albusthepenguin.metaMorph.Models.Model;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private final MetaMorph metaMorph;

    private final Economy economy;

    public VaultHook(MetaMorph metaMorph) {
        this.metaMorph = metaMorph;
        RegisteredServiceProvider<Economy> rsp = metaMorph.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new IllegalArgumentException("Could not find a valid Vault economy.");
        }
        this.economy = rsp.getProvider();
    }

    /**
     *
     * @param offlinePlayer that is 'paying'.
     * @param model the model.
     */
    public boolean buy(OfflinePlayer offlinePlayer, Model model) {
        double balance = economy.getBalance(offlinePlayer);
        double price = model.getPrice();
        if(balance < price) {
            return false;
        }
        this.economy.withdrawPlayer(offlinePlayer, price);
        this.metaMorph.getLuckPermsHook().givePermission(offlinePlayer, model);
        return true;
    }


}
