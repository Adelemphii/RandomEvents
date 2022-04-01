package me.adelemphii.randomevents.corruption;

import me.adelemphii.randomevents.RandomEvents;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.Sign;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CorruptionManager {

    private final HashMap<Integer, List<BlockState>> portals = new HashMap<>();

    private final HashMap<Integer, BukkitRunnable> portalRunnables = new HashMap<>();
    private final HashMap<Integer, Integer> portalRadius = new HashMap<>();

    List<Material> exclusionTypes = Arrays.asList(
            Material.LAVA,
            Material.AIR,
            Material.OBSIDIAN,
            Material.NETHER_PORTAL,
            Material.CAVE_AIR,
            Material.VOID_AIR,
            Material.BEDROCK,

            Material.GRAVEL,
            Material.TWISTING_VINES_PLANT,
            Material.WEEPING_VINES,
            Material.SOUL_SAND,
            Material.SOUL_SOIL,
            Material.NETHER_WART_BLOCK,
            Material.NETHERRACK,
            Material.NETHER_QUARTZ_ORE,
            Material.NETHER_GOLD_ORE,
            Material.BASALT,
            Material.POLISHED_BASALT,

            Material.NETHER_BRICKS,
            Material.NETHER_BRICK_FENCE,
            Material.NETHER_BRICK_STAIRS,
            Material.NETHER_BRICK_WALL,
            Material.NETHER_BRICKS,
            Material.NETHER_BRICK_FENCE,
            Material.NETHER_BRICK_SLAB,
            Material.RED_NETHER_BRICK_SLAB,
            Material.RED_NETHER_BRICK_STAIRS,
            Material.RED_NETHER_BRICK_WALL,

            Material.CRIMSON_NYLIUM,
            Material.CRIMSON_NYLIUM,
            Material.CRIMSON_ROOTS,
            Material.CRIMSON_STEM,
            Material.CRIMSON_FUNGUS,
            Material.CRIMSON_PLANKS,
            Material.CRIMSON_FENCE,
            Material.CRIMSON_FENCE_GATE,
            Material.CRIMSON_DOOR,
            Material.CRIMSON_TRAPDOOR,
            Material.CRIMSON_SIGN,
            Material.CRIMSON_WALL_SIGN,
            Material.CRIMSON_PRESSURE_PLATE,
            Material.CRIMSON_BUTTON,

            Material.SOUL_TORCH,
            Material.SOUL_WALL_TORCH,
            Material.SOUL_CAMPFIRE,
            Material.SOUL_FIRE,
            Material.SOUL_LANTERN,

            Material.CHEST,
            Material.BREWING_STAND,
            Material.DISPENSER,
            Material.DROPPER,
            Material.FURNACE,
            Material.HOPPER,
            Material.JUKEBOX,
            Material.NOTE_BLOCK,
            Material.TRAPPED_CHEST,
            Material.ENCHANTING_TABLE,
            Material.ANVIL,
            Material.BEACON,
            Material.ENDER_CHEST,
            Material.SHULKER_BOX,

            Material.REDSTONE,
            Material.REDSTONE_BLOCK,
            Material.REDSTONE_LAMP,
            Material.REDSTONE_TORCH,
            Material.REDSTONE_WALL_TORCH,
            Material.REDSTONE_LAMP,
            Material.COMPARATOR,
            Material.REPEATER,
            Material.TARGET,
            Material.LECTERN,
            Material.REDSTONE_WIRE,
            Material.IRON_TRAPDOOR,
            Material.TRIPWIRE,
            Material.TRIPWIRE_HOOK,
            Material.LEVER,
            Material.PISTON,
            Material.STICKY_PISTON,
            Material.PISTON_HEAD,
            Material.TNT,
            Material.OBSERVER,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.DAYLIGHT_DETECTOR,
            Material.IRON_DOOR,

            Material.QUARTZ_BLOCK,
            Material.QUARTZ_BRICKS,
            Material.QUARTZ_PILLAR,
            Material.QUARTZ_SLAB,
            Material.QUARTZ_STAIRS,
            Material.SMOOTH_QUARTZ,
            Material.SMOOTH_QUARTZ_SLAB,
            Material.SMOOTH_QUARTZ_STAIRS,
            Material.CHISELED_QUARTZ_BLOCK,

            Material.BLACKSTONE,
            Material.BLACKSTONE_SLAB,
            Material.BLACKSTONE_STAIRS,
            Material.BLACKSTONE_WALL,
            Material.POLISHED_BLACKSTONE,
            Material.POLISHED_BLACKSTONE_BRICK_SLAB,
            Material.POLISHED_BLACKSTONE_BRICK_STAIRS,
            Material.POLISHED_BLACKSTONE_BRICK_WALL,
            Material.POLISHED_BLACKSTONE_BUTTON,
            Material.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            Material.POLISHED_BLACKSTONE_SLAB,
            Material.POLISHED_BLACKSTONE_WALL,
            Material.CRACKED_POLISHED_BLACKSTONE_BRICKS,

            Material.PURPUR_BLOCK,
            Material.PURPUR_PILLAR,
            Material.PURPUR_SLAB,
            Material.PURPUR_STAIRS,

            Material.CREEPER_HEAD,
            Material.PLAYER_HEAD,
            Material.CREEPER_WALL_HEAD,
            Material.PLAYER_WALL_HEAD,
            Material.ZOMBIE_HEAD,
            Material.DRAGON_HEAD,
            Material.ZOMBIE_WALL_HEAD,
            Material.DRAGON_WALL_HEAD,
            Material.SKELETON_SKULL,
            Material.WITHER_SKELETON_SKULL,
            Material.WITHER_SKELETON_WALL_SKULL,
            Material.SKELETON_WALL_SKULL,

            Material.BELL,
            Material.FLOWER_POT,
            Material.CARTOGRAPHY_TABLE,
            Material.CAULDRON,
            Material.BOOKSHELF,
            Material.COBWEB,
            Material.LOOM,
            Material.BEEHIVE,
            Material.SEA_PICKLE,
            Material.CRYING_OBSIDIAN,
            Material.CHAIN
    );

    public CorruptionManager() {

    }

    public void addPortal(List<BlockState> blocks) {
        int id = portals.size() + 1;
        portals.put(id, blocks);

        int x = 0;
        int y = 0;
        int z = 0;
        int count = 0;
        for (BlockState block : blocks) {
            if (block.getType() == Material.OBSIDIAN) {
                if (count == 0) {
                    x = block.getLocation().getBlockX();
                }
                if (count == 1) {
                    y = block.getLocation().getBlockY();
                }
                if (count == 2) {
                    z = block.getLocation().getBlockZ();
                }
                count++;
            }
        }

        Location center = new Location(blocks.get(0).getWorld(), x, y, z);

        Bukkit.broadcastMessage("Center: " + center.getX() + ", " + center.getY() + ", " + center.getZ());

        startPortalPass(id, center);

    }

    public boolean isPortal(int id) {
        List<BlockState> blockStates = portals.get(id);
        if (blockStates == null) {
            return false;
        }
        return true;
    }

    public boolean getPortal(BlockState block) {
        for (List<BlockState> portal : portals.values()) {
            for (BlockState portalBlock : portal) {
                if (portalBlock.getLocation().equals(block.getLocation())) {
                    return true;
                }
            }
        }
        return false;
    }
    public List<BlockState> getPortal(int id) {
        return portals.get(id);
    }

    public List<BlockState> removePortal(int id) {
        return portals.remove(id);
    }

    public void removePortal(BlockState block) {
        for (Map.Entry<Integer, List<BlockState>> entry : portals.entrySet()) {
            if (entry.getValue().stream().anyMatch(blockState -> blockState.getLocation().equals(block.getLocation()))) {
                portals.remove(entry.getKey());
                return;
            }
        }
    }

    // method to get all blocks in a radius
    public List<BlockState> getBlocksInRadius(Location center, int radius, int weight) {
        List<BlockState> blocks = new ArrayList<>();

        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();
        World world = center.getWorld();

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
                        Block block = world.getBlockAt(x1, y1, z1);

                        if(exclusionTypes.contains(block.getType())) {
                            continue;
                        } else if(block.getBlockData() instanceof CreatureSpawner spawner) {
                            // if type is not blaze, piglin, piglin_brute, skeleton, or hoglin, add it to the list
                            if(!spawner.getSpawnedType().equals(EntityType.BLAZE) &&
                                    !spawner.getSpawnedType().equals(EntityType.PIGLIN) &&
                                    !spawner.getSpawnedType().equals(EntityType.PIGLIN_BRUTE) &&
                                    !spawner.getSpawnedType().equals(EntityType.SKELETON) &&
                                    !spawner.getSpawnedType().equals(EntityType.HOGLIN)) {
                                blocks.add(block.getState());
                                continue;
                            }
                        } else if(Tag.BANNERS.isTagged(block.getType()) || Tag.CARPETS.isTagged(block.getType())
                                || Tag.WOOL.isTagged(block.getType()) || Tag.BEACON_BASE_BLOCKS.isTagged(block.getType())
                                || Tag.BEDS.isTagged(block.getType()) || Tag.RAILS.isTagged(block.getType())) {
                            continue;
                        } else if(block.getType().toString().contains("CONCRETE") || block.getType().toString().contains("GLASS")
                                || block.getType().toString().contains("TERRACOTTA")) {
                            continue;
                        } else if(block.getType().toString().contains("STAINED_GLASS") || block.getType().toString().contains("STAINED_GLASS_PANE")) {
                            continue;
                        }

                        // get the distance from the center of the sphere
                        double distance = Math.sqrt(i);
                        // get the weight of the point
                        double weightOfPoint = Math.pow(distance / radius, weight);
                        // get a random number between 0 and 1
                        double random = ThreadLocalRandom.current().nextDouble();
                        // if the random number is more than the weight of the point, add the block to the list
                        if(random > weightOfPoint) {
                            blocks.add(block.getState());
                        }
                    }
                }
            }
        }

        return blocks;
    }

    // method to make a runnable which runs every 5 seconds, and rnadomly changes a block inside to netherrack
    public void startPortalPass(int id, Location center) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                if(!isPortal(id)) {
                    // remove the portal from the list
                    portalRunnables.remove(id);
                    portalRadius.remove(id);
                    portals.remove(id);
                    this.cancel();
                    return;
                }

                // make a radius that starts at 3 and only increases if the blocks are all netherrack
                int radius;
                if(portalRadius.get(id) == null) {
                    radius = 12;
                } else {
                    radius = portalRadius.get(id);
                }
                // TODO: Store this list outside of the runnable until its empty so we don't have to continuously research
                List<BlockState> blocks = getBlocksInRadius(center, radius, 10);

                // if blocks is empty, increase radius
                if(blocks.isEmpty()) {
                    portalRadius.put(id, radius + 1);
                    return;
                }
                blocks = getBlocksInRadius(center, radius, 10);

                // randomly choose a block
                ThreadLocalRandom random = ThreadLocalRandom.current();
                if(blocks.isEmpty()) {
                    portalRadius.put(id, radius + 1);
                    return;
                }
                BlockState block = blocks.get(random.nextInt(blocks.size()));

                if(block.getLocation().getWorld() == null) {
                    this.cancel();
                    return;
                }

                if(block.getBlock().getType() == Material.NETHERRACK) {
                    return;
                }

                Bukkit.broadcastMessage("Changing block at " + block.getLocation().getX() + ", " + block.getLocation().getY() + ", " + block.getLocation().getZ());
                Bukkit.broadcastMessage("From: " + block.getType());

                doIfBlock(block.getBlock());
                // play a sound
                block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_PLACE, 1, 1);
                blocks.remove(block);
                Bukkit.broadcastMessage("Radius: " + radius);
                portalRadius.put(id, radius);
            }
        };
        runnable.runTaskTimer(RandomEvents.getInstance(), 0, 10);
        portalRunnables.put(id, runnable);
    }

    public void doIfBlock(Block block) {
        BlockState blockState = block.getState();
        if(block.getType() == Material.WATER || block.getType() == Material.BUBBLE_COLUMN) {
            block.setType(Material.LAVA);
            // steam particles and sound
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            // make particles appear above block
            block.getWorld().spawnParticle(Particle.SMOKE_NORMAL, block.getLocation().add(0.5, 1, 0.5), 10, .5, .5, .5, 0);
        } else if(block.getType() == Material.CACTUS) {
            block.setType(Material.TWISTING_VINES_PLANT);
        } else if(block.getType() == Material.TALL_GRASS) {
                block.setType(Material.DEAD_BUSH);
        } else if(block.getType() == Material.TWISTING_VINES) {
            block.setType(Material.TWISTING_VINES_PLANT);
        } else if(block.getType() == Material.SUGAR_CANE) {
            block.setType(Material.TWISTING_VINES);
        }
        else if(block.getType() == Material.DEAD_BUSH || Tag.FLOWERS.isTagged(block.getType())) {
            block.setType(Material.CRIMSON_FUNGUS);
        }
        else if(block.getType() == Material.RED_MUSHROOM || block.getType() == Material.BROWN_MUSHROOM) {
            block.setType(Material.CRIMSON_FUNGUS);
        }
        else if(block.getType() == Material.GRASS) {
            block.setType(Material.CRIMSON_ROOTS);
        }
        else if(block.getType() == Material.GRASS_BLOCK) {
            block.setType(Material.CRIMSON_NYLIUM);
        }
        else if(block.getType() == Material.DIRT) {
            block.setType(Material.NETHERRACK);
        }
        else if(block.getType() == Material.SAND) {
            if(Math.random() < 0.1) {
                block.setType(Material.SOUL_SAND);
            } else {
                block.setType(Material.SOUL_SOIL);
            }
        }
        else if(block.getType() == Material.GOLD_ORE) {
            block.setType(Material.NETHER_GOLD_ORE);
        }
        else if(block.getType() == Material.CLAY) {
            block.setType(Material.NETHER_QUARTZ_ORE);
        }
        else if(block.getType() == Material.SPAWNER) {
            // spawner of type blaze, piglin, piglin_brute, skeleton, or hoglin
            CreatureSpawner spawner = (CreatureSpawner) block;

            int type = (int) (Math.random() * 4);
            // use a switch
            switch(type) {
                case 0 -> spawner.setSpawnedType(EntityType.BLAZE);
                case 1 -> spawner.setSpawnedType(EntityType.PIGLIN);
                case 2 -> spawner.setSpawnedType(EntityType.PIGLIN_BRUTE);
                case 3 -> spawner.setSpawnedType(EntityType.SKELETON);
                case 4 -> spawner.setSpawnedType(EntityType.HOGLIN);
            }

            spawner.update();
        }
        else if(block.getType() == Material.STONE || block.getType() == Material.ANDESITE || block.getType() == Material.DIORITE || block.getType() == Material.GRANITE) {
            block.setType(Material.NETHERRACK);
        }
        else if(Tag.LOGS.isTagged(block.getType())) {
            Orientable orientable = (Orientable) block.getBlockData();
            Set<Axis> axes = orientable.getAxes();

            block.setType(Material.CRIMSON_STEM);

            orientable = (Orientable) block.getBlockData();
            orientable.setAxis(axes.iterator().next());
            block.setBlockData(orientable);
        }
        else if(Tag.PLANKS.isTagged(block.getType())) {
            block.setType(Material.CRIMSON_PLANKS);
        }
        // leaves = nether_wart_block
        else if(Tag.LEAVES.isTagged(block.getType())) {
            block.setType(Material.NETHER_WART_BLOCK);
        }
        else if(block.getType() == Material.VINE) {
            block.setType(Material.WEEPING_VINES);
        }
        else if(Tag.STANDING_SIGNS.isTagged(block.getType())) {
            if(block.getBlockData() instanceof org.bukkit.block.data.type.Sign sign) {
                BlockFace rotation = sign.getRotation();
                Sign signBlock = (Sign) blockState;

                signBlock.setType(Material.CRIMSON_SIGN);
                sign = (org.bukkit.block.data.type.Sign) signBlock.getBlockData();
                sign.setRotation(rotation);
                signBlock.setBlockData(sign);

                blockState.update(true);
            }
        } else if(Tag.WALL_SIGNS.isTagged(block.getType())) {
            if(block.getBlockData() instanceof org.bukkit.block.data.type.WallSign sign) {
                BlockFace rotation = sign.getFacing();
                Sign signBlock = (Sign) blockState;

                signBlock.setType(Material.CRIMSON_WALL_SIGN);
                sign = (org.bukkit.block.data.type.WallSign) signBlock.getBlockData();
                sign.setFacing(rotation);
                signBlock.setBlockData(sign);

                blockState.update(true);
            }
        }
        else if(Tag.BUTTONS.isTagged(block.getType())) {
            Directional directional = (Directional) block.getBlockData();
            BlockFace facing = directional.getFacing();

            block.setType(Material.CRIMSON_BUTTON);

            directional = (Directional) block.getBlockData();
            directional.setFacing(facing);
            block.setBlockData(directional);
        }
        else if(Tag.STAIRS.isTagged(block.getType())) {
            // crimson_stairs or nether_brick_stairs
            if(Math.random() < 0.5) {
                Directional directional = (Directional) block.getBlockData();
                Bisected bisected = (Bisected) block.getBlockData();
                Bisected.Half half = bisected.getHalf();
                Stairs stairs = (Stairs) block.getBlockData();
                Stairs.Shape shape = stairs.getShape();
                BlockFace facing = directional.getFacing();
                block.setType(Material.NETHER_BRICK_STAIRS);

                directional = (Directional) block.getBlockData();
                directional.setFacing(facing);
                block.setBlockData(directional);

                bisected = (Bisected) block.getBlockData();
                bisected.setHalf(half);
                block.setBlockData(bisected);

                stairs = (Stairs) block.getBlockData();
                stairs.setShape(shape);
                block.setBlockData(stairs);
            } else {
                Directional directional = (Directional) block.getBlockData();
                Bisected bisected = (Bisected) block.getBlockData();
                Bisected.Half half = bisected.getHalf();
                Stairs stairs = (Stairs) block.getBlockData();
                Stairs.Shape shape = stairs.getShape();
                BlockFace facing = directional.getFacing();
                block.setType(Material.NETHER_BRICK_STAIRS);

                directional = (Directional) block.getBlockData();
                directional.setFacing(facing);
                block.setBlockData(directional);

                bisected = (Bisected) block.getBlockData();
                bisected.setHalf(half);
                block.setBlockData(bisected);

                stairs = (Stairs) block.getBlockData();
                stairs.setShape(shape);
                block.setBlockData(stairs);
            }
        }
        else if(Tag.FENCES.isTagged(block.getType())) {
            // crimson_fence or nether_brick_fence
            if(Math.random() < 0.5) {
                Fence fence = (Fence) block.getBlockData();
                Map<BlockFace, Boolean> faces = new HashMap<>();
                for(BlockFace face : fence.getFaces()) {
                    faces.put(face, fence.hasFace(face));
                }

                block.setType(Material.NETHER_BRICK_FENCE);

                fence = (Fence) block.getBlockData();
                for(BlockFace face : faces.keySet()) {
                    fence.setFace(face, faces.get(face));
                }
                block.setBlockData(fence);
            } else {
                Fence fence = (Fence) block.getBlockData();
                Map<BlockFace, Boolean> faces = new HashMap<>();
                for(BlockFace face : fence.getFaces()) {
                    faces.put(face, fence.hasFace(face));
                }

                block.setType(Material.NETHER_BRICK_FENCE);

                fence = (Fence) block.getBlockData();
                for(BlockFace face : faces.keySet()) {
                    fence.setFace(face, faces.get(face));
                }
                block.setBlockData(fence);
            }
        }
        else if(Tag.TRAPDOORS.isTagged(block.getType())) {
            Directional directional = (Directional) block.getBlockData();
            BlockFace facing = directional.getFacing();
            Openable openable = (Openable) block.getBlockData();
            boolean isOpen = openable.isOpen();
            block.setType(Material.CRIMSON_TRAPDOOR);

            directional = (Directional) block.getBlockData();
            directional.setFacing(facing);
            block.setBlockData(directional);

            openable = (Openable) block.getBlockData();
            openable.setOpen(isOpen);
            block.setBlockData(openable);
        }
        else if(Tag.FENCE_GATES.isTagged(block.getType())) {
            Directional directional = (Directional) block.getBlockData();
            BlockFace facing = directional.getFacing();
            block.setType(Material.CRIMSON_FENCE_GATE);

            directional = (Directional) block.getBlockData();
            directional.setFacing(facing);
            block.setBlockData(directional);
        }
        else if(Tag.PRESSURE_PLATES.isTagged(block.getType())) {
            block.setType(Material.CRIMSON_PRESSURE_PLATE);
        }
        else if(Tag.DOORS.isTagged(block.getType())) {
            Directional directional = (Directional) block.getBlockData();
            BlockFace facing = directional.getFacing();
            Bisected bisected = (Bisected) block.getBlockData();
            Bisected.Half half = bisected.getHalf();
            block.setType(Material.CRIMSON_DOOR);

            directional = (Directional) block.getBlockData();
            directional.setFacing(facing);
            block.setBlockData(directional);

            bisected = (Bisected) block.getBlockData();
            bisected.setHalf(half);
            block.setBlockData(bisected);
        }
        else if(Tag.SLABS.isTagged(block.getType())) {
            Slab slab = (Slab) block.getBlockData();
            Slab.Type type = slab.getType();
            block.setType(Material.NETHER_BRICK_SLAB);

            slab = (Slab) block.getBlockData();
            slab.setType(type);
            block.setBlockData(slab);
        }
        else if(block.getType() == Material.TORCH) {
            block.setType(Material.SOUL_TORCH);
        }
        else if(block.getType() == Material.CAMPFIRE) {
            org.bukkit.block.data.type.Campfire campfireData = (org.bukkit.block.data.type.Campfire) block.getBlockData();

            boolean signalFire = campfireData.isSignalFire();

            block.setType(Material.SOUL_CAMPFIRE);

            campfireData = (org.bukkit.block.data.type.Campfire) block.getBlockData();
            campfireData.setSignalFire(signalFire);
            block.setBlockData(campfireData);
        }
        else if(block.getType() == Material.WALL_TORCH) {
            Directional directional = (Directional) block.getBlockData();
            BlockFace facing = directional.getFacing();
            block.setType(Material.SOUL_WALL_TORCH);

            directional = (Directional) block.getBlockData();
            directional.setFacing(facing);
            block.setBlockData(directional);
        }
        else if(block.getType() == Material.LANTERN) {
            Lantern lantern = (Lantern) block.getBlockData();
            boolean hanging = lantern.isHanging();
            block.setType(Material.SOUL_LANTERN);

            lantern = (Lantern) block.getBlockData();
            lantern.setHanging(hanging);
            block.setBlockData(lantern);
        }
        else if(block.getType() == Material.FIRE) {
            block.setType(Material.SOUL_FIRE);
        }
        else if(Tag.WALLS.isTagged(block.getType())) {
            Wall wall = (Wall) block.getBlockData();
            List<Wall.Height> wallHeights = new ArrayList<>();
            List<BlockFace> blockFaces = new ArrayList<>();
            blockFaces.add(BlockFace.NORTH);
            blockFaces.add(BlockFace.EAST);
            blockFaces.add(BlockFace.SOUTH);
            blockFaces.add(BlockFace.WEST);
            for(BlockFace blockFace : blockFaces) {
                wallHeights.add(wall.getHeight(blockFace));
            }

            block.setType(Material.NETHER_BRICK_WALL);

            wall = (Wall) block.getBlockData();
            for(int i = 0; i < blockFaces.size(); i++) {
                wall.setHeight(blockFaces.get(i), wallHeights.get(i));
            }
            block.setBlockData(wall);
        }
        else {
            block.setType(Material.NETHERRACK);
        }

        if(block.getLocation().getWorld() != null) {
            block.getLocation().getWorld().setBiome(block.getX(), block.getY(), block.getZ(), Biome.CRIMSON_FOREST);
        }
    }
}
