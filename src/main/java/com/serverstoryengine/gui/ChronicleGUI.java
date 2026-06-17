package com.serverstoryengine.gui;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ChronicleGUI implements Listener, InventoryHolder {

    private final ServerStoryEngine plugin;
    private final String INVENTORY_TITLE = "Server Chronicle";
    private int currentPage = 0;

    public ChronicleGUI(ServerStoryEngine plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player player, int page) {
        this.currentPage = page;
        Inventory gui = Bukkit.createInventory(this, 54, INVENTORY_TITLE);

        List<ChronicleEvent> events = plugin.getChronicleManager().getPage(page);
        int totalPages = plugin.getChronicleManager().getTotalPages();

        for (int i = 0; i < events.size() && i < 45; i++) {
            ChronicleEvent event = events.get(i);
            ItemStack item = createEventItem(event);
            gui.setItem(i, item);
        }

        if (page > 0) {
            gui.setItem(45, createNavigationItem(Material.ARROW, "Previous Page"));
        }
        if (page < totalPages - 1) {
            gui.setItem(53, createNavigationItem(Material.ARROW, "Next Page"));
        }

        gui.setItem(49, createNavigationItem(Material.BARRIER, "Close"));

        gui.setItem(46, createInfoItem(Material.BOOK, "Page " + (page + 1) + " of " + Math.max(totalPages, 1)));
        gui.setItem(47, createInfoItem(Material.PAPER, "Total Events: " + plugin.getDatabaseManager().getEventCount()));

        player.openInventory(gui);
    }

    private ItemStack createEventItem(ChronicleEvent event) {
        ItemStack item = new ItemStack(getMaterialForEventType(event.getEventType()));
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(event.getTitle(), NamedTextColor.YELLOW, TextDecoration.BOLD));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(event.getNarrative(), NamedTextColor.GRAY));
        lore.add(Component.empty());
        lore.add(Component.text("Type: " + event.getEventType().name(), NamedTextColor.DARK_GRAY));
        lore.add(Component.text("Time: " + formatTime(event.getTimestamp()), NamedTextColor.DARK_GRAY));
        if (event.getPlayerName() != null) {
            lore.add(Component.text("Player: " + event.getPlayerName(), NamedTextColor.DARK_GRAY));
        }
        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private Material getMaterialForEventType(ChronicleEvent.EventType type) {
        return switch (type) {
            case PVP_KILL -> Material.IRON_SWORD;
            case PLAYER_DEATH -> Material.BONE;
            case DRAGON_KILL -> Material.DRAGON_EGG;
            case WARS_START, WARS_END -> Material.RED_BANNER;
            case ECONOMY_MILESTONE -> Material.GOLD_INGOT;
            case BUILD -> Material.BRICK;
            case ADVANCEMENT -> Material.NETHER_STAR;
            case PLAYER_JOIN, FIRST_JOIN -> Material.PLAYER_HEAD;
            case PLAYER_LEAVE -> Material.CHEST;
            case AGE_TRANSITION -> Material.CLOCK;
            case CUSTOM -> Material.WRITTEN_BOOK;
            default -> Material.PAPER;
        };
    }

    private ItemStack createNavigationItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name, NamedTextColor.WHITE));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createInfoItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name, NamedTextColor.GRAY));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ChronicleGUI gui)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();
        if (slot < 0 || slot >= event.getInventory().getSize()) return;

        if (slot == 53) {
            open(player, gui.currentPage + 1);
        } else if (slot == 45) {
            open(player, gui.currentPage - 1);
        } else if (slot == 49) {
            player.closeInventory();
        }
    }

    private String formatTime(long timestamp) {
        long days = (System.currentTimeMillis() - timestamp) / (1000 * 60 * 60 * 24);
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        return days + " days ago";
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
