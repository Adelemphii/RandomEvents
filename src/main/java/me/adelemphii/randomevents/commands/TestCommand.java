package me.adelemphii.randomevents.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.ItemCollection;
import me.adelemphii.randomevents.util.ShapeManager;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

@CommandAlias("re|randomevents")
public class TestCommand extends BaseCommand {

    private final RandomEvents plugin;

    public TestCommand(RandomEvents plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    @CommandPermission("randomevents.create.sphere")
    @Description("Shows the help menu")
    public void onHelp(CommandSender sender) {
        // send usage using ChatColor
        sender.sendMessage(ChatColor.GREEN + "/sph create <radius> <amount>");
    }

    @Subcommand("reload")
    @CommandPermission("randomevents.reload")
    @Description("Reload the config, it wont register new events or recipes, so its pr pointless")
    public void onReload(Player player) {
        RandomEvents.getInstance().reloadConfig();
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "Config reloaded.");
    }

    @Subcommand("create")
    @CommandPermission("randomevents.create.sphere")
    @Description("Creates a sphere")
    public void onCreate(Player player, int radius, String materialStr, String type) {

        if (Material.getMaterial(materialStr) == null) {
            player.sendMessage(ChatColor.RED + "Invalid material");
            return;
        }
        if(!Objects.requireNonNull(Material.getMaterial(materialStr)).isBlock()) {
            player.sendMessage(ChatColor.RED + "That material is not a block");
            return;
        }

        Material material = Material.getMaterial(materialStr);

        if(type.equalsIgnoreCase("instant")) {
            ShapeManager.createSphere(player.getWorld(),
                    player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(),
                    radius, material);
        } else if(type.equalsIgnoreCase("delayed")) {
            ShapeManager.createSphereOverTime(player.getWorld(),
                    player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(),
                    radius, material, plugin);
        }
    }

    @Subcommand("get")
    @CommandPermission("randomevents.create.sphere")
    @Description("gets an item")
    public void onGet(Player player) {
        player.getWorld().dropItemNaturally(player.getLocation(), ItemCollection.HARDENED_SNOW);
        player.getWorld().dropItemNaturally(player.getLocation(), ItemCollection.THANHIUM_BOMB);
        player.getWorld().dropItemNaturally(player.getLocation(), ItemCollection.SNOW_SHOVEL);
    }

    @Subcommand("circle")
    @CommandPermission("randomevents.circle")
    @Description("Creates a circle")
    public void createCircle(Player player) {
        // create a circle of particles using getCircle in ShapeManager
        List<Location> circle = ShapeManager.getCircle(player, 3);

        // make a runnable that runs every 5 ticks
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                // if i is less than the amount of locations in the circle
                if(i < circle.size()) {
                    // get the location at i
                    Location loc = circle.get(i);

                    // get a different chatcolor every time
                    ChatColor color = ChatColor.values()[i % ChatColor.values().length];

                    Item item = loc.getWorld().dropItem(loc.add(0, 1, 0), new ItemStack(Material.ENDER_PEARL));
                    item.setPickupDelay(Integer.MAX_VALUE);
                    item.setGravity(false);
                    item.setVelocity(item.getVelocity().zero());

                    ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setSmall(true);
                    int x = i + 1;
                    armorStand.setCustomName(color + "Pearl #" + x);
                    armorStand.setCustomNameVisible(true);

                    // play a sound at the location
                    loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    // play a puff of particles at the location
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 3, 1, 1, 1, 0);

                    // increment i
                    i++;
                } else {
                    // cancel the runnable
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }
}
