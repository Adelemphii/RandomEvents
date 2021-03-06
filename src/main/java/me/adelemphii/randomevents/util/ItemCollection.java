package me.adelemphii.randomevents.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ItemCollection {

    public static final ItemStack THANHIUM_BOMB = createThanhiumBomb();
    public static final ItemStack HARDENED_SNOW = createHardenedSnow();
    public static final ItemStack SNOW_SHOVEL = createSnowShovel();

    private static ItemStack createSnowShovel() {
        ItemStack snowShovel = new ItemStack(Material.IRON_SHOVEL);
        ItemMeta meta = snowShovel.getItemMeta();
        assert meta != null;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.AQUA + "Snow Shovel");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "A shovel made for shoveling snow.",
                ChatColor.GRAY + "It is extremely durable and can be used",
                ChatColor.GRAY + "to remove layers of snow."));

        // add pdc
        meta.getPersistentDataContainer().set(PluginKeys.RANDOM_EVENTS_ITEMS.getKey(), PersistentDataType.STRING, "snow_shovel");

        snowShovel.setItemMeta(meta);
        return snowShovel;
    }

    private static ItemStack createHardenedSnow() {
        ItemStack hardenedSnow = new ItemStack(Material.SNOW_BLOCK);
        ItemMeta meta = hardenedSnow.getItemMeta();
        assert meta != null;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.AQUA + "Hardened Snow");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "A block of hardened snow that is",
                ChatColor.GRAY + "extremely resistant to the effects of",
                ChatColor.GRAY + "heat and cold."
        ));

        // add pdc
        meta.getPersistentDataContainer().set(PluginKeys.RANDOM_EVENTS_ITEMS.getKey(), PersistentDataType.STRING, "hardened_snow");

        hardenedSnow.setItemMeta(meta);
        return hardenedSnow;
    }

    private static ItemStack createThanhiumBomb() {
        ItemStack thaniumBomb = new ItemStack(Material.TNT);
        ItemMeta meta = thaniumBomb.getItemMeta();
        // make thanhium bomb glow
        thaniumBomb.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 1);

        assert meta != null;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.AQUA + "Thanhium Bomb");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "A weapon whose great explosive power results from the",
                ChatColor.GRAY + "sudden release of energy upon the splitting,",
                ChatColor.GRAY + "or fission, of the nuclei of heavy elements",
                ChatColor.GRAY + "using thanhium."
        ));

        // add pdc
        meta.getPersistentDataContainer().set(PluginKeys.RANDOM_EVENTS_BOMBS.getKey(), PersistentDataType.STRING, "THANHIUM");

        thaniumBomb.setItemMeta(meta);
        return thaniumBomb;
}

}
