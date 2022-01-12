package me.adelemphii.randomevents.events;

import org.bukkit.Bukkit;
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
                Bukkit.broadcastMessage(explosionAmount + " " + player.getName());
                player.getWorld().createExplosion(player.getLocation(), explosionAmount);
            }
        }
    }

    private int calculateExplosionAmount(Player player) {
        int explosionAmount = 0;

        for(ItemStack item : player.getInventory().getContents()) {
            if(explosionAmount >= 128) {
                return explosionAmount;
            }

            if(item == null || item.getType() == Material.AIR) {
                continue;
            }

            if(explosiveMaterials.contains(item.getType())) {
                explosionAmount = explosionAmount + item.getAmount();
            }
        }

        return explosionAmount;
    }
}
