package com.serverstoryengine.managers;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ChronicleManager {

    private final ServerStoryEngine plugin;

    public ChronicleManager(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    public void recordEvent(ChronicleEvent event) {
        plugin.getDatabaseManager().saveEvent(event);

        if (event.getId() > 0) {
            plugin.getNPCMemoryManager().createMemory(event);
        }

        broadcastEvent(event);
        plugin.getDiscordManager().sendEvent(event);
    }

    private void broadcastEvent(ChronicleEvent event) {
        Component message = Component.text()
            .append(Component.text("[Chronicle] ", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(event.getTitle(), NamedTextColor.YELLOW))
            .build();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public List<ChronicleEvent> getPage(int page) {
        int pageSize = plugin.getConfig().getInt("narrative.max-events-per-page", 10);
        return plugin.getDatabaseManager().getEvents(page, pageSize);
    }

    public int getTotalPages() {
        int pageSize = plugin.getConfig().getInt("narrative.max-events-per-page", 10);
        int totalEvents = plugin.getDatabaseManager().getEventCount();
        return (int) Math.ceil((double) totalEvents / pageSize);
    }

    public void sendChronicleToPlayer(Player player, int page) {
        List<ChronicleEvent> events = getPage(page);
        int totalPages = getTotalPages();

        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("=== Server Chronicle ===", NamedTextColor.GOLD, TextDecoration.BOLD));
        player.sendMessage(Component.text("Page " + (page + 1) + " of " + Math.max(totalPages, 1), NamedTextColor.GRAY));
        player.sendMessage(Component.empty());

        if (events.isEmpty()) {
            player.sendMessage(Component.text("No events recorded yet.", NamedTextColor.GRAY));
            return;
        }

        for (ChronicleEvent event : events) {
            player.sendMessage(Component.text()
                .append(Component.text("[" + formatTime(event.getTimestamp()) + "] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(event.getTitle(), NamedTextColor.YELLOW))
                .build());
            player.sendMessage(Component.text("  " + event.getNarrative(), NamedTextColor.GRAY));
            player.sendMessage(Component.empty());
        }
    }

    private String formatTime(long timestamp) {
        long days = (System.currentTimeMillis() - timestamp) / (1000 * 60 * 60 * 24);
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        return days + " days ago";
    }
}
