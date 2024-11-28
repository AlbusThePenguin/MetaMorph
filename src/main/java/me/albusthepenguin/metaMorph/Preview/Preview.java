package me.albusthepenguin.metaMorph.Preview;

import me.albusthepenguin.metaMorph.Message;
import me.albusthepenguin.metaMorph.Models.Model;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Preview {

    private final Plugin plugin;

    public Preview(Plugin plugin) {
        this.plugin = plugin;
    }

    public void spawn(Player player, Model model) {
        this.plugin.getLogger().info("Spawning a preview! :)");

        if (!player.hasPermission(model.getPermission()) && !model.getPermission().equalsIgnoreCase("none")) {
            player.sendMessage(Message.setColor("You don't have permission to preview " + model.getDisplayName()));
            return;
        }

        Location eyeLocation = player.getEyeLocation().add(0, -2, 0);
        Vector direction = eyeLocation.getDirection();
        Location location = eyeLocation.add(direction.multiply(2));
        World world = location.getWorld();

        if (world == null) {
            throw new IllegalArgumentException("Could not spawn 'Preview' because world is null.");
        }

        ItemDisplay itemDisplay = world.spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(model.getItemStack());
        itemDisplay.setGlowing(true);
        itemDisplay.setCustomName(Message.setColor("&ePreview: " + model.getDisplayName()));
        itemDisplay.setCustomNameVisible(true);

        animate(itemDisplay, location);
    }

    private void animate(ItemDisplay itemDisplay, Location location) {
        final double maxHeight = 5.0;
        final double stepHeight = 0.1;
        final long taskInterval = 2L;
        final int spinSpeed = 5;

        new BukkitRunnable() {
            double currentHeight = 0.0;

            @Override
            public void run() {
                if (currentHeight >= maxHeight) {
                    Bukkit.getScheduler().runTaskLater(plugin, itemDisplay::remove, 20L);
                    cancel();
                    return;
                }

                Location updatedLocation = location.clone().add(0, currentHeight, 0);
                itemDisplay.teleport(updatedLocation);

                float yaw = (itemDisplay.getLocation().getYaw() + spinSpeed) % 360;
                updatedLocation.setYaw(yaw);
                itemDisplay.teleport(updatedLocation);

                currentHeight += stepHeight;
            }
        }.runTaskTimer(plugin, 0L, taskInterval);
    }
}
