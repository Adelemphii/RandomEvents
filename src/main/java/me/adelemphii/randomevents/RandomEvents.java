package me.adelemphii.randomevents;

import co.aikar.commands.BukkitCommandManager;
import me.adelemphii.randomevents.commands.CreateSphereCommand;
import me.adelemphii.randomevents.events.BlockRegenEvents;
import me.adelemphii.randomevents.events.CustomBombPlacementEvent;
import me.adelemphii.randomevents.events.ExplosionEvents;
import me.adelemphii.randomevents.recipes.HardenedSnowRecipe;
import me.adelemphii.randomevents.recipes.ThanhiumBombRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomEvents extends JavaPlugin {

    private BukkitCommandManager commandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        /* https://github.com/aikar/commands/wiki/Using-ACF */
        commandManager = new BukkitCommandManager(this);

        registerRecipes();
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        commandManager.registerCommand(new CreateSphereCommand(this));
    }

    private void registerEvents() {
        if(getConfig().getBoolean("bigger_explosions")) {
            getLogger().info("Registered Bigger Explosions");
            getLogger().info("WARNING: Can crash servers if server doesn't have decent hardware. I.E. a free aternos instance.");
            getServer().getPluginManager().registerEvents(new ExplosionEvents(this), this);
        }

        if(getConfig().getBoolean("regen_explosions")) {
            getLogger().info("Registered Block Regen");
            getServer().getPluginManager().registerEvents(new BlockRegenEvents(this), this);
        }

        if(getConfig().getBoolean("custom_bombs")) {
            getLogger().info("Registered Custom Bomb events");
            getLogger().info("WARNING: Can crash servers if server doesn't have decent hardware. I.E. a free aternos instance.");
            getServer().getPluginManager().registerEvents(new CustomBombPlacementEvent(this), this);
        }
    }

    private void registerRecipes() {
        if(getConfig().getBoolean("custom_bombs")) {
            getLogger().info("Registered Custom Bomb recipes");
            getServer().addRecipe(new HardenedSnowRecipe().getRecipe());
            getServer().addRecipe(new ThanhiumBombRecipe().getRecipe());
        }
    }

    public static RandomEvents getInstance() {
        return JavaPlugin.getPlugin(RandomEvents.class);
    }
}
