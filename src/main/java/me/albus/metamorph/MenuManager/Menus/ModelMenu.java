package me.albus.metamorph.MenuManager.Menus;

import me.albus.metamorph.MenuManager.MenuUtilities;
import me.albus.metamorph.MenuManager.PaginatedMenu;
import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.ModelManager.ModelManager;
import me.albus.metamorph.config.Messages;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ModelMenu extends PaginatedMenu {

    private MetaMorph metaMorph;
    public ModelMenu(MenuUtilities menuUtilities) {
        super(menuUtilities);

        metaMorph = MetaMorph.getInstance();
    }

    @Override
    public String getMenuName() {
        return null;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        ItemStack item = menuUtilities.getItem();

        ModelManager modelManager = metaMorph.getModelManager();

        List<Integer> ids = modelManager.get(item);
        if(ids != null && !ids.isEmpty()) {
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                int index = getMaxItemsPerPage() * page + i;
                if(index >= ids.size()) {
                    break;
                }

                int id = ids.get(index);
                Player player = menuUtilities.getOwner();

                if(player.hasPermission("mm." + item.getType().name() + "." + id)) {
                    ItemStack dupe = new ItemStack(Material.valueOf(item.getType().name()));
                    ItemMeta meta = dupe.getItemMeta();
                    meta.setCustomModelData(id);
                    meta.setDisplayName(Messages.translateColorCodes("&bSelect Skin"));
                    PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                    dataContainer.set(new NamespacedKey(metaMorph, "metamorph"), PersistentDataType.STRING, String.valueOf(id));
                    item.setItemMeta(meta);
                    inventory.addItem(item);
                }
            }
        }
    }
}
