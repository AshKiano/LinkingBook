package com.ashkiano.linkingbook;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class LinkingBook extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new LinkingBookListener(), this);

        addLinkingBookRecipe();

        Metrics metrics = new Metrics(this, 19437);
    }

    private void addLinkingBookRecipe() {
        ItemStack linkingBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) linkingBook.getItemMeta();
        meta.setDisplayName("Linking Book");
        linkingBook.setItemMeta(meta);

        ShapelessRecipe linkingBookRecipe = new ShapelessRecipe(new NamespacedKey(this, "linking_book"), linkingBook);
        linkingBookRecipe.addIngredient(Material.BOOK);

        Bukkit.addRecipe(linkingBookRecipe);
    }

}
