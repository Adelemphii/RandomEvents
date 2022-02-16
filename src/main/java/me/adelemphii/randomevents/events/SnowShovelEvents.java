package me.adelemphii.randomevents.events;

import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.PluginKeys;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SnowShovelEvents implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if(event.getItem() == null) {
            return;
        }

        if(event.getClickedBlock() == null) {
            return;
        }
        
        if(event.getItem().getType() != Material.IRON_SHOVEL) {
            return;
        }

        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        
        if(pdc.has(PluginKeys.RANDOM_EVENTS_ITEMS.getKey(), PersistentDataType.STRING)) {
            String itemType = pdc.get(PluginKeys.RANDOM_EVENTS_ITEMS.getKey(), PersistentDataType.STRING);

            assert itemType != null;
            if(itemType.equals("snow_shovel")) {

                Block clickedBlock = event.getClickedBlock();
                if(clickedBlock.getBlockData() instanceof Snow snow) {
                    if(snow.getLayers() > 1) {
                        snow.setLayers(snow.getLayers() - 1);
                        clickedBlock.setBlockData(snow);
                    } else {
                        clickedBlock.setType(Material.AIR);
                    }
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(),
                            new ItemStack(Material.SNOWBALL, 1));
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_SNOW_BREAK, 1, 1);
                } else if(clickedBlock.getType() == Material.SNOW_BLOCK) {

                    clickedBlock.setType(Material.SNOW);
                    Snow snow = (Snow) clickedBlock.getBlockData();
                    snow.setLayers(snow.getMaximumLayers() - 1);
                    clickedBlock.setBlockData(snow);

                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(),
                            new ItemStack(Material.SNOWBALL, 1));
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_SNOW_BREAK, 1, 1);
                }
            }
        }
    }
}
