package com.serverstoryengine.managers;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCMemoryManager implements Listener {

    private final ServerStoryEngine plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public NPCMemoryManager(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    public void createMemory(ChronicleEvent event) {
        if (!plugin.getConfig().getBoolean("npc-memory.enabled", true)) return;

        String memory = generateMemoryText(event);
        if (memory != null) {
            plugin.getDatabaseManager().saveNPCMemory(event.getId(), memory);
        }
    }

    private String generateMemoryText(ChronicleEvent event) {
        return switch (event.getEventType()) {
            case PVP_KILL -> "I heard that %s defeated %s in battle. What a sight that must have been!";
            case DRAGON_KILL -> "Have you heard? %s defeated the dragon years ago. They say the End has been peaceful since.";
            case WARS_START -> "I still remember the war... the sounds of battle were everywhere.";
            case WARS_END -> "Finally, peace returned after the great war. We can rest easy now.";
            case ECONOMY_MILESTONE -> "%s became incredibly wealthy. Some say they have more gold than the mines themselves.";
            case BUILD -> "Did you see what %s built? It will stand for generations.";
            case ADVANCEMENT -> "%s accomplished something great today. The whole server is talking about it.";
            default -> null;
        };
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;
        if (!plugin.getConfig().getBoolean("npc-memory.enabled", true)) return;

        UUID villagerId = villager.getUniqueId();
        Long lastUsed = cooldowns.get(villagerId);
        long cooldown = plugin.getConfig().getLong("npc-memory.mention-cooldown", 600);

        if (lastUsed != null && System.currentTimeMillis() - lastUsed < cooldown * 50) return;

        String memory = plugin.getDatabaseManager().getRandomNPCMemory();
        if (memory != null) {
            cooldowns.put(villagerId, System.currentTimeMillis());
            event.getPlayer().sendMessage(
                net.kyori.adventure.text.Component.text(villager.getName() + ": ", net.kyori.adventure.text.format.NamedTextColor.GREEN)
                    .append(net.kyori.adventure.text.Component.text(memory, net.kyori.adventure.text.format.NamedTextColor.GRAY, net.kyori.adventure.text.format.TextDecoration.ITALIC))
            );
        }
    }
}
