 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.MenuManager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class MenuUtilities {
    private final Player owner;
    private ItemStack item;
    public MenuUtilities(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }
    public ItemStack getItem() {
        return item;
    }
    public void setItem(ItemStack item) {
        this.item = item;
    }
}