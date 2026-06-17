package com.serverstoryengine.listeners;

import com.serverstoryengine.ServerStoryEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class AgeListener implements Listener {

    private final ServerStoryEngine plugin;

    public AgeListener(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntityType() == org.bukkit.entity.EntityType.ENDER_DRAGON) {
            if (event.getEntity().getKiller() != null) {
                plugin.getAgeManager().startNewAge(
                    com.serverstoryengine.models.ServerAge.AgeType.PROSPERITY
                );
            }
        }
    }
}
