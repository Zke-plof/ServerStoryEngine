package com.serverstoryengine.commands;

import com.serverstoryengine.ServerStoryEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {

    private final ServerStoryEngine plugin;

    public DiscordCommand(ServerStoryEngine plugin) {
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

        FileConfiguration config = plugin.getConfig();

        switch (args[0].toLowerCase()) {
            case "token" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /discord token <bot-token>", NamedTextColor.RED));
                    return true;
                }
                String token = args[1];
                config.set("discord.token", token);
                plugin.saveConfig();
                sender.sendMessage(Component.text("Discord bot token saved. Restart server to apply.", NamedTextColor.GREEN));
            }
            case "channel" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /discord channel <channel-id>", NamedTextColor.RED));
                    return true;
                }
                String channelId = args[1];
                config.set("discord.channel-id", channelId);
                plugin.saveConfig();
                sender.sendMessage(Component.text("Discord channel ID saved: " + channelId, NamedTextColor.GREEN));
            }
            case "enable" -> {
                config.set("discord.enabled", true);
                plugin.saveConfig();
                sender.sendMessage(Component.text("Discord integration enabled. Restart server to apply.", NamedTextColor.GREEN));
            }
            case "disable" -> {
                config.set("discord.enabled", false);
                plugin.saveConfig();
                sender.sendMessage(Component.text("Discord integration disabled. Restart server to apply.", NamedTextColor.YELLOW));
            }
            case "status" -> {
                boolean enabled = config.getBoolean("discord.enabled", false);
                String token = config.getString("discord.token", "");
                String channelId = config.getString("discord.channel-id", "");
                boolean connected = plugin.getDiscordManager().isConnected();

                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text("=== Discord Status ===", NamedTextColor.GOLD, TextDecoration.BOLD));
                sender.sendMessage(Component.text("Enabled: ", NamedTextColor.GRAY)
                    .append(Component.text(enabled, enabled ? NamedTextColor.GREEN : NamedTextColor.RED)));
                sender.sendMessage(Component.text("Token: ", NamedTextColor.GRAY)
                    .append(Component.text(token.isEmpty() ? "Not set" : "Set (" + token.substring(0, Math.min(8, token.length())) + "...)", token.isEmpty() ? NamedTextColor.RED : NamedTextColor.GREEN)));
                sender.sendMessage(Component.text("Channel: ", NamedTextColor.GRAY)
                    .append(Component.text(channelId.isEmpty() ? "Not set" : channelId, channelId.isEmpty() ? NamedTextColor.RED : NamedTextColor.GREEN)));
                sender.sendMessage(Component.text("Connected: ", NamedTextColor.GRAY)
                    .append(Component.text(connected, connected ? NamedTextColor.GREEN : NamedTextColor.RED)));
                sender.sendMessage(Component.empty());
            }
            case "test" -> {
                if (!plugin.getDiscordManager().isConnected()) {
                    sender.sendMessage(Component.text("Discord bot is not connected.", NamedTextColor.RED));
                    return true;
                }
                plugin.getDiscordManager().sendEvent(new com.serverstoryengine.models.ChronicleEvent(
                    com.serverstoryengine.models.ChronicleEvent.EventType.CUSTOM,
                    "Test Event",
                    "This is a test message from Server Story Engine.",
                    null, 0, 0, 0,
                    "Server",
                    System.currentTimeMillis(),
                    null
                ));
                sender.sendMessage(Component.text("Test message sent to Discord.", NamedTextColor.GREEN));
            }
            case "format" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /discord format <embed|text>", NamedTextColor.RED));
                    return true;
                }
                String format = args[1].toLowerCase();
                if (!format.equals("embed") && !format.equals("text")) {
                    sender.sendMessage(Component.text("Format must be 'embed' or 'text'.", NamedTextColor.RED));
                    return true;
                }
                config.set("discord.format", format);
                plugin.saveConfig();
                sender.sendMessage(Component.text("Discord format set to: " + format, NamedTextColor.GREEN));
            }
            default -> sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.empty());
        sender.sendMessage(Component.text("=== Discord Commands ===", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("/discord token <token>", NamedTextColor.YELLOW)
            .hoverEvent(HoverEvent.showText(Component.text("Set the Discord bot token")))
            .clickEvent(ClickEvent.suggestCommand("/discord token ")));
        sender.sendMessage(Component.text("/discord channel <id>", NamedTextColor.YELLOW)
            .hoverEvent(HoverEvent.showText(Component.text("Set the channel ID for history posts")))
            .clickEvent(ClickEvent.suggestCommand("/discord channel ")));
        sender.sendMessage(Component.text("/discord enable", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/discord disable", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/discord status", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/discord format <embed|text>", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/discord test", NamedTextColor.YELLOW));
        sender.sendMessage(Component.empty());
        sender.sendMessage(Component.text("Get bot token from:", NamedTextColor.GRAY));
        sender.sendMessage(Component.text("https://discord.com/developers/applications", NamedTextColor.AQUA)
            .clickEvent(ClickEvent.openUrl("https://discord.com/developers/applications")));
        sender.sendMessage(Component.empty());
    }
}
