package com.ashkiano.linkingbook;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

//TODO pridat craftici permisi
public class LinkingBook extends JavaPlugin {

    private String bookName;
    private String sameDimensionMessage;

    @Override
    public void onEnable() {

        // Load or generate the config file
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();

        // Get the book name from config
        bookName = config.getString("linking-book-name", "Linking Book");
        // Load additional settings from config
        sameDimensionMessage = getConfig().getString("same-dimension-message", "You cannot use this book in the same dimension it was created.");

        this.getServer().getPluginManager().registerEvents(new LinkingBookListener(bookName, sameDimensionMessage), this);

        addLinkingBookRecipe();

        Metrics metrics = new Metrics(this, 19437);

        this.getLogger().info("Thank you for using the LinkingBook plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    private void addLinkingBookRecipe() {
        ItemStack linkingBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) linkingBook.getItemMeta();
        meta.setDisplayName(bookName); // Use the name from config
        linkingBook.setItemMeta(meta);

        ShapelessRecipe linkingBookRecipe = new ShapelessRecipe(new NamespacedKey(this, "linking_book"), linkingBook);
        linkingBookRecipe.addIngredient(Material.BOOK);

        Bukkit.addRecipe(linkingBookRecipe);
    }
}
