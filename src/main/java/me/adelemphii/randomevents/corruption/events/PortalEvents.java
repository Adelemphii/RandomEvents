package me.adelemphii.randomevents.corruption.events;

import me.adelemphii.randomevents.RandomEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.List;

public record PortalEvents(RandomEvents plugin) implements Listener {

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        List<BlockState> blocks = event.getBlocks();

        if(blocks.isEmpty()) return;

        plugin.getCorruptionManager().addPortal(blocks);
    }

    @EventHandler
    public void onPortalDestroy(BlockPhysicsEvent event) {
        if(event.getChangedType() == Material.NETHER_PORTAL) {
            Block block = event.getSourceBlock();

            if(plugin.getCorruptionManager().getPortal(block.getState())) {
                plugin.getCorruptionManager().removePortal(block.getState());
            }
        }
    }

}
