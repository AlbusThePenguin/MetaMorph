/*
 * This file is part of MetaMorph.
 *
 * MetaMorph is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MetaMorph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with MetaMorph. If not, see <http://www.gnu.org/licenses/>.
 */
package me.albusthepenguin.metaMorph.Models;

import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.Menu.Menu;
import me.albusthepenguin.metaMorph.Menu.MenuUtilities;
import me.albusthepenguin.metaMorph.MetaMorph;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelMenu extends Menu {

    private final ModelHandler modelHandler;

    private final int maxPages;

    private final NamespacedKey namespacedKey;

    private final List<Model> models;

    public ModelMenu(MetaMorph metaMorph, MenuUtilities menuUtilities, ModelHandler modelHandler, List<Model> models) {
        super(metaMorph, menuUtilities);
        this.modelHandler = modelHandler;

        this.models = models;

        this.maxPages = getMP(models.size());

        this.namespacedKey = new NamespacedKey(super.metaMorph, "metamorph_model");
    }

    private int getMP(int itemAmount) {
        int maxPerPage = super.maxItemsPerPage;
        return (itemAmount + maxPerPage - 1) / maxPerPage; // This is an efficient way to calculate the ceiling of division
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
                player.sendMessage(super.message.get("page-first", null, true));
                super.playNope(player);
                return;
            }
            this.page -= 1;
            this.changePage(player);
        } else if (clicked == 8) {
            if (this.page >= this.maxPages) {
                player.sendMessage(super.message.get("page-last", null, true));
                super.playNope(player);
                return;
            }
            this.page += 1;
            this.changePage(player);
        } else if (clicked == 53) {
            player.getOpenInventory().close();
        } else {

            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null) {
                super.metaMorph.getLogger().info("ItemStack is null.");
                super.playNope(player);
                return;
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) {
                super.metaMorph.getLogger().info("Item Meta is null-");
                super.playNope(player);
                return;
            }

            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            String modelID = container.get(this.namespacedKey, PersistentDataType.STRING);
            if (modelID == null) {
                super.metaMorph.getLogger().info("Model is null.");
                super.playNope(player);
                return;
            }

            Model model = this.modelHandler.getModels().get(modelID);

            if (model == null) {
                super.metaMorph.getLogger().severe(modelID + " is not a valid model. Please check configs.yml.");
                super.playNope(player);
                return;
            }

            ClickType clickType = event.getClick();

            if (clickType == ClickType.SHIFT_LEFT) {
                if (this.modelHandler.getMetaMorph().getPreview().spawn(player, model)) {
                    player.getOpenInventory().close();
                    super.playYes(player);
                } else {
                    super.playNope(player);
                }
            } else if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) {
                if (hasPermission(player, model)) {
                    setModel(player, model);
                    super.playYes(player);
                    player.getOpenInventory().close();
                } else {
                    String display = model.getDisplayName();
                    String price = String.valueOf(model.getPrice());

                    if (this.modelHandler.getMetaMorph().getVaultHook().buy(player, model)) {
                        player.sendMessage(super.message.get("buy-success", Map.of("{name}", display, "{price}", price), true));
                        super.playYes(player);
                        super.open();
                    } else {
                        player.sendMessage(super.message.get("buy-fail", Map.of("{name}", display, "{price}", price), true));
                        super.playNope(player);
                    }
                }
            }
        }
    }

    private void setModel(Player player, Model model) {
        ItemStack inHand = player.getInventory().getItemInMainHand();
        ItemMeta handMeta = inHand.getItemMeta();
        assert handMeta != null;
        handMeta.setCustomModelData(model.getModel());
        inHand.setItemMeta(handMeta);
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

        Player player = super.menuUtilities.getPlayer();

        int i = 0;
        for (Model model : this.models) {
            this.index = super.maxItemsPerPage * page + i;

            if (i >= super.maxItemsPerPage) {
                break;
            }

            if (this.index >= models.size()) {
                break;
            }

            this.inventory.addItem(addOwned(player, model));

            i++;
        }
    }

    private boolean hasPermission(Player player, Model model) {
        if(player.hasPermission(model.getPermission())) return true;
        return model.getPermission().equalsIgnoreCase("none");
    }

    private ItemStack addOwned(Player player, Model model) {
        ItemStack itemStack = Optional.ofNullable(model.getItemStack()).orElse(new ItemStack(Material.BARRIER));
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;

        List<String> lore = Stream.concat(
                Optional.ofNullable(model.getLore()).orElseGet(ArrayList::new).stream(),
                getLore(player, model).stream()
        ).collect(Collectors.toList());

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private List<String> getLore(Player player, Model model) {
        ConfigurationSection section = this.modelHandler.getMetaMorph()
                .getConfiguration()
                .getConfig(ConfigType.Config)
                .getConfigurationSection("lore-settings");

        if (section == null) {
            return Collections.emptyList();
        }

        List<String> rawLore = hasPermission(player, model)
                ? section.getStringList("owned")
                : section.getStringList("unowned");

        return rawLore.stream()
                .map(line -> {
                    line = line.replace("{item-display}", model.getDisplayName());
                    line = line.replace("{price}", String.valueOf(model.getPrice()));
                    return super.message.setColor(line);
                })
                .collect(Collectors.toList());
    }

    private final int[] filtered = new int[]{1,2,3,4,5,6,7,9,17,18,27,26,35,36,44,45,46,47,48,49,50,51,52};
}
