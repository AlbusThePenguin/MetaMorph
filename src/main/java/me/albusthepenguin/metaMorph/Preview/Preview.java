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
package me.albusthepenguin.metaMorph.Preview;

import me.albusthepenguin.metaMorph.Configs.ConfigType;
import me.albusthepenguin.metaMorph.misc.Message;
import me.albusthepenguin.metaMorph.MetaMorph;
import me.albusthepenguin.metaMorph.Models.Model;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

public class Preview {

    private final MetaMorph metaMorph;

    private final Message message;

    public Preview(MetaMorph metaMorph) {
        this.metaMorph = metaMorph;

        this.message = this.metaMorph.getMessage();
    }

    /**
     * Spawns an animated ItemDisplay in front of the player at their feet level,
     * 4 blocks in the direction they are facing.
     */
    public boolean spawn(Player player, Model model) {
        if(!player.hasPermission("mm.preview")) {
            player.sendMessage(this.message.get("no-permission", null, true));
            return false;
        }

        ConfigurationSection section = this.metaMorph.getConfiguration().getConfig(ConfigType.Config).getConfigurationSection("Preview");
        assert section != null;

        Location feetLocation = player.getLocation(); // Player's feet location
        Vector direction = feetLocation.getDirection().normalize();
        Location spawnLocation = feetLocation.add(direction.multiply(4));
        World world = spawnLocation.getWorld();

        if (world == null) {
            throw new IllegalArgumentException("Could not spawn 'Preview' because world is null.");
        }

        // Spawn the ItemDisplay
        ItemDisplay itemDisplay = world.spawn(spawnLocation, ItemDisplay.class);

        Transformation transformation = itemDisplay.getTransformation();

        float size = (float) section.getDouble("size", 1.0);
        transformation.getScale().set(size);

        itemDisplay.setItemStack(model.getItemStack());

        String displayName = section.getString("display-name", "&ePreview: " + model.getDisplayName());
        if(displayName.contains("{model}")) {
            displayName = displayName.replace("{model}", model.getDisplayName());
        }
        itemDisplay.setCustomName(this.message.setColor(displayName));

        itemDisplay.setCustomNameVisible(true);

        itemDisplay.setTransformation(transformation);

        // Animate the spawned ItemDisplay
        double maxHeight = section.getDouble("max-height", 5.0);
        double heightIncrements = section.getDouble("height-increments", 0.1);
        double rotationSpeed = section.getDouble("rotation-speed", 10.0);
        long intervals = section.getLong("intervals", 2L);

        animate(player, itemDisplay, spawnLocation, maxHeight, heightIncrements, rotationSpeed, intervals);
        return true;
    }

    /**
     * Animates the ItemDisplay by moving it upward, spinning it, and adding a cyan particle trail.
     */
    private void animate(Player player, ItemDisplay itemDisplay, Location baseLocation,
                         double maxHeight, double heightIncrements, double rotationSpeed, long intervals) {
        new BukkitRunnable() {
            double currentHeight = 0.0;    // Tracks the current height
            float currentYaw = 0.0f;       // Tracks the current yaw rotation

            @Override
            public void run() {
                if (currentHeight >= maxHeight) {
                    Bukkit.getScheduler().runTaskLater(metaMorph, itemDisplay::remove, 20L);
                    player.playSound(baseLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    cancel();
                    return;
                }

                // Update ItemDisplay location and rotation (spin effect)
                Location updatedLocation = baseLocation.clone().add(0, currentHeight, 0);
                updatedLocation.setYaw(currentYaw); // Apply spinning effect
                itemDisplay.teleport(updatedLocation);

                // Update yaw for next tick
                currentYaw += (float) rotationSpeed;
                if (currentYaw >= 360.0f) currentYaw -= 360.0f; // Keep yaw within bounds

                // Spawn particle trail
                World world = updatedLocation.getWorld();
                if (world != null) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1.0F);
                    Location particleLocation = updatedLocation.clone().subtract(0, 0.3, 0);
                    world.spawnParticle(Particle.REDSTONE, particleLocation, 5, 0.1, 0.1, 0.1, 0.01, dustOptions);
                }

                currentHeight += heightIncrements;
            }
        }.runTaskTimer(metaMorph, 0L, intervals);
    }
}
