package me.albusthepenguin.metaMorph.Models;

import lombok.Getter;
import lombok.NonNull;
import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.Configs.Configuration;
import me.albusthepenguin.metaMorph.MetaMorph;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ModelHandler {
    @Getter
    private final MetaMorph metaMorph;

    private final Configuration configuration;

    @Getter
    private final Set<Category> categories = new HashSet<>();

    @Getter
    private final Map<Category, List<Model>> models = new HashMap<>();

    private final Map<String, Model> modelMap = new HashMap<>(); // New map for direct Model lookups

    public ModelHandler(MetaMorph metaMorph, Configuration configuration) {
        this.metaMorph = metaMorph;
        this.configuration = configuration;

        ConfigurationSection categorySection = this.configuration.getConfig(ConfigType.Config).getConfigurationSection("Categories");
        if (categorySection == null) {
            throw new IllegalArgumentException("The 'Categories' in config.yml does not exist. Could not load categories.");
        }

        reloadAll();
    }

    public void reloadAll() {
        this.categories.clear();
        this.models.clear();
        this.modelMap.clear();

        ConfigurationSection categorySection = this.configuration.getConfig(ConfigType.Config).getConfigurationSection("Categories");
        if (categorySection == null) {
            throw new IllegalArgumentException("The 'Categories' in config.yml does not exist. Could not load categories.");
        }
        loadCategories(categorySection);
    }

    private void loadCategories(@NonNull ConfigurationSection section) {
        for (String index : section.getKeys(false)) {
            ConfigurationSection indexSection = section.getConfigurationSection(index);
            if (indexSection == null) {
                throw new IllegalArgumentException(index + " is not a valid category section. Could not load categories.");
            }
            categories.add(new Category(this.metaMorph, indexSection));
        }
        loadModels();
    }

    private void loadModels() {
        File modelsDirectory = new File(metaMorph.getDataFolder(), "models");
        this.metaMorph.getLogger().info("The directory is " + modelsDirectory.getAbsolutePath());

        if (!modelsDirectory.exists()) {
            boolean created = modelsDirectory.mkdirs();
            this.metaMorph.getLogger().info("Trying to create model default files.");

            if (created) {
                String furniturePath = "models" + File.separator + "furnitures.yml";
                String swordsPath = "models" + File.separator + "swords.yml";

                File furnitures = new File(furniturePath);
                File swords = new File(swordsPath);

                if (!furnitures.exists()) {
                    this.metaMorph.saveResource(furniturePath, false);
                }

                if (!swords.exists()) {
                    this.metaMorph.saveResource(swordsPath, false);
                }
            }
        }

        File[] files = modelsDirectory.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            throw new IllegalArgumentException("Could not find any models. Please make sure you have some models in " + modelsDirectory);
        }

        for (File file : files) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            Set<String> keys = yamlConfiguration.getKeys(false);
            for (String index : keys) {
                ConfigurationSection section = yamlConfiguration.getConfigurationSection(index);
                if (section == null) {
                    throw new IllegalArgumentException("Could not find any valid models in " + file.getName());
                }
                loadModel(section);
            }
        }
    }

    public Category getCategory(String categoryName) {
        return categories.stream()
                .filter(category -> category.getId().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);
    }

    public Model getModel(String modelName) {
        return modelMap.get(modelName.toLowerCase()); // Direct lookup from modelMap
    }

    private void loadModel(@NonNull ConfigurationSection section) {
        String categoryName = section.getString("category", null);
        if (categoryName == null) {
            throw new IllegalArgumentException(section.getName() + "'s category is not valid.");
        }

        Category category = getCategory(categoryName);
        if (category == null) {
            throw new IllegalArgumentException(section.getName() + " does not have a valid category: " + categoryName);
        }

        List<Model> modelList = models.computeIfAbsent(category, k -> new ArrayList<>());
        Model model = new Model(this.metaMorph, section);
        modelList.add(model);
        modelMap.put(model.getId().toLowerCase(), model); // Add to modelMap for quick lookup
    }
}

