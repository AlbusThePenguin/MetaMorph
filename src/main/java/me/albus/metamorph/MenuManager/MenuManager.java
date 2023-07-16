 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.MenuManager;

import me.albus.metamorph.MetaMorph;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class MenuManager implements InventoryHolder {
    protected MenuUtilities menuUtilities;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS;
    public MenuManager(MenuUtilities menuUtilities) {
        this.menuUtilities = menuUtilities;
        YamlConfiguration config = MetaMorph.getInstance().config().get();
        String name = config.getString("GUI.pagination.filler_item.display");
        if(name == null) {
            name = " ";
        }

        String mat = config.getString("GUI.pagination.filler_item.material");
        if(mat == null || mat.isEmpty()) {
            mat = "GRAY_STAINED_GLASS_PANE";
        }

        Material material = Material.valueOf(mat);
        FILLER_GLASS = makeItem(material, name);
    }
    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract void handleMenu(InventoryClickEvent e);
    public abstract void setMenuItems();
    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        menuUtilities.getOwner().openInventory(inventory);
    }
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    public void setFillerGlass() {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }
    @SuppressWarnings("ConstantConditions")
    public ItemStack makeItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }
}
