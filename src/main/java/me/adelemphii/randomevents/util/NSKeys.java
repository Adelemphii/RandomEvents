package me.adelemphii.randomevents.util;

import me.adelemphii.randomevents.RandomEvents;
import org.bukkit.NamespacedKey;

public enum NSKeys {

    RANDOM_EVENTS_BOMBS(new NamespacedKey(RandomEvents.getInstance(), "re_bombs")),
    RANDOM_EVENTS_ITEMS(new NamespacedKey(RandomEvents.getInstance(), "re_blocks"));

    private final NamespacedKey key;

    NSKeys(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
