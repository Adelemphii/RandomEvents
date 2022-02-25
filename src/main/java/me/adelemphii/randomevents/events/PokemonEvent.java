package me.adelemphii.randomevents.events;

import me.adelemphii.randomevents.RandomEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PokemonEvent implements Listener {

    List<Location> triggers = new ArrayList<>();
    
    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event) {
        if(event.getTo() == event.getFrom()) {
            return;
        }

        Location locTo = event.getTo().clone().add(0, 1, 0);

        if(locTo.getBlock().getType() != Material.TALL_GRASS) {
            return;
        }
        Location locationExact = new Location(locTo.getWorld(), locTo.getBlockX(), locTo.getBlockY(), locTo.getBlockZ());
        if(triggers.contains(locationExact)) {
            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int chance = random.nextInt(101);

        if(chance <= 10) {
            locTo.getWorld().spawnEntity(locTo, EntityType.CREEPER);
            event.getPlayer().sendMessage("Lol you're so fucked.");
            triggers.add(locationExact);
            removeLocation();
        }

    }

    // make a runnable which fires 3 seconds after being called and removes the first location from the list
    public void removeLocation() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(triggers.size() > 0) {
                    triggers.remove(0);
                }
            }
        };

        runnable.runTaskLater(RandomEvents.getInstance(), 20 * 3);
    }
}
