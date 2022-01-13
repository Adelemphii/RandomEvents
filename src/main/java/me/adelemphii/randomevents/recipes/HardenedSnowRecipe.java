package me.adelemphii.randomevents.recipes;

import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.ItemCollection;
import me.adelemphii.randomevents.util.NSKeys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class HardenedSnowRecipe {

    NamespacedKey key = new NamespacedKey(RandomEvents.getInstance(), "hardened_snow");

    ShapedRecipe recipe;

    public HardenedSnowRecipe() {
        recipe = new ShapedRecipe(key, ItemCollection.HARDENED_SNOW);
        recipe.shape("SSS", "SBS", "SBS");
        recipe.setIngredient('S', Material.SNOW_BLOCK);
        recipe.setIngredient('B', Material.BLAZE_ROD);
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
