package com.serverstoryengine.commands;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import com.serverstoryengine.models.ServerAge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChronicleAdminCommand implements CommandExecutor {

    private final ServerStoryEngine plugin;

    public ChronicleAdminCommand(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("storyengine.admin")) {
            sender.sendMessage(Component.text("You don't have permission.", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add" -> {
                if (args.length < 3) {
                    sender.sendMessage(Component.text("Usage: /chronicle-admin add <title> <narrative>", NamedTextColor.RED));
                    return true;
                }
                String title = args[1];
                StringBuilder narrative = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    narrative.append(args[i]).append(" ");
                }

                ChronicleEvent event = new ChronicleEvent(
                    ChronicleEvent.EventType.CUSTOM,
                    title,
                    narrative.toString().trim(),
                    null, 0, 0, 0,
                    sender.getName(),
                    System.currentTimeMillis(),
                    null
                );
                plugin.getChronicleManager().recordEvent(event);
                sender.sendMessage(Component.text("Event added to chronicle.", NamedTextColor.GREEN));
            }
            case "delete" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /chronicle-admin delete <id>", NamedTextColor.RED));
                    return true;
                }
                try {
                    int id = Integer.parseInt(args[1]);
                    plugin.getDatabaseManager().deleteEvent(id);
                    sender.sendMessage(Component.text("Event deleted.", NamedTextColor.GREEN));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid ID.", NamedTextColor.RED));
                }
            }
            case "reload" -> {
                plugin.reloadConfig();
                sender.sendMessage(Component.text("Configuration reloaded.", NamedTextColor.GREEN));
            }
            case "stats" -> {
                int totalEvents = plugin.getDatabaseManager().getEventCount();
                int totalKills = plugin.getDatabaseManager().getTotalPvPKills();
                int uniquePlayers = plugin.getDatabaseManager().getUniquePlayers();

                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text("=== Chronicle Stats ===", NamedTextColor.GOLD));
                sender.sendMessage(Component.text("Total Events: " + totalEvents, NamedTextColor.YELLOW));
                sender.sendMessage(Component.text("Total PvP Kills: " + totalKills, NamedTextColor.YELLOW));
                sender.sendMessage(Component.text("Unique Players: " + uniquePlayers, NamedTextColor.YELLOW));

                ServerAge currentAge = plugin.getAgeManager().getCurrentAge();
                if (currentAge != null) {
                    sender.sendMessage(Component.text("Current Age: " + currentAge.getAgeType().getDisplayName(), NamedTextColor.LIGHT_PURPLE));
                }
                sender.sendMessage(Component.empty());
            }
            case "age" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /chronicle-admin age <settlement|expansion|conflict|prosperity>", NamedTextColor.RED));
                    return true;
                }
                try {
                    ServerAge.AgeType ageType = ServerAge.AgeType.valueOf(args[1].toUpperCase());
                    plugin.getAgeManager().startNewAge(ageType);
                    sender.sendMessage(Component.text("Age changed to " + ageType.getDisplayName(), NamedTextColor.GREEN));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("Invalid age type.", NamedTextColor.RED));
                }
            }
            case "book" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /chronicle-admin book <title>", NamedTextColor.RED));
                    return true;
                }
                if (sender instanceof Player player) {
                    String title = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                    var events = plugin.getDatabaseManager().getEvents(0, 10);
                    String content = plugin.getNarrativeEngine().generateBookContent(title, events.toArray(new ChronicleEvent[0]));
                    plugin.getDatabaseManager().saveBook(title, content, "Server Chronicle", "");
                    player.getInventory().addItem(createBook(title, content));
                    sender.sendMessage(Component.text("Book created and added to inventory.", NamedTextColor.GREEN));
                }
            }
            default -> sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.empty());
        sender.sendMessage(Component.text("=== Chronicle Admin Commands ===", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/chronicle-admin add <title> <narrative>", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/chronicle-admin delete <id>", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/chronicle-admin stats", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/chronicle-admin age <type>", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/chronicle-admin book <title>", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/chronicle-admin reload", NamedTextColor.YELLOW));
        sender.sendMessage(Component.empty());
    }

    private org.bukkit.inventory.ItemStack createBook(String title, String content) {
        org.bukkit.inventory.ItemStack book = new org.bukkit.inventory.ItemStack(org.bukkit.Material.WRITTEN_BOOK);
        org.bukkit.inventory.meta.BookMeta meta = (org.bukkit.inventory.meta.BookMeta) book.getItemMeta();
        meta.setTitle(title);
        meta.setAuthor("Server Chronicle");
        meta.addPage(content);
        book.setItemMeta(meta);
        return book;
    }
}
