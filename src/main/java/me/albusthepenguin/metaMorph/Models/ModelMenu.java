package me.albusthepenguin.metaMorph.Menu;

import me.albusthepenguin.metaMorph.Models.Category;
import me.albusthepenguin.metaMorph.Models.Model;
import me.albusthepenguin.metaMorph.Models.ModelHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class ModelMenu extends Menu {

    private final Category category;

    private final ModelHandler modelHandler;

    public ModelMenu(Plugin plugin, MenuUtilities menuUtilities, Category category, ModelHandler modelHandler) {
        super(plugin, menuUtilities);
        this.category = category;
        this.modelHandler = modelHandler;
    }

    @Override
    public String getMenuName() {
        return "";
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public void onMenuClick(InventoryClickEvent event) {
        this.inventory.setItem(0, super.prevPage);
        this.inventory.setItem(8, super.nextPage);
        this.inventory.setItem(53, super.close);

        Collection<Model> models = modelHandler.getModels().get(this.category);

        if(models.isEmpty()) return;

        int i = 0;
        for (Model model : models) {
            this.index = super.maxItemsPerPage * page + i;

            if (i >= super.maxItemsPerPage) {
                break;
            }

            if (this.index >= models.size()) {
                break;
            }

            ItemStack itemStack = model.getItemStack();

            this.inventory.addItem(itemStack);

            i++;
        }


    }

    @Override
    public void onMenuClose(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {

    }

    private final int[] filtered = new int[]{1,2,3,4,5,6,7,9,17,18,27,26,35,36,44,45,46,47,48,49,50,51,52};
}
