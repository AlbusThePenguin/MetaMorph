 /* This file is part of MetaMorph by AlbusThePenguin (Albus)
  * MetaMorph is open-source software licensed under the MIT License.
  * For details, see the LICENSE file or visit https://github.com/pricelessdev/MetaMorph/blob/master/MIT%20License*/
package me.albus.metamorph.MenuManager.Menus;

import me.albus.metamorph.MenuManager.MenuUtilities;
import me.albus.metamorph.MenuManager.PaginatedMenu;
import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.ModelManager.ModelManager;
import me.albus.metamorph.config.Messages;
import org.bukkit.Bukkit;
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

    private ModelManager modelManager;
    public ModelMenu(MenuUtilities menuUtilities) {
        super(menuUtilities);

        metaMorph = MetaMorph.getInstance();

        modelManager = metaMorph.getModelManager();
    }

    @Override
    public String getMenuName() {
        String name = metaMorph.config().get().getString("GUI.model_menu.title");
        if(name == null || name.isEmpty()) {
            name = "&b&lSelect Model";
        }
        return Messages.translateColorCodes(name);
    }

    @Override
    public int getSlots() {
        int slots = metaMorph.config().get().getInt("GUI.model_menu.size");
        if(slots == 0) {
            slots = 54;
        }
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack clicked = e.getCurrentItem();

        Player player = (Player) e.getWhoClicked();

        if(clicked == null || clicked.getType() == Material.AIR || !clicked.hasItemMeta()) {
            return;
        }

        ItemMeta meta = clicked.getItemMeta();
        if(meta == null) {
            return;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String click = container.get(new NamespacedKey(metaMorph.getInstance(), "metamorph"), PersistentDataType.STRING);

        if(click == null || click.isEmpty()) {
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        List<Integer> ids = modelManager.get(hand);
        if(ids == null || ids.isEmpty()) {
            return;
        }

        if(modelManager.isNumeric(click)) {
            modelManager.setModel(hand, Integer.valueOf(click));
            player.sendMessage(Messages.chatMessage("gui_button_model_click"));
            player.closeInventory();
        } else {
            switch (click) {
                case "next":
                    if (!((index + 1) >= ids.size())){
                        page = page + 1;
                        super.open();
                    }
                    break;
                case "previous":
                    if(page != 0){
                        page = page - 1;
                        super.open();
                    }
                    break;
                case "clean": //todo: more than model is copied over which is an issue. maybe copy their item and add the model.
                    ItemMeta handMeta = hand.getItemMeta();
                    handMeta.clone();
                    handMeta.setCustomModelData(0);
                    hand.setItemMeta(handMeta);
                    player.sendMessage(Messages.chatMessage("gui_button_clean_click"));
                    player.closeInventory();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        ItemStack player_item = menuUtilities.getItem();
        ItemStack item = player_item.clone();
        Player player = menuUtilities.getOwner();
        List<Integer> ids = modelManager.get(item);
        if(ids != null && !ids.isEmpty()) {
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                int index = getMaxItemsPerPage() * page + i;
                if(index >= ids.size()) {
                    break;
                }

                int id = ids.get(index);

                if(player.hasPermission("mm." + item.getType().name() + "." + id)) {
                    ItemStack dupe = new ItemStack(Material.valueOf(item.getType().name()));
                    ItemMeta dMeta = dupe.getItemMeta();
                    dMeta.setCustomModelData(id);

                    String display = metaMorph.config().get().getString("GUI.items.display");
                    if(display == null || display.isEmpty()) {
                        display = "&#00FF00&lSelect This Model";
                    }

                    if(player.hasPermission("mm.admin")) {
                        display += display + "&e&l(&6&l" + id + "&e&l)";
                    }

                    dMeta.setDisplayName(Messages.translateColorCodes(display));
                    PersistentDataContainer dataContainer = dMeta.getPersistentDataContainer();
                    dataContainer.set(new NamespacedKey(metaMorph, "metamorph"), PersistentDataType.STRING, String.valueOf(id));
                    dupe.setItemMeta(dMeta);
                    inventory.addItem(dupe);
                }
            }
        }
    }
}
