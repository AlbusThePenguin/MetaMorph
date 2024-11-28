package me.albusthepenguin.metaMorph.Menu;

import me.albusthepenguin.metaMorph.Models.Category;
import me.albusthepenguin.metaMorph.Models.ModelHandler;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class CategoryMenu extends Menu {

    private final ModelHandler modelHandler;

    //Todo:
    private final int maxPages;

    public CategoryMenu(Plugin plugin, MenuUtilities menuUtilities, ModelHandler modelHandler) {
        super(plugin, menuUtilities);
        this.modelHandler = modelHandler;

        this.maxPages = 50;
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
        event.getWhoClicked().sendMessage("Hey gurl!");
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

        Collection<Category> categories = modelHandler.getCategories().values();

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
