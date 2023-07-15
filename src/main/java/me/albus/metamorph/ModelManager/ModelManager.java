package me.albus.metamorph.ModelManager;

import me.albus.metamorph.MetaMorph;
import me.albus.metamorph.config.Models;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ModelManager {
    private final MetaMorph metaMorph;
    private final YamlConfiguration modelConfig;
    private final Models models;
    public ModelManager() {
        metaMorph = MetaMorph.getInstance();
        models = metaMorph.getModels();
        modelConfig = models.getConfig();
    }

    public boolean defined(String itemName) {
        String path = "models." + itemName;
        List<String> values = modelConfig.getStringList(path);
        return !values.isEmpty();
    }
    @SuppressWarnings("ConstantConditions")
    public boolean IDExists(ItemStack item) {
        String name = item.getType().name();
        int id = item.getItemMeta().getCustomModelData();
        List<Integer> ids = modelConfig.getIntegerList("models." + name);
        return ids.contains(id);
    }

    public boolean ModelExists(ItemStack itemStack) {
        String name = itemStack.getType().name();
        String path = "models." + name;
        List<String> list = modelConfig.getStringList(path);
        return !list.isEmpty();
    }
    @SuppressWarnings("ConstantConditions")
    public void setModel(ItemStack item, int id) {
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(id);
        item.setItemMeta(meta);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @SuppressWarnings("ConstantConditions")
    public void remove(ItemStack item) {
        String name = item.getType().name();
        int id = item.getItemMeta().getCustomModelData();

        if(ModelExists(item)) {
            List<Integer> ids = modelConfig.getIntegerList("models." + name);
            ids.remove(Integer.valueOf(id));
            modelConfig.set("models." + name, ids);
            models.save();
        }
    }
    @SuppressWarnings("ConstantConditions")
    public void add(ItemStack item) {
        String name = item.getType().name();
        int id = item.getItemMeta().getCustomModelData();
        List<Integer> ids;
        if(ModelExists(item)) {
            ids = modelConfig.getIntegerList("models." + name);
            if(!ids.contains(id)) {
                ids.add(id);
                modelConfig.set("models." + name, ids);
            } else {
                Bukkit.getLogger().warning("[MetaMorph] Tried adding an ID that already exists.");
            }
        } else {
            ids = new ArrayList<>();
            ids.add(id);
            modelConfig.set("models." + name, ids);
        }
        models.save();
    }

    @SuppressWarnings("ConstantConditions")
    public List<Integer> get(ItemStack item) {
        List<Integer> ids = new ArrayList<>();
        String name = item.getType().name();
        String path = "models." + name;
        List<String> list = modelConfig.getStringList(path);
        if(list != null && !list.isEmpty()) {
            for(String value : list) {
                try {
                    ids.add(Integer.parseInt(value));
                } catch(NumberFormatException e) {
                    Bukkit.getLogger().severe("Cannot find the models because " + e.getMessage());
                }
            }
        }
        return ids;
    }
}
