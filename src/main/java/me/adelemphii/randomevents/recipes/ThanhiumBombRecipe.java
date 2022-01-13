package me.adelemphii.randomevents.recipes;

import me.adelemphii.randomevents.RandomEvents;
import me.adelemphii.randomevents.util.ItemCollection;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class ThanhiumBombRecipe {

    NamespacedKey key = new NamespacedKey(RandomEvents.getInstance(), "thanhium_bomb");

    ShapedRecipe recipe;

    public ThanhiumBombRecipe() {
        recipe = new ShapedRecipe(key, ItemCollection.THANHIUM_BOMB);
        recipe.shape("GGG", "GSG", "GGG");
        recipe.setIngredient('G', Material.GUNPOWDER);
        RecipeChoice.ExactChoice choice = new RecipeChoice.ExactChoice(ItemCollection.HARDENED_SNOW);
        recipe.setIngredient('S', choice);
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
