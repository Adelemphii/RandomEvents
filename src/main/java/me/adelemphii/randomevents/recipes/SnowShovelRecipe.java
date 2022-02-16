package me.adelemphii.randomevents.recipes;

import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.ItemCollection;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class SnowShovelRecipe {

    NamespacedKey key = new NamespacedKey(RandomEvents.getInstance(), "snow_shovel");

    ShapedRecipe recipe;

    public SnowShovelRecipe() {
        recipe = new ShapedRecipe(key, ItemCollection.SNOW_SHOVEL);
        recipe.shape("III", " S ", "NSN");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('N', Material.SNOW_BLOCK);
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

}
