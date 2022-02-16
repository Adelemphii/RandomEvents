package me.adelemphii.randomevents.events;

import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.PluginKeys;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class CustomItemPlacementEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;
        if(event.getPlayer().getGameMode() == GameMode.ADVENTURE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();

        if(meta.getPersistentDataContainer().has(PluginKeys.RANDOM_EVENTS_ITEMS.getKey(), PersistentDataType.STRING)) {
            PersistentDataContainer container = meta.getPersistentDataContainer();

            String itemType = container.get(PluginKeys.RANDOM_EVENTS_ITEMS.getKey(), PersistentDataType.STRING);
            if(itemType == null || itemType.isEmpty()) return;

            Block clickedBlock = event.getClickedBlock();
            assert clickedBlock != null;
            Block faceBlock = clickedBlock.getRelative(event.getBlockFace());

            setCustomEffect(itemType, Objects.requireNonNull(faceBlock.getLocation()));

        }
    }

    private void setCustomEffect(String bombType, Location location) {
        switch(bombType) {
            case "hardened_snow":
                setFreezeArea(location);
                break;
            case "default":
                break;
        }
    }

    // Make a runnable that ticks every second, and if the player is in the area, deal 1 damage to them with the message
    // "You are freezing!"
    private void setFreezeArea(Location location) {

        ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(location.getWorld()).spawnEntity(location.getBlock().getLocation().add(.5, 0, .5), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setGravity(false);
        armorStand.setCustomName(ChatColor.RED + "Brr.... Hardened Snow");
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);

        // TODO: work on fixing this
        new BukkitRunnable() {
            @Override
            public void run() {
                // check a random block within an 8x8x8 area around location
                // if it is a solid block, and the block above it is air, set the block above it to slow
                Block block = location.getWorld().getBlockAt(location.getBlockX() + (int) (Math.random() * 8), location.getBlockY() + (int) (Math.random() * 8), location.getBlockZ() + (int) (Math.random() * 8));
                if(block.getType().isSolid() && block.getRelative(BlockFace.UP).getType() == Material.AIR) {
                    block.getRelative(BlockFace.UP).setType(Material.SNOW);
                }
                // if the player is in the area, deal 1 damage to them with a message
                World world = location.getWorld();
                for(Entity entity : world.getNearbyEntities(location, 4, 4, 4)) {
                    if(entity instanceof LivingEntity livingEntity) {
                        livingEntity.damage(1);
                        // send a colored message to the player
                        livingEntity.sendMessage(ChatColor.RED + "You are freezing! Leave the area to stop the effect!");
                    }
                }

                // play snow particles randomly around the area
                for(int i = 0; i < 10; i++) {
                    location.getWorld().spawnParticle(Particle.SNOW_SHOVEL, location.getBlockX() + (int) (Math.random() * 8), location.getBlockY() + (int) (Math.random() * 8), location.getBlockZ() + (int) (Math.random() * 8), 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(RandomEvents.getInstance(), 0, 20);
    }
}
