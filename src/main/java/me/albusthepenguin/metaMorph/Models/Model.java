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

import lombok.Getter;
import lombok.NonNull;
import me.albusthepenguin.metaMorph.MetaMorph;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.List;

@Getter
public class Model {

    private final MetaMorph metaMorph;

    private final ItemStack itemStack;

    private final String permission;

    private final String id;

    private final double price;

    private final String displayName;

    private final int model;

    private final List<String> lore;

    public Model(MetaMorph metaMorph, @NonNull ConfigurationSection section) {
        this.metaMorph = metaMorph;
        this.id  = section.getName();

        String materialName = section.getString("material");
        if(materialName == null) {
            throw new IllegalArgumentException(this.id + "'s material is null.");
        }

        Material material = Material.getMaterial(materialName);
        if(material == null) {
            throw new IllegalArgumentException(this.id + " has " + materialName + " which is not a valid material.");
        }

        this.itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        this.model = section.getInt("model-id", 0);
        itemMeta.setCustomModelData(model);

        this.permission = section.getString("permission", "none");

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(this.metaMorph, "metamorph_model"), PersistentDataType.STRING, this.id);

        this.displayName = section.getString("display-name");
        if(displayName == null) {
            throw new IllegalArgumentException(this.id + " do not have a valid 'display-name'.");
        }
        itemMeta.setDisplayName(this.metaMorph.getMessage().setColor(displayName));

        this.lore = section.getStringList("lore").stream()
                .map(this.metaMorph.getMetaMorph().getMessage()::setColor)
                .toList();

        this.price = section.getDouble("price");

        this.itemStack.setItemMeta(itemMeta);
        this.metaMorph.getLogger().info("Loading model [" + this.id + "] ...");
    }

}
