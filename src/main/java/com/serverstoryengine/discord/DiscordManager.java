package com.serverstoryengine.discord;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import com.serverstoryengine.models.ServerAge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.awt.Color;
import java.time.Instant;

public class DiscordManager extends ListenerAdapter {

    private final ServerStoryEngine plugin;
    private JDA jda;
    private TextChannel channel;
    private boolean connected = false;

    public DiscordManager(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        if (!plugin.getConfig().getBoolean("discord.enabled", false)) return;

        String token = plugin.getConfig().getString("discord.token", "");
        if (token.isEmpty()) {
            plugin.getLogger().warning("Discord bot token is empty! Discord integration disabled.");
            return;
        }

        try {
            jda = JDABuilder.createDefault(token)
                .addEventListeners(this)
                .build();
            plugin.getLogger().info("Connecting to Discord...");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to connect to Discord: " + e.getMessage());
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        String channelId = plugin.getConfig().getString("discord.channel-id", "");
        if (channelId.isEmpty()) {
            plugin.getLogger().warning("Discord channel ID is empty! Discord integration partially disabled.");
            connected = true;
            return;
        }

        try {
            channel = jda.getTextChannelById(channelId);
            if (channel == null) {
                plugin.getLogger().severe("Discord channel not found: " + channelId);
                return;
            }
            connected = true;
            plugin.getLogger().info("Connected to Discord channel: " + channel.getName());

            channel.sendMessageEmbeds(
                new EmbedBuilder()
                    .setTitle("Server Story Engine Online")
                    .setDescription("The chronicle bot is now watching the server.")
                    .setColor(Color.GREEN)
                    .setTimestamp(Instant.now())
                    .build()
            ).queue();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to find Discord channel: " + e.getMessage());
        }
    }

    public void sendEvent(ChronicleEvent event) {
        if (!connected || channel == null) return;
        if (!plugin.getConfig().getBoolean("discord.post-events", true)) return;

        String format = plugin.getConfig().getString("discord.format", "embed");

        if (format.equalsIgnoreCase("embed")) {
            sendEmbed(event);
        } else {
            sendText(event);
        }
    }

    private void sendEmbed(ChronicleEvent event) {
        Color color = getColorForEventType(event.getEventType());
        String emoji = getEmojiForEventType(event.getEventType());

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle(emoji + " " + event.getTitle())
            .setDescription(event.getNarrative())
            .setColor(color)
            .setFooter(event.getEventType().name().replace("_", " "));

        if (plugin.getConfig().getBoolean("discord.timestamps", true)) {
            embed.setTimestamp(Instant.ofEpochMilli(event.getTimestamp()));
        }

        if (event.getPlayerName() != null) {
            embed.addField("Player", event.getPlayerName(), true);
        }
        if (event.getWorld() != null) {
            embed.addField("Location", event.getWorld() + " " + event.getX() + " " + event.getY() + " " + event.getZ(), true);
        }

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    private void sendText(ChronicleEvent event) {
        String prefix = plugin.getConfig().getString("discord.prefix", "[Chronicle]");
        String timestamp = plugin.getConfig().getBoolean("discord.timestamps", true)
            ? "`[" + formatTimestamp(event.getTimestamp()) + "]` "
            : "";

        String message = prefix + " " + timestamp + "**" + event.getTitle() + "**\n" + event.getNarrative();
        channel.sendMessage(message).queue();
    }

    public void sendAgeTransition(ServerAge.AgeType oldAge, ServerAge.AgeType newAge) {
        if (!connected || channel == null) return;

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("New Age Dawns")
            .setDescription("The **" + oldAge.getDisplayName() + "** has ended.\n\nA new era begins: **" + newAge.getDisplayName() + "**")
            .setColor(Color.MAGENTA)
            .setTimestamp(Instant.now());

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void disconnect() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    private Color getColorForEventType(ChronicleEvent.EventType type) {
        return switch (type) {
            case PVP_KILL -> Color.RED;
            case PLAYER_DEATH -> Color.DARK_GRAY;
            case DRAGON_KILL -> Color.MAGENTA;
            case WARS_START -> new Color(139, 0, 0);
            case WARS_END -> Color.GREEN;
            case ECONOMY_MILESTONE -> new Color(255, 215, 0);
            case BUILD -> new Color(139, 69, 19);
            case ADVANCEMENT -> Color.CYAN;
            case PLAYER_JOIN, FIRST_JOIN -> Color.BLUE;
            case PLAYER_LEAVE -> Color.GRAY;
            case AGE_TRANSITION -> Color.MAGENTA;
            default -> Color.WHITE;
        };
    }

    private String getEmojiForEventType(ChronicleEvent.EventType type) {
        return switch (type) {
            case PVP_KILL -> "\u2694\ufe0f";
            case PLAYER_DEATH -> "\ud83d\udc80";
            case DRAGON_KILL -> "\ud83d\udc09";
            case WARS_START -> "\ud83d\udca5";
            case WARS_END -> "\ud83d\udd4a\ufe0f";
            case ECONOMY_MILESTONE -> "\ud83d\udcb0";
            case BUILD -> "\ud83c\udfd7\ufe0f";
            case ADVANCEMENT -> "\u2b50";
            case PLAYER_JOIN, FIRST_JOIN -> "\ud83d\udc4b";
            case PLAYER_LEAVE -> "\ud83d\udc4b";
            case AGE_TRANSITION -> "\u23f3";
            default -> "\ud83d\udcdc";
        };
    }

    private String formatTimestamp(long timestamp) {
        long days = (System.currentTimeMillis() - timestamp) / (1000 * 60 * 60 * 24);
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        return days + "d ago";
    }

    public boolean isConnected() {
        return connected;
    }

    public JDA getJda() {
        return jda;
    }
}
