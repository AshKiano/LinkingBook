package com.ashkiano.linkingbook;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import java.util.Arrays;
import java.util.List;

public class LinkingBookListener implements Listener {

    private final String bookName;
    private final String sameDimensionMessage;

    public LinkingBookListener(String bookName, String sameDimensionMessage) {
        this.bookName = bookName;
        this.sameDimensionMessage = sameDimensionMessage;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getCurrentItem();
        if (result != null && result.getType() == Material.WRITTEN_BOOK) {
            BookMeta meta = (BookMeta) result.getItemMeta();
            if (meta.getDisplayName().equals(bookName)) { // Use the name from config
                Location playerLocation = event.getWhoClicked().getLocation();
                meta.setLore(
                        Arrays.asList(
                                "Dim:" + playerLocation.getWorld().getName(),
                                "X:" + playerLocation.getBlockX(),
                                "Y:" + playerLocation.getBlockY(),
                                "Z:" + playerLocation.getBlockZ()
                        )
                );
                result.setItemMeta(meta);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.WRITTEN_BOOK) {
            BookMeta meta = (BookMeta) item.getItemMeta();
            if (meta.hasLore() && meta.getLore().size() >= 4) {
                List<String> lore = meta.getLore();

                String dim = lore.get(0).split(":")[1];
                int x = Integer.parseInt(lore.get(1).split(":")[1]);
                int y = Integer.parseInt(lore.get(2).split(":")[1]);
                int z = Integer.parseInt(lore.get(3).split(":")[1]);

                // If the player is in the same dimension as the book, they won't be teleported
                if (dim.equals(event.getPlayer().getWorld().getName())) {
                    event.getPlayer().sendMessage(sameDimensionMessage);

                    event.setCancelled(true);
                    return;
                }

                World world = Bukkit.getWorld(dim);
                if (world != null) {
                    event.getPlayer().teleport(new Location(world, x, y, z));

                    // Removing the book from the player's hand
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        event.getPlayer().getInventory().setItemInMainHand(null);
                    }

                    event.setCancelled(true);
                }
            }
        }
    }

}