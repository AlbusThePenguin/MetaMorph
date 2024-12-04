/*
 * This file is part of Lockers.
 *
 * Lockers is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lockers is a distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Lockers. If not, see <http://www.gnu.org/licenses/>.
 */
package me.albusthepenguin.metaMorph.Menu;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import me.albusthepenguin.metaMorph.Message;
import me.albusthepenguin.metaMorph.MetaMorph;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

@SuppressWarnings("unused")
@Getter
public abstract class Menu implements InventoryHolder {

    protected MetaMorph metaMorph;

    protected Message message;

    protected MenuUtilities menuUtilities;

    protected Inventory inventory;

    protected NamespacedKey key;

    protected final ItemStack nextPage;

    protected final ItemStack prevPage;

    protected final ItemStack close;

    protected final ItemStack filter;

    protected int page = 0;

    @Getter
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    public Menu(MetaMorph metaMorph, MenuUtilities menuUtilities) {
        this.metaMorph = metaMorph;
        this.message = this.metaMorph.getMessage();
        this.key = new NamespacedKey(metaMorph, "clicked");
        this.menuUtilities = menuUtilities;

        this.nextPage = new ItemStack(Material.ARROW);
        this.prevPage = new ItemStack(Material.ARROW);
        this.close = new ItemStack(Material.ACACIA_DOOR);
        this.filter = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void onMenuClick(InventoryClickEvent event);

    public abstract void onMenuClose(InventoryCloseEvent event);

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.getServer().createInventory(this, getSlots(), this.message.setColor(getMenuName()));

        this.setMenuItems();
        menuUtilities.getPlayer().openInventory(inventory);
    }

    @Override
    public @NonNull Inventory getInventory() {
        return inventory;
    }

    public void setFillers(ItemStack filler) {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
    }

    public void changePage(Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
        this.open();
    }

    public void playNope(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
    }

    public void playYes(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    private final Gson gson = new Gson();

    @NonNull
    private PlayerProfile getPlayerProfile(@NonNull final String base64Url) {
        final PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());

        final String decodedBase64 = decodeSkinUrl(base64Url);
        if (decodedBase64 == null) {
            return profile;
        }

        final PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(new URL(decodedBase64));
        } catch (final MalformedURLException exception) {
            throw new IllegalArgumentException("Could not create skull because " + exception);
        }

        profile.setTextures(textures);
        return profile;
    }

    /**
     * Get the skull from a base64 encoded texture url
     *
     * @param base64Url base64 encoded url to use
     * @return skull
     */
    @NonNull
    public ItemStack getSkull(@NonNull final String base64Url) {
        final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (base64Url.isEmpty()) {
            return head;
        }

        final SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta == null) {
            return head;
        }

        final PlayerProfile profile = getPlayerProfile(base64Url);
        headMeta.setOwnerProfile(profile);
        head.setItemMeta(headMeta);
        return head;
    }

    private String decodeSkinUrl(@NonNull final String base64Texture) {
        final String decoded = new String(Base64.getDecoder().decode(base64Texture));
        final JsonObject object = gson.fromJson(decoded, JsonObject.class); // Use the Gson instance

        final JsonElement textures = object.get("textures");

        if (textures == null) {
            return null;
        }

        final JsonElement skin = textures.getAsJsonObject().get("SKIN");

        if (skin == null) {
            return null;
        }

        final JsonElement url = skin.getAsJsonObject().get("url");
        return url == null ? null : url.getAsString();
    }
}
