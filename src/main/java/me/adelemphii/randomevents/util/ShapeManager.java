package me.adelemphii.randomevents.util;

import me.adelemphii.randomevents.RandomEvents;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ShapeManager {

    /**
     * Creates a sphere of blocks over time
     * @param world the world to create the sphere in
     * @param x the x coordinate of the center of the sphere
     * @param y the y coordinate of the center of the sphere
     * @param z the z coordinate of the center of the sphere
     * @param radius the radius of the sphere
     * @param material the material to change the blocks to
     * @param plugin the plugin to use for the scheduler
     */
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

    /**
     * Creates a sphere of blocks
     * @param world the world to create the sphere in
     * @param x the x coordinate of the center of the sphere
     * @param y the y coordinate of the center of the sphere
     * @param z the z coordinate of the center of the sphere
     * @param radius the radius of the sphere
     * @param material the material to change the blocks to
     */
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
    }

    /**
     * Create a sphere with snow/ice at the bottom & change the biome to ice
     * @param world The world to create the sphere in
     * @param x The x coordinate of the center of the sphere
     * @param y The y coordinate of the center of the sphere
     * @param z The z coordinate of the center of the sphere
     * @param radius The radius of the sphere
     * @param material The material to change the blocks to
     */
    public static void createThanhiumExplosion(World world, int x, int y, int z, int radius, Material material) {
        List<Block> blocks = new ArrayList<>();

        // I tried optimizing this to do the calculations async but my brain is small,
        // so it was really slow and not working
        // feel free to PR and optimize it

        int r2 = radius * radius;
        for (int y1 = y - radius; y1 <= y + radius; y1++) {
            for (int z1 = z - radius; z1 <= z + radius; z1++) {
                for (int x1 = x - radius; x1 <= x + radius; x1++) {
                    int dx = x1 - x;
                    int dy = y1 - y;
                    int dz = z1 - z;
                    if (dx * dx + dy * dy + dz * dz <= r2) {
                        Block block = world.getBlockAt(x1, y1, z1);

                        if(block.getType() == Material.BEDROCK || block.getType() == Material.WATER) {
                            continue;
                        }

                        if(world.getBiome(block.getX(), block.getY(), block.getZ()) != Biome.ICE_SPIKES) {
                            world.setBiome(block.getX(), block.getY(), block.getZ(), Biome.ICE_SPIKES);
                        }

                        world.getBlockAt(x1, y1, z1).setType(material);
                        blocks.add(world.getBlockAt(x1, y1, z1));
                    }
                }
            }
        }

        if(!blocks.isEmpty()) {
            // for every block in the list check if the block under the block isn't air, and if so, change the block to snow/ice at random
            for(Block block : blocks) {
                if(block.getRelative(BlockFace.DOWN).getType() != Material.AIR && block.getRelative(BlockFace.DOWN).getType() != Material.WATER) {
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    int randomInt = random.nextInt(0, 100);
                    if(randomInt < 50) {
                        block.setType(Material.SNOW);
                    }
                    else if(randomInt >= 75) {
                        // get the block under the block and change it to snow block
                        block.getRelative(BlockFace.DOWN).setType(Material.SNOW_BLOCK);
                    }
                    else {
                        // get the block under the block and change it to ice
                        block.getRelative(BlockFace.DOWN).setType(Material.ICE);
                    }
                }
            }
        }
    }

    // create a 2d circle horizontally surrounding a player, evenly retrieving locations from the circle
    public static List<Location> getCircle(Player player, int radius) {
        List<Location> locations = new ArrayList<>();

        // make a circle and evenly distribute 8 points around the circle
        for(int i = 0; i < 8; i++) {
            double angle = (i * Math.PI * 2) / 8;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            Location location = player.getLocation().clone();
            location.setX(location.getX() + x);
            location.setZ(location.getZ() + z);
            locations.add(location);
        }
        return locations;
    }

    public static List<Block> generateLineWithWeights(Location location, int weight, int radius) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int xCenter = location.getBlockX();
        int yCenter = location.getBlockY();
        int zCenter = location.getBlockZ();

        List<Block> blocks = new ArrayList<>();

        for(int i = 0; i < 200; i++) {
            double t = random.nextInt() * 2 * Math.PI;
            double r = Math.pow(random.nextFloat(), weight) * radius;

            double newX = xCenter + r * Math.cos(t);
            double newZ = zCenter + r * Math.sin(t);

            Block block = new Location(location.getWorld(), Math.round(newX), yCenter, Math.round(newZ)).getBlock();
            blocks.add(block);
        }
        return blocks;
    }

    public static List<Block> generateSphereWithWeights(Location location, int weight, int radius) {
        // make a list of random points in a sphere
        // every point has a weight and the higher the weight, the closer to the center they are
        List<Block> blocks = new ArrayList<>();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        World world = location.getWorld();

        if(world == null) {
            return null;
        }

        // generate a full sphere
        for(int x1 = x - radius; x1 <= x + radius; x1++) {
            for(int y1 = y - radius; y1 <= y + radius; y1++) {
                for(int z1 = z - radius; z1 <= z + radius; z1++) {
                    int dx = x1 - x;
                    int dy = y1 - y;
                    int dz = z1 - z;
                    int i = dx * dx + dy * dy + dz * dz;
                    if(i <= radius * radius) {
                        // get the distance from the center of the sphere
                        double distance = Math.sqrt(i);
                        // get the weight of the point
                        double weightOfPoint = Math.pow(distance / radius, weight);
                        // get a random number between 0 and 1
                        double random = ThreadLocalRandom.current().nextDouble();
                        // if the random number is more than the weight of the point, add the block to the list
                        if(random > weightOfPoint) {
                            blocks.add(world.getBlockAt(x1, y1, z1));
                        }
                    }
                }
            }
        }
        return blocks;
    }

}
