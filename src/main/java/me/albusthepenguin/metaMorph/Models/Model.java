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

@Getter
public class Model {

    private final Plugin plugin;

    private final ItemStack itemStack;

    private final String permission;

    private final String id;

    private final double price;

    private final String displayName;

    public Model(Plugin plugin, @NonNull ConfigurationSection section) {
        this.plugin = plugin;
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

        int model = section.getInt("model-id", 0);
        itemMeta.setCustomModelData(model);

        //Todo: do the permission check for 'none'.
        this.permission = section.getString("permission", "none");

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(this.plugin, "metamorph_model"), PersistentDataType.STRING, this.id);

        this.displayName = section.getString("display-name");
        if(displayName == null) {
            throw new IllegalArgumentException(this.id + " do not have a valid 'display-name'.");
        }
        itemMeta.setDisplayName(Message.setColor(displayName));

        List<String> lore = section.getStringList("lore").stream()
                .map(Message::setColor)
                .toList();

        itemMeta.setLore(lore);

        this.price = section.getDouble("price");

        this.itemStack.setItemMeta(itemMeta);
    }

}
