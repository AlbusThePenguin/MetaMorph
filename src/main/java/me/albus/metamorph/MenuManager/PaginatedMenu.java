package me.albus.metamorph.MenuManager;

import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.config.Messages;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class PaginatedMenu extends MenuManager {
    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    public PaginatedMenu(MenuUtilities menuUtilities) {
        super(menuUtilities);
    }
    @SuppressWarnings("ConstantConditions")
    public void addMenuBorder(){
        YamlConfiguration config = MetaMorph.getInstance().config().get();

        Material nex = Material.valueOf(config.getString("GUI.pagination.next_button.material"));
        if(nex == null) {
            nex = Material.LEVER;
        }

        Material prev = Material.valueOf(config.getString("GUI.pagination.prev_button.material"));
        if(prev == null) {
            prev = Material.LEVER;
        }

        Material clean = Material.valueOf(config.getString("GUI.pagination.clean.material"));
        if(clean == null) {
            clean = Material.SPONGE;
        }


        String next_display = config.getString("GUI.pagination.next_button.display");
        if(next_display == null || next_display.isEmpty()) {
            next_display = "&#CC9911&lNext";
        }

        String prev_display = config.getString("GUI.pagination.prev_button.display");
        if(prev_display == null || prev_display.isEmpty()) {
            prev_display = "&#CC9911&lPrevious";
        }

        String clean_display = config.getString("GUI.pagination.clean.display");
        if(clean_display == null || clean_display.isEmpty()) {
            clean_display = "&#FF91AF&lClean Item";
        }

        ItemStack next = new ItemStack(nex);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(Messages.translateColorCodes(next_display));
        PersistentDataContainer nextContainer = nextMeta.getPersistentDataContainer();
        nextContainer.set(new NamespacedKey(MetaMorph.getInstance(), "metamorph"), PersistentDataType.STRING, "next");
        next.setItemMeta(nextMeta);
        inventory.setItem(50, next);

        ItemStack previous = new ItemStack(prev);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName(Messages.translateColorCodes(prev_display));
        PersistentDataContainer previousContainer = previousMeta.getPersistentDataContainer();
        previousContainer.set(new NamespacedKey(MetaMorph.getInstance(), "metamorph"), PersistentDataType.STRING, "previous");
        previous.setItemMeta(previousMeta);
        inventory.setItem(48, previous);

        ItemStack clearModel = new ItemStack(clean);
        ItemMeta clearModelMeta = clearModel.getItemMeta();
        clearModelMeta.setDisplayName(Messages.translateColorCodes(clean_display));
        PersistentDataContainer clearContainer = clearModelMeta.getPersistentDataContainer();
        clearContainer.set(new NamespacedKey(MetaMorph.getInstance(), "metamorph"), PersistentDataType.STRING, "clean");
        clearModel.setItemMeta(clearModelMeta);
        inventory.setItem(49, clearModel);


        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}
