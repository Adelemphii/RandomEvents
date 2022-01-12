package me.adelemphii.randomevents.events;

import me.adelemphii.randomevents.RandomEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * THIS CLASS IS WIP
 */
public class BlockRegenEvents implements Listener {

    private final Map<Location, Material> explodedBlocks = new HashMap<>();

    public BlockRegenEvents(RandomEvents plugin) {

        BukkitRunnable regenRunnable = createRegenRunnable();
        // run the task every 5 ticks
        regenRunnable.runTaskTimer(plugin, 0, 5);
    }

    // TODO: make multiple runnables on different explosions
    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().forEach(block -> {
            explodedBlocks.put(block.getLocation(), block.getType());
            block.setType(Material.AIR);
        });
    }

    private BukkitRunnable createRegenRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // regen one block from the map
                if(explodedBlocks.size() > 0) {
                    Location location = explodedBlocks.keySet().iterator().next();
                    location.getBlock().setType(explodedBlocks.get(location));
                    explodedBlocks.remove(location);
                }
            }
        };
    }
}
