package me.albusthepenguin.metaMorph;

import lombok.Getter;
import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.Configs.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class ModelHandler {

    private final Plugin plugin;

    @Getter
    private final Map<String, Category> categories = new HashMap<>();

    @Getter
    private final Map<Category, List<Model>> models = new HashMap<>();

    public ModelHandler(Plugin plugin, Configuration configuration) {
        this.plugin = plugin;

        ConfigurationSection categorySection = configuration.getConfig(ConfigType.Config).getConfigurationSection("Categories");
        if(categorySection == null) {
            throw new IllegalArgumentException("The 'Categories' in config.yml does not exists. Could not load categories.");
        }
        loadCategories(categorySection);

        loadModels();
    }

    private void loadCategories(@Nonnull ConfigurationSection section) {
        for(String index : section.getKeys(true)) {
            ConfigurationSection indexSection = section.getConfigurationSection(index);
            if(indexSection == null) {
                throw new IllegalArgumentException(index + " is not a valid category section. Could not load categories.");
            }
            categories.put(index, new Category(indexSection));
        }
    }

    private void loadModels() {
        File modelsDirectory = new File(plugin.getDataFolder(), "models");

        if (!modelsDirectory.exists()) {
            boolean created = modelsDirectory.mkdirs();

            File furnituresFile = new File(modelsDirectory, "furnitures.yml");
            File swordsFile = new File(modelsDirectory, "swords.yml");

            if (!furnituresFile.exists()) {
                plugin.saveResource(furnituresFile.getPath(), false);
            }

            if (!swordsFile.exists()) {
                plugin.saveResource(swordsFile.getPath(), false);
            }
        }

        File[] files = modelsDirectory.listFiles((dir, name) -> name.endsWith(".yml"));
        if(files == null) {
            throw new IllegalArgumentException("Could not find any models. Please make sure you have some models in " + modelsDirectory);
        }

        for(File file : files) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            Set<String> keys = yamlConfiguration.getKeys(false);
            for(String index : keys) {
                ConfigurationSection section = yamlConfiguration.getConfigurationSection(index);
                if(section == null) {
                    throw new IllegalArgumentException("Could not find any valid models in " + file.getName());
                }
                loadModel(section);
            }
        }
    }

    private void loadModel(@Nonnull ConfigurationSection section) {
        String categoryName = section.getString("category", null);
        if(categoryName == null) {
            throw new IllegalArgumentException(section.getName() + "'s category is not valid.");
        }

        Category category = categories.get(categoryName);
        if(category == null) {
            throw new IllegalArgumentException(section.getName() + " does not have a valid category: " + categoryName);
        }

        List<Model> modelList = models.getOrDefault(category, new ArrayList<>());

        modelList.add(new Model(section));
        models.put(category, modelList);
    }
}
