package me.adelemphii.randomevents.events;

import me.adelemphii.randomevents.util.ShapeManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * THIS EVENT COULD CAUSE ISSUES WITH SERVERS WHICH HAVE A SMALL AMOUNT OF RAM ALLOCATED
 * USE AT OWN RISK
 */
public class ExplosionEvents implements Listener {

    private final List<Material> explosiveMaterials = Arrays.asList(
            Material.TNT,
            Material.GUNPOWDER,
            Material.TNT_MINECART,
            Material.END_CRYSTAL
    );

    @EventHandler
    public void onEntityExplosion(ExplosionPrimeEvent event) {
        for(Entity entity : event.getEntity().getNearbyEntities(4, 3, 4)) {
            if(entity instanceof Player player) {
                int explosionAmount = calculateExplosionAmount(player);
                if(explosionAmount <= 3) {
                    explosionAmount = 6;
                }

                event.setCancelled(true);

                // TODO: make block regen work with this method
                ShapeManager.createSphere(player.getWorld(),
                        player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(),
                        explosionAmount, Material.AIR);
            }
        }
    }

    private int calculateExplosionAmount(Player player) {
        int explosionAmount = 0;

        // Even 64 is fkin huge, but its funny LMAO :D
        // https://cdn.discordapp.com/attachments/694661573125472256/931040245909176350/2022-01-12_22.20.42.png
        for(ItemStack item : player.getInventory().getContents()) {
            if(explosionAmount / 5 >= 64) {
                return 64;
            }

            if(item == null || item.getType() == Material.AIR) {
                continue;
            }

            if(explosiveMaterials.contains(item.getType())) {
                explosionAmount = explosionAmount + item.getAmount();
                if(player.getGameMode() != GameMode.CREATIVE) {
                    item.setAmount(0);
                }
            }
        }
        return explosionAmount / 5;
    }
}
