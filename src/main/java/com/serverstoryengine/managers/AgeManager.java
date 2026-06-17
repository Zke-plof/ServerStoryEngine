package com.serverstoryengine.managers;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import com.serverstoryengine.models.ServerAge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AgeManager {

    private final ServerStoryEngine plugin;
    private ServerAge currentAge;

    public AgeManager(ServerStoryEngine plugin) {
        this.plugin = plugin;
        this.currentAge = plugin.getDatabaseManager().getCurrentAge();

        if (currentAge == null) {
            startNewAge(ServerAge.AgeType.SETTLEMENT);
        }

        if (plugin.getConfig().getBoolean("ages.enabled", true)) {
            startAgeCheckTask();
        }
    }

    private void startAgeCheckTask() {
        long interval = plugin.getConfig().getLong("ages.check-interval", 6000);
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkAgeTransition, interval, interval);
    }

    private void checkAgeTransition() {
        if (currentAge == null) return;

        ServerAge.AgeType detectedAge = detectAge();
        if (detectedAge != null && detectedAge != currentAge.getAgeType()) {
            transitionAge(detectedAge);
        }
    }

    private ServerAge.AgeType detectAge() {
        int population = Bukkit.getOnlinePlayers().size();
        int structures = plugin.getDatabaseManager().getStructureCount();
        int pvpKills = plugin.getDatabaseManager().getTotalPvPKills();

        int settlementThreshold = plugin.getConfig().getInt("ages.thresholds.settlement-population", 5);
        int expansionThreshold = plugin.getConfig().getInt("ages.thresholds.expansion-buildings", 50);
        int conflictThreshold = plugin.getConfig().getInt("ages.thresholds.conflict-pvp-rate", 10);

        if (pvpKills > conflictThreshold) {
            return ServerAge.AgeType.CONFLICT;
        }
        if (structures > expansionThreshold) {
            return ServerAge.AgeType.EXPANSION;
        }
        if (population <= settlementThreshold) {
            return ServerAge.AgeType.SETTLEMENT;
        }
        return null;
    }

    public void startNewAge(ServerAge.AgeType ageType) {
        if (currentAge != null) {
            plugin.getDatabaseManager().endCurrentAge(System.currentTimeMillis(), -1);
        }

        currentAge = new ServerAge(ageType, System.currentTimeMillis(), null, null, null, true);
        plugin.getDatabaseManager().saveAge(currentAge);

        broadcastAgeTransition(ageType);
    }

    private void transitionAge(ServerAge.AgeType newAgeType) {
        ServerAge.AgeType oldAgeType = currentAge.getAgeType();

        plugin.getDatabaseManager().endCurrentAge(System.currentTimeMillis(), -1);

        currentAge = new ServerAge(newAgeType, System.currentTimeMillis(), null, null, null, true);
        plugin.getDatabaseManager().saveAge(currentAge);

        String narrative = plugin.getNarrativeEngine().generateAgeTransitionNarrative(oldAgeType, newAgeType);
        ChronicleEvent event = new ChronicleEvent(
            ChronicleEvent.EventType.AGE_TRANSITION,
            "The " + newAgeType.getDisplayName(),
            narrative,
            null, 0, 0, 0,
            null,
            System.currentTimeMillis(),
            null
        );
        plugin.getChronicleManager().recordEvent(event);

        broadcastAgeTransition(newAgeType);
        plugin.getDiscordManager().sendAgeTransition(oldAgeType, newAgeType);
    }

    private void broadcastAgeTransition(ServerAge.AgeType ageType) {
        Component message = Component.text()
            .append(Component.text("=== ", NamedTextColor.GOLD))
            .append(Component.text(ageType.getDisplayName(), NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .append(Component.text(" ===", NamedTextColor.GOLD))
            .build();

        Component intro = Component.text(ageType.getIntroNarrative(), NamedTextColor.GRAY, TextDecoration.ITALIC);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.empty());
            player.sendMessage(message);
            player.sendMessage(intro);
            player.sendMessage(Component.empty());
        }
    }

    public ServerAge getCurrentAge() {
        return currentAge;
    }

    public void sendAgeInfo(Player player) {
        if (currentAge == null) {
            player.sendMessage(Component.text("No age recorded yet.", NamedTextColor.GRAY));
            return;
        }

        long duration = currentAge.getDuration();
        long days = duration / (1000 * 60 * 60 * 24);
        long hours = (duration % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("=== Current Server Age ===", NamedTextColor.GOLD, TextDecoration.BOLD));
        player.sendMessage(Component.text()
            .append(Component.text(currentAge.getAgeType().getDisplayName(), NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .build());
        player.sendMessage(Component.text(currentAge.getAgeType().getIntroNarrative(), NamedTextColor.GRAY, TextDecoration.ITALIC));
        player.sendMessage(Component.text("Duration: " + days + " days, " + hours + " hours", NamedTextColor.YELLOW));
        player.sendMessage(Component.empty());
    }
}
