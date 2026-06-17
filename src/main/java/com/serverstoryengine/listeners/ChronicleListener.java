package com.serverstoryengine.listeners;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChronicleListener implements Listener {

    private final ServerStoryEngine plugin;
    private int blocksPlacedSinceCheck = 0;

    public ChronicleListener(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("tracking.player-joins", true)) return;

        Player player = event.getPlayer();
        ChronicleEvent.EventType type = player.hasPlayedBefore()
            ? ChronicleEvent.EventType.PLAYER_JOIN
            : ChronicleEvent.EventType.FIRST_JOIN;

        String title = player.hasPlayedBefore()
            ? player.getName() + " returned"
            : player.getName() + " joined the server";

        String narrative = plugin.getNarrativeEngine().generatePlayerJoinNarrative(player.getName());

        ChronicleEvent chronicleEvent = new ChronicleEvent(
            type, title, narrative,
            player.getWorld().getName(),
            player.getLocation().getBlockX(),
            player.getLocation().getBlockY(),
            player.getLocation().getBlockZ(),
            player.getName(),
            System.currentTimeMillis(),
            null
        );
        plugin.getChronicleManager().recordEvent(chronicleEvent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.getConfig().getBoolean("tracking.player-joins", true)) return;

        Player player = event.getPlayer();
        String narrative = plugin.getNarrativeEngine().generatePlayerLeaveNarrative(player.getName());

        ChronicleEvent chronicleEvent = new ChronicleEvent(
            ChronicleEvent.EventType.PLAYER_LEAVE,
            player.getName() + " departed",
            narrative,
            player.getWorld().getName(),
            player.getLocation().getBlockX(),
            player.getLocation().getBlockY(),
            player.getLocation().getBlockZ(),
            player.getName(),
            System.currentTimeMillis(),
            null
        );
        plugin.getChronicleManager().recordEvent(chronicleEvent);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("tracking.player-deaths", true)) return;

        Player player = event.getEntity();
        String cause = event.getDeathMessage() != null ? event.getDeathMessage().toString() : "unknown causes";
        String narrative = plugin.getNarrativeEngine().generateDeathNarrative(player.getName(), cause);

        ChronicleEvent chronicleEvent = new ChronicleEvent(
            ChronicleEvent.EventType.PLAYER_DEATH,
            player.getName() + " fell",
            narrative,
            player.getWorld().getName(),
            player.getLocation().getBlockX(),
            player.getLocation().getBlockY(),
            player.getLocation().getBlockZ(),
            player.getName(),
            System.currentTimeMillis(),
            cause
        );
        plugin.getChronicleManager().recordEvent(chronicleEvent);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!plugin.getConfig().getBoolean("tracking.dragon-kills", true)) return;

        if (event.getEntityType() == org.bukkit.entity.EntityType.ENDER_DRAGON) {
            Player killer = event.getEntity().getKiller();
            if (killer == null) return;

            String narrative = plugin.getNarrativeEngine().generateDragonKillNarrative(killer.getName());

            ChronicleEvent chronicleEvent = new ChronicleEvent(
                ChronicleEvent.EventType.DRAGON_KILL,
                killer.getName() + " slew the Ender Dragon",
                narrative,
                killer.getWorld().getName(),
                killer.getLocation().getBlockX(),
                killer.getLocation().getBlockY(),
                killer.getLocation().getBlockZ(),
                killer.getName(),
                System.currentTimeMillis(),
                null
            );
            plugin.getChronicleManager().recordEvent(chronicleEvent);
            plugin.getLandmarkManager().placeLandmark(chronicleEvent);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getConfig().getBoolean("tracking.block-placement", true)) return;

        blocksPlacedSinceCheck++;
        if (blocksPlacedSinceCheck >= 100) {
            blocksPlacedSinceCheck = 0;

            Player player = event.getPlayer();
            String narrative = plugin.getNarrativeEngine().generateBuildNarrative(player.getName());

            ChronicleEvent chronicleEvent = new ChronicleEvent(
                ChronicleEvent.EventType.BUILD,
                player.getName() + " shaped the land",
                narrative,
                player.getWorld().getName(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ(),
                player.getName(),
                System.currentTimeMillis(),
                null
            );
            plugin.getChronicleManager().recordEvent(chronicleEvent);
        }
    }
}
