package me.adelemphii.randomevents.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.SphereManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@CommandAlias("sph|sphere")
public class CreateSphereCommand extends BaseCommand {

    private final RandomEvents plugin;

    public CreateSphereCommand(RandomEvents plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    @CommandPermission("randomevents.create.sphere")
    @Description("Shows the help menu")
    public void onHelp(CommandSender sender) {
        // send usage using ChatColor
        sender.sendMessage(ChatColor.GREEN + "/sph create <radius> <amount>");
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
            SphereManager.createSphere(player.getWorld(),
                    player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(),
                    radius, material);
        } else if(type.equalsIgnoreCase("delayed")) {
            SphereManager.createSphereOverTime(player.getWorld(),
                    player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(),
                    radius, material, plugin);
        }
    }
}
