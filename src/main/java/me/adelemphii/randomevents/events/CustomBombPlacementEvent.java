package me.adelemphii.randomevents.events;

import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.PluginKeys;
import me.adelemphii.randomevents.util.ShapeManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * This class is HEAVY on TPS, but its funny.
 */
public class CustomBombPlacementEvent implements Listener {

    private RandomEvents plugin;

    private Map<UUID, String> playersWhoDied = new HashMap<>();

    public CustomBombPlacementEvent(RandomEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCustomBombPlacement(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;
        if(event.getPlayer().getGameMode() == GameMode.ADVENTURE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        // check the pdc of the item
        if(meta.getPersistentDataContainer().has(PluginKeys.RANDOM_EVENTS_BOMBS.getKey(), PersistentDataType.STRING)) {
            PersistentDataContainer container = meta.getPersistentDataContainer();

            String bombType = container.get(PluginKeys.RANDOM_EVENTS_BOMBS.getKey(), PersistentDataType.STRING);
            if(bombType == null || bombType.isEmpty()) return;

            Block clickedBlock = event.getClickedBlock();
            assert clickedBlock != null;
            Block faceBlock = clickedBlock.getRelative(event.getBlockFace());

            setCustomExplosion(event.getPlayer(), bombType, Objects.requireNonNull(faceBlock.getLocation()));

            if(event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                item.setAmount(item.getAmount() - 1);
            }
            event.setCancelled(true);

        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(playersWhoDied.containsKey(event.getEntity().getUniqueId())) {
            Player player = event.getEntity();
            event.setDeathMessage(player.getDisplayName() + " died from " + playersWhoDied.get(player.getUniqueId()));
            playersWhoDied.remove(player.getUniqueId());
        }
    }

    private void setCustomExplosion(Player player, String bombType, Location blockPlacedLocation) {
        switch(bombType.toLowerCase()) {
            case "thanhium":
                thanhiumExplosion(player, blockPlacedLocation);
                break;
            case "normal":
                break;
        }
    }

    private void thanhiumExplosion(Player player, Location blockPlacedLocation) {
        // make a repeating runnable that counts down from 30 seconds at the blockplacedlocation
        // when it reaches 0, explode the block

        ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(blockPlacedLocation.getWorld()).spawnEntity(blockPlacedLocation.getBlock().getLocation().add(.5, 0, .5), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomName(ChatColor.RED + "Thanhium Bomb");
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setSmall(true);

        blockPlacedLocation.getBlock().setType(Material.TNT);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lTHANHIUM BOMB ACTIVATED! RUN!"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lIT WILL EXPLODE IN &e&l30 SECONDS!"));
        new BukkitRunnable() {
            int counter = 30;
            @Override
            public void run() {
                if(counter == 25 || counter == 20 || counter == 15 || counter == 10 || counter == 5) {
                    for(Entity entity : Objects.requireNonNull(blockPlacedLocation.getWorld()).getNearbyEntities(blockPlacedLocation, 64, 64, 64)) {
                        if(entity instanceof Player nearbyPlayer) {
                            // send message to nearby player saying that the bomb will explode in counter seconds
                            nearbyPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lTHE THANHIUM BOMB WILL EXPLODE IN &e&l" + counter + " SECONDS!"));
                        }
                    }
                }

                if(counter == 5) {
                    ShapeManager.createThanhiumExplosion(blockPlacedLocation.getWorld(),
                            blockPlacedLocation.getBlockX(), blockPlacedLocation.getBlockY(), blockPlacedLocation.getBlockZ(),
                            64, Material.AIR);
                    armorStand.remove();
                }
                if(counter == 0) {
                    for(Entity entity : Objects.requireNonNull(blockPlacedLocation.getWorld()).getNearbyEntities(blockPlacedLocation, 128, 128, 128)) {
                        if(entity instanceof Player nearbyPlayer) {
                            // damage every player scaling down with distance
                            nearbyPlayer.damage(Math.max(0, (32 - entity.getLocation().distance(blockPlacedLocation)) / 2));
                            
                            playersWhoDied.put(player.getUniqueId(), "thanhium bomb");

                            nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                            nearbyPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&c&lThe ground rocks violently as a bomb of extraordinary power explodes nearby, a mushroom cloud engulfing the clouds!"));
                            nearbyPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lMist and smoke rise from the ground, and snow falls within the vicinity of the explosion!"));
                            this.cancel();
                        }
                    }
                }
                counter--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
