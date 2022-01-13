package me.adelemphii.randomevents.util;

import me.adelemphii.randomevents.RandomEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SphereManager {

    // how to create a sphere of blocks

    public static void createSphereOverTime(World world, int x, int y, int z, int radius, Material material, RandomEvents plugin) {
        List<Block> blocks = new ArrayList<>();

        int r2 = radius * radius;
        for (int y1 = y - radius; y1 <= y + radius; y1++) {
            for (int z1 = z - radius; z1 <= z + radius; z1++) {
                for (int x1 = x - radius; x1 <= x + radius; x1++) {
                    int dx = x1 - x;
                    int dy = y1 - y;
                    int dz = z1 - z;
                    if (dx * dx + dy * dy + dz * dz <= r2) {
                        // store blocks in a list and then make a bukkitrunnable to slowly change them to the new material
                        blocks.add(world.getBlockAt(x1, y1, z1));
                    }
                }
            }
        }

        if(!blocks.isEmpty()) {
            // make a bukkitrunnable to slowly change them to the new material
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (blocks.size() >= 1) {
                        blocks.get(0).setType(material);

                        if(blocks.size() - 1 > 1) {
                            blocks.get(blocks.size() - 1).setType(material);
                            blocks.remove(blocks.size() - 1);
                        }

                        blocks.remove(0);
                    } else {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Sphere created with material " + material.name());
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1); 
        }
    }

    // create a sphere instantly
    public static void createSphere(World world, int x, int y, int z, int radius, Material material) {
        int r2 = radius * radius;
        for (int y1 = y - radius; y1 <= y + radius; y1++) {
            for (int z1 = z - radius; z1 <= z + radius; z1++) {
                for (int x1 = x - radius; x1 <= x + radius; x1++) {
                    int dx = x1 - x;
                    int dy = y1 - y;
                    int dz = z1 - z;
                    if (dx * dx + dy * dy + dz * dz <= r2) {
                        if(world.getBlockAt(x1, y1, z1).getType() == Material.BEDROCK) {
                            continue;
                        }
                        world.getBlockAt(x1, y1, z1).setType(material);
                    }
                }
            }
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "Sphere created with material " + material.name());
        Bukkit.broadcastMessage(ChatColor.GREEN + "With a radius of " + radius);
    }
}
