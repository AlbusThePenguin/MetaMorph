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

import me.albusthepenguin.metaMorph.Message;
import me.albusthepenguin.metaMorph.MetaMorph;
import me.albusthepenguin.metaMorph.Models.Model;
import org.bukkit.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
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
        // Permission check
        if (!player.hasPermission("mm.preview") && !model.getPermission().equalsIgnoreCase("none")) {
            player.sendMessage(this.message.get("no-permission", null, true));
            return false;
        }

        // Determine spawn location 4 blocks from player's feet in the direction they are looking
        Location feetLocation = player.getLocation(); // Player's feet location
        Vector direction = feetLocation.getDirection().normalize();
        Location spawnLocation = feetLocation.add(direction.multiply(4));
        World world = spawnLocation.getWorld();

        if (world == null) {
            throw new IllegalArgumentException("Could not spawn 'Preview' because world is null.");
        }

        // Spawn the ItemDisplay
        ItemDisplay itemDisplay = world.spawn(spawnLocation, ItemDisplay.class);
        itemDisplay.setItemStack(model.getItemStack());
        itemDisplay.setBillboard(Display.Billboard.CENTER);
        itemDisplay.setCustomName(this.message.setColor("&ePreview: " + model.getDisplayName()));
        itemDisplay.setCustomNameVisible(true);

        // Animate the spawned ItemDisplay
        animate(itemDisplay, spawnLocation);
        return true;
    }

    /**
     * Animates the ItemDisplay by moving it upward, spinning it, and adding a cyan particle trail.
     */
    private void animate(ItemDisplay itemDisplay, Location baseLocation) {
        final double maxHeight = 5.0;       // Maximum height the item reaches
        final double stepHeight = 0.1;     // Incremental height change per tick
        final long taskInterval = 2L;      // Interval in ticks between updates

        new BukkitRunnable() {
            double currentHeight = 0.0;    // Tracks the current height

            @Override
            public void run() {
                if (currentHeight >= maxHeight) {
                    Bukkit.getScheduler().runTaskLater(metaMorph, itemDisplay::remove, 20L);
                    cancel();
                    return;
                }

                // Update ItemDisplay location
                Location updatedLocation = baseLocation.clone().add(0, currentHeight, 0);
                itemDisplay.teleport(updatedLocation);

                World world = updatedLocation.getWorld();
                if (world != null) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1.0F); // Slightly reduced size for subtler effect
                    Location particleLocation = updatedLocation.clone().subtract(0, 0.3, 0); // Move particles slightly below the item
                    world.spawnParticle(Particle.REDSTONE, particleLocation, 5, 0.1, 0.1, 0.1, 0.01, dustOptions); // Reduced count and spread for a less overwhelming effect
                }

                currentHeight += stepHeight;
            }
        }.runTaskTimer(metaMorph, 0L, taskInterval);
    }
}
