package me.adelemphii.randomevents;

import co.aikar.commands.BukkitCommandManager;
import me.adelemphii.randomevents.events.BlockRegenEvents;
import me.adelemphii.randomevents.events.ExplosionEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomEvents extends JavaPlugin {

    private BukkitCommandManager commandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        /* https://github.com/aikar/commands/wiki/Using-ACF */
        commandManager = new BukkitCommandManager(this);

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {

    }

    private void registerEvents() {
        if(getConfig().getBoolean("bigger_explosions")) {
            getLogger().info("Registered Bigger Explosions");
            getLogger().info("WARNING: Can crash servers if server doesn't have decent hardware. I.E. a free aternos instance.");
            getServer().getPluginManager().registerEvents(new ExplosionEvents(), this);
        }

        if(getConfig().getBoolean("regen_explosions")) {
            getLogger().info("Registered Regen Explosions");
            getServer().getPluginManager().registerEvents(new BlockRegenEvents(this), this);
        }
    }

    public BukkitCommandManager getCommandManager() {
        return commandManager;
    }
}
