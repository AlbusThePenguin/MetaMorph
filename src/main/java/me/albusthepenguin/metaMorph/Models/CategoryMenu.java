package me.albusthepenguin.metaMorph.Models;

import me.albusthepenguin.metaMorph.Menu.Menu;
import me.albusthepenguin.metaMorph.Menu.MenuUtilities;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class CategoryMenu extends Menu {

    private final ModelHandler modelHandler;

    private final int maxPages;

    private final NamespacedKey namespacedKey;

    public CategoryMenu(Plugin plugin, MenuUtilities menuUtilities, ModelHandler modelHandler) {
        super(plugin, menuUtilities);
        this.modelHandler = modelHandler;

        this.maxPages = 50;

        this.namespacedKey = new NamespacedKey(super.plugin, "metamorph_category");
    }

    @Override
    public String getMenuName() {
        return "&bSelect Model Category";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void onMenuClick(InventoryClickEvent event) {
        event.setCancelled(true);

        int clicked = event.getSlot();

        Player player = (Player) event.getWhoClicked();

        if (clicked == 0) { // Previous Page
            if (this.page <= 0) {
                player.sendMessage(super.color("&cYou're already on the first page."));
                return;
            }
            this.page -= 1;
            super.changePage(player);
        } else if (clicked == 8) {
            if (this.page >= this.maxPages) {
                player.sendMessage(super.color("&cYou're already on the last page."));
                return;
            }
            this.page += 1;
            super.changePage(player);
        } else if(clicked == 53) {
            player.getOpenInventory().close();
        } else {

            ItemStack itemStack = event.getCurrentItem();
            if(itemStack == null) {
                this.plugin.getLogger().info("Clicked [0]");
                return;
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemMeta == null) {
                this.plugin.getLogger().info("Clicked [1]");
                return;
            }

            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            String categoryID = container.get(this.namespacedKey, PersistentDataType.STRING);
            if(categoryID == null) {
                this.plugin.getLogger().info("Clicked [2]");
                return;
            }

            Category categoryClicked = this.modelHandler.getCategory(categoryID);

            if(categoryClicked == null) {
                super.plugin.getLogger().severe(categoryID + " is not a valid category. Please check configs.yml.");
                return;
            }

            ModelMenu modelMenu = new ModelMenu(this.plugin, this.menuUtilities, categoryClicked, this.modelHandler);
            modelMenu.open();
            this.plugin.getLogger().info("Opening Model Menu.");
        }
    }

    @Override
    public void onMenuClose(InventoryCloseEvent event) {

    }

    private final int[] filtered = new int[]{1,2,3,4,5,6,7,9,17,18,27,26,35,36,44,45,46,47,48,49,50,51,52};

    /**
     * This needs testing.
     */
    @Override
    public void setMenuItems() {
        this.inventory.setItem(0, super.prevPage);
        this.inventory.setItem(8, super.nextPage);
        this.inventory.setItem(53, super.close);
        for(int i : this.filtered) super.inventory.setItem(i, super.filter);

        Collection<Category> categories = modelHandler.getCategories();

        if(categories.isEmpty()) return;

        int i = 0;
        for (Category category : categories) {
            this.index = super.maxItemsPerPage * page + i;

            if (i >= super.maxItemsPerPage) {
                break;
            }

            if (this.index >= categories.size()) {
                break;
            }

            ItemStack itemStack = category.getItemStack();

            this.inventory.addItem(itemStack);

            i++;
        }
    }
}
