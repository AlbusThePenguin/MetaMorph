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

public class ModelMenu extends Menu {

    private final Category category;

    private final ModelHandler modelHandler;

    private final int maxPages;

    private final NamespacedKey namespacedKey;

    public ModelMenu(Plugin plugin, MenuUtilities menuUtilities, Category category, ModelHandler modelHandler) {
        super(plugin, menuUtilities);
        this.category = category;
        this.modelHandler = modelHandler;

        this.maxPages = 50;

        this.namespacedKey = new NamespacedKey(super.plugin, "metamorph_model");
    }

    @Override
    public String getMenuName() {
        return "&bSelect Model";
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

        if (clicked == 0) {
            if (this.page <= 0) {
                player.sendMessage(super.color("&cYou're already on the first page."));
                return;
            }
            this.page -= 1;
            this.changePage(player);
        } else if (clicked == 8) {
            if (this.page >= this.maxPages) {
                player.sendMessage(super.color("&cYou're already on the last page."));
                return;
            }
            this.page += 1;
            this.changePage(player);
        } else if(clicked == 53) {
            player.getOpenInventory().close();
        } else {

            ItemStack itemStack = event.getCurrentItem();
            if(itemStack == null) {
                this.plugin.getLogger().info("ItemStack is null.");
                return;
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemMeta == null) {
                this.plugin.getLogger().info("Item Meta is null-");
                return;
            }

            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            String modelID = container.get(this.namespacedKey, PersistentDataType.STRING);
            if(modelID == null) {
                this.plugin.getLogger().info("Model is null.");
                return;
            }

            Model modelClicked = this.modelHandler.getModel(modelID);

            if(modelClicked == null) {
                super.plugin.getLogger().severe(modelID + " is not a valid category. Please check configs.yml.");
                return;
            }

            //Todo: handle the model?
            this.modelHandler.getMetaMorph().getPreview().spawn(player, modelClicked); //I guess we try?
        }
    }

    @Override
    public void onMenuClose(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        this.inventory.setItem(0, super.prevPage);
        this.inventory.setItem(8, super.nextPage);
        this.inventory.setItem(53, super.close);
        for(int i : this.filtered) super.inventory.setItem(i, super.filter);

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

    private final int[] filtered = new int[]{1,2,3,4,5,6,7,9,17,18,27,26,35,36,44,45,46,47,48,49,50,51,52};
}
