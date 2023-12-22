package me.albus.metamorph.Listeners;

import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.ModelManager.ModelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemListener implements Listener {

    @EventHandler
    public void inventory(InventoryDragEvent event) {
        if(event.getInventory().getHolder() instanceof Player) {
            ItemStack item = ((Player) event.getInventory().getHolder()).getItemOnCursor();

            if(!item.hasItemMeta()) {
                return;
            }

            ItemMeta meta = item.getItemMeta();
            if(meta == null) {
                return;
            }

            if(!meta.hasCustomModelData()) {
                return;
            }

            int model = meta.getCustomModelData();

            if(model < 1) {
                return;
            }

            ModelManager modelManager = MetaMorph.getInstance().getModelManager();
            if(modelManager.ModelExists(item)) {
                String permission = "mm." + item.getType().name() + "." + model;
                Player player = (Player) event.getInventory().getHolder();
                if(player == null) {
                    return;
                }

                if(!player.hasPermission(permission)) {
                    event.setCancelled(true);
                    player.sendMessage("You can't carry this item.");
                }
            }
        }
    }

    @EventHandler
    public void pickup(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player) {
            ItemStack item = (ItemStack) event.getItem();

            if(!item.hasItemMeta()) {
                return;
            }

            ItemMeta meta = item.getItemMeta();
            if(meta == null) {
                return;
            }

            if(!meta.hasCustomModelData()) {
                return;
            }

            int model = meta.getCustomModelData();

            if(model < 1) {
                return;
            }

            ModelManager modelManager = MetaMorph.getInstance().getModelManager();
            if(modelManager.ModelExists(item)) {
                String permission = "mm." + item.getType().name() + "." + model;
                Player player = ((Player) event.getEntity()).getPlayer();
                if(player == null) {
                    return;
                }

                if(!player.hasPermission(permission)) {
                    event.setCancelled(true);
                    player.sendMessage("You can't carry this item.");
                }
            }
        }
    }

}
