package me.albusthepenguin.metaMorph.Models;

import lombok.Getter;
import lombok.NonNull;
import me.albusthepenguin.metaMorph.Message;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Todo: get lombok.
 */
@Getter
public class Category {

    private final Plugin plugin;

    private final String id;

    private final ItemStack itemStack;


    public Category(Plugin plugin, @NonNull ConfigurationSection section) {
        this.plugin = plugin;
        this.id = section.getName();

        String materialName = section.getString("material");
        if(materialName == null) {
            throw new IllegalArgumentException("The material set for 'Category' " + this.id + " is null.");
        }

        Material material = Material.getMaterial(materialName);
        if(material == null) {
            throw new IllegalArgumentException("The material set for 'Category' " + this.id + " '" + materialName + "' is not a valid material");
        }

        this.itemStack = new ItemStack(material);

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(this.plugin, "metamorph_category"), PersistentDataType.STRING, this.id);

        List<String> lore = section.getStringList("lore").stream()
                .map(Message::setColor)
                .toList();

        itemMeta.setLore(lore);

        String displayName = section.getString("display-name");
        if(displayName == null) {
            throw new IllegalArgumentException("The display name for 'Category' " + this.id + " is null.");
        }

        itemMeta.setDisplayName(Message.setColor(displayName));

        int model = section.getInt("model-id", 0);
        itemMeta.setCustomModelData(model);

        this.itemStack.setItemMeta(itemMeta);
    }
}
