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
import me.albusthepenguin.metaMorph.MetaMorph;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ModelHandler {
    private final MetaMorph metaMorph;

    private final Map<String, Model> models = new HashMap<>(); // New map for direct Model lookups

    public ModelHandler(MetaMorph metaMorph) {
        this.metaMorph = metaMorph;

        reloadAll();
    }

    public void reloadAll() {
        this.models.clear();

        loadModels();
    }

    public List<Model> getModels(Material material) {
        return this.models.values().stream()
                .filter(model -> model.getItemStack().getType() == material)
                .collect(Collectors.toList());
    }

    private void loadModels() {
        File modelsDirectory = new File(metaMorph.getDataFolder(), "models");

        if (!modelsDirectory.exists()) {
            boolean created = modelsDirectory.mkdirs();
            this.metaMorph.getLogger().info("Trying to create model default files.");

            if (created) {
                String pickaxePath = "models" + File.separator + "pickaxes.yml";
                this.generate(new File(pickaxePath), pickaxePath);

                String swordsPath = "models" + File.separator + "swords.yml";
                this.generate(new File(swordsPath), swordsPath);

                String axePath = "models" + File.separator + "axes.yml";
                this.generate(new File(axePath), axePath);

                String hoePath = "models" + File.separator + "hoes.yml";
                this.generate(new File(hoePath), hoePath);

                String shovelPath = "models" + File.separator + "shovels.yml";
                this.generate(new File(shovelPath), shovelPath);
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
                this.models.put(section.getName(), new Model(this.metaMorph, section));
            }
        }
    }

    private void generate(File file, String path) {
        if (!file.exists()) {
            this.metaMorph.saveResource(path, false);
        }
    }
}

