package com.serverstoryengine.managers;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class LandmarkManager {

    private final ServerStoryEngine plugin;

    public LandmarkManager(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    public void placeLandmark(ChronicleEvent event) {
        if (!plugin.getConfig().getBoolean("landmarks.enabled", true)) return;
        if (event.getWorld() == null) return;

        World world = Bukkit.getWorld(event.getWorld());
        if (world == null) return;

        Location loc = new Location(world, event.getX(), event.getY(), event.getZ());
        Material material = Material.matchMaterial(
            plugin.getConfig().getString("landmarks.material", "GOLD_BLOCK")
        );
        if (material == null) material = Material.GOLD_BLOCK;

        Block block = world.getBlockAt(loc);
        if (block.getType() == Material.AIR) {
            block.setType(material);
        }

        String description = plugin.getNarrativeEngine().generateLandmarkDescription(event);
        plugin.getDatabaseManager().saveLandmark(
            event.getId(), event.getTitle(), description,
            event.getWorld(), event.getX(), event.getY(), event.getZ()
        );

        if (plugin.getConfig().getBoolean("landmarks.particles", true)) {
            spawnParticles(loc);
        }
    }

    private void spawnParticles(Location loc) {
        World world = loc.getWorld();
        if (world == null) return;

        BukkitTask[] task = new BukkitTask[1];
        task[0] = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 100) {
                    task[0].cancel();
                    return;
                }
                world.spawnParticle(Particle.TOTEM_OF_UNDYING, loc.clone().add(0.5, 1, 0.5), 5, 0.5, 0.5, 0.5, 0.01);
                ticks += 5;
            }
        }, 0L, 5L);
    }

    public void showNearestLandmark(Player player) {
        Location playerLoc = player.getLocation();
        int maxDist = plugin.getConfig().getInt("landmarks.max-distance", 10);

        double nearestDist = Double.MAX_VALUE;
        String nearestName = null;
        String nearestDesc = null;

        for (int x = -maxDist; x <= maxDist; x++) {
            for (int y = -maxDist; y <= maxDist; y++) {
                for (int z = -maxDist; z <= maxDist; z++) {
                    Block block = player.getWorld().getBlockAt(
                        playerLoc.getBlockX() + x,
                        playerLoc.getBlockY() + y,
                        playerLoc.getBlockZ() + z
                    );

                    if (block.getType() == Material.GOLD_BLOCK || block.getType() == Material.DIAMOND_BLOCK) {
                        Location blockLoc = block.getLocation();
                        double dist = playerLoc.distance(blockLoc);
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            nearestName = "Historical Landmark";
                            nearestDesc = "A place of significance in this server's history.";
                        }
                    }
                }
            }
        }

        if (nearestName != null) {
            player.sendMessage(Component.empty());
            player.sendMessage(Component.text("=== " + nearestName + " ===", NamedTextColor.GOLD));
            player.sendMessage(Component.text(nearestDesc, NamedTextColor.GRAY));
            player.sendMessage(Component.empty());
        }
    }
}
