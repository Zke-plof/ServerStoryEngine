package com.serverstoryengine.commands;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BookCommand implements CommandExecutor {

    private final ServerStoryEngine plugin;

    public BookCommand(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("storyengine.book")) {
            player.sendMessage(Component.text("You don't have permission.", NamedTextColor.RED));
            return true;
        }

        String title = args.length > 0 ? String.join(" ", args) : "Server Chronicle";

        List<ChronicleEvent> events = plugin.getDatabaseManager().getEvents(0, 15);
        if (events.isEmpty()) {
            player.sendMessage(Component.text("No events to put in a book yet.", NamedTextColor.GRAY));
            return true;
        }

        String content = plugin.getNarrativeEngine().generateBookContent(
            title, events.toArray(new ChronicleEvent[0])
        );

        plugin.getDatabaseManager().saveBook(title, content, "Server Chronicle", "");

        org.bukkit.inventory.ItemStack book = new org.bukkit.inventory.ItemStack(org.bukkit.Material.WRITTEN_BOOK);
        org.bukkit.inventory.meta.BookMeta meta = (org.bukkit.inventory.meta.BookMeta) book.getItemMeta();
        meta.setTitle(title);
        meta.setAuthor("Server Chronicle");
        meta.addPage(content);
        book.setItemMeta(meta);

        player.getInventory().addItem(book);
        player.sendMessage(Component.text("You received: " + title, NamedTextColor.GREEN));

        return true;
    }
}
