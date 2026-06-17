package com.serverstoryengine.narrative;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import com.serverstoryengine.models.ServerAge;

import java.util.concurrent.ThreadLocalRandom;

public class NarrativeEngine {

    private final ServerStoryEngine plugin;

    public NarrativeEngine(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    public String generatePvPKillNarrative(String killer, String victim) {
        String[] templates = {
            "The clash of blades echoed as %s struck down %s in a fierce duel.",
            "Blood was spilled when %s claimed victory over %s on the battlefield.",
            "%s fell to the hand of %s, adding another chapter to the endless cycle of violence.",
            "In a moment of swift justice, %s brought %s to their knees.",
            "%s emerged victorious from the clash, leaving %s to taste defeat.",
            "The echoes of combat rang out as %s bested %s in mortal combat."
        };
        return fillTemplate(pickRandom(templates), killer, victim);
    }

    public String generateDragonKillNarrative(String player) {
        String[] templates = {
            "The Age of Dragons ended when %s struck down the beast and claimed victory over the End.",
            "With a final, decisive blow, %s slew the Ender Dragon, bringing peace to the End.",
            "The dragon's roar fell silent as %s emerged triumphant from the End dimension.",
            "%s conquered the End, banishing the dragon that had terrorized the skies.",
            "Legend tells of %s, who faced the dragon and emerged as the server's greatest hero."
        };
        return fillTemplate(pickRandom(templates), player);
    }

    public String generateBuildNarrative(String player) {
        String[] templates = {
            "%s raised new structures, marking the beginning of civilization.",
            "Under %s's guidance, the land took shape as walls and roofs appeared.",
            "%s laid the foundations of what would become a great settlement.",
            "With each block placed by %s, the world grew more alive.",
            "%s shaped the landscape, leaving a mark that would endure for ages."
        };
        return fillTemplate(pickRandom(templates), player);
    }

    public String generateEconomyMilestoneNarrative(String player, long amount) {
        String[] templates = {
            "A merchant empire rises as %s amassed a fortune of %s coins.",
            "Wealth beyond measure flowed to %s, who accumulated %s coins.",
            "%s became a legend of commerce, their chest bursting with %s coins.",
            "The markets trembled as %s's wealth reached %s coins.",
            "Riches like never before were gathered by %s, totaling %s coins."
        };
        return fillTemplate(pickRandom(templates), player, String.format("%,d", amount));
    }

    public String generateWarStartNarrative(String factionA, String factionB, int kills) {
        String[] templates = {
            "The %s War has begun! %s and %s clash in a devastating conflict.",
            "Blood feuds erupted into open warfare between %s and %s after %s encounters.",
            "The drums of war sounded as %s declared war on %s following %s battles.",
            "A terrible war erupted. %s and %s now fight for dominance after %s skirmishes.",
            "Peace shattered as %s and %s entered a state of war after %s clashes."
        };
        return fillTemplate(pickRandom(templates), factionA, factionB, String.valueOf(kills));
    }

    public String generateWarEndNarrative(String factionA, String factionB, String winner, int days) {
        String[] templates = {
            "The %s War ended after %s days. The %s emerged victorious.",
            "After %s days of bloodshed, the war between %s and %s concluded with %s triumphant.",
            "Peace returned as the %s War ended. The %s claimed ultimate victory after %s days.",
            "%s days of conflict came to an end. The %s stood victorious over %s.",
            "The great war between %s and %s ended after %s days, with %s as the victor."
        };
        return fillTemplate(pickRandom(templates), factionA, String.valueOf(days), factionB, winner);
    }

    public String generatePlayerJoinNarrative(String player) {
        String[] templates = {
            "A new traveler, %s, arrived seeking fortune and glory.",
            "%s set foot in the land for the first time, eyes full of wonder.",
            "The chronicles welcomed %s as they began their journey.",
            "%s emerged from the wilderness, ready to make their mark.",
            "A new chapter began with the arrival of %s."
        };
        return fillTemplate(pickRandom(templates), player);
    }

    public String generatePlayerLeaveNarrative(String player) {
        String[] templates = {
            "%s departed the land, their story unfinished.",
            "The chronicles note the departure of %s, leaving behind unfinished tales.",
            "%s faded from memory as they ventured beyond the known world.",
            "With a heavy heart, %s departed, their legacy etched in stone.",
            "%s vanished from the land, but their deeds would not be forgotten."
        };
        return fillTemplate(pickRandom(templates), player);
    }

    public String generateAdvancementNarrative(String player, String advancement) {
        String[] templates = {
            "%s achieved greatness by completing: %s.",
            "The chronicles mark %s's triumph: %s.",
            "%s reached a new milestone: %s.",
            "With determination, %s accomplished: %s.",
            "%s added to their legend by completing: %s."
        };
        return fillTemplate(pickRandom(templates), player, advancement);
    }

    public String generateDeathNarrative(String player, String cause) {
        String[] templates = {
            "%s met a tragic end, claimed by %s.",
            "The chronicles mourn the loss of %s, taken by %s.",
            "%s's journey ended abruptly when %s claimed them.",
            "%s fell to %s, their adventure cut short.",
            "A somber entry marks the passing of %s, lost to %s."
        };
        return fillTemplate(pickRandom(templates), player, cause);
    }

    public String generateAgeTransitionNarrative(ServerAge.AgeType oldAge, ServerAge.AgeType newAge) {
        String[] templates = {
            "The %s has ended. A new era begins: the %s.",
            "As the %s fades into history, the %s dawns upon the land.",
            "The age of %s is over. Let the %s commence.",
            "Transition complete. The %s gives way to the %s.",
            "A turning point in history. The %s ends, and the %s begins."
        };
        return fillTemplate(pickRandom(templates), oldAge.getDisplayName(), newAge.getDisplayName());
    }

    public String generateBookContent(String title, ChronicleEvent[] events) {
        StringBuilder sb = new StringBuilder();
        sb.append("§6§l").append(title).append("\n\n");
        sb.append("§r§7A chronicle of notable events in this server's history.\n\n");

        for (int i = 0; i < events.length; i++) {
            ChronicleEvent event = events[i];
            sb.append("§o").append(event.getTitle()).append("§r\n");
            sb.append(event.getNarrative()).append("\n\n");

            if (sb.length() > 2000) break;
        }

        sb.append("§7--- End of Chronicle ---");
        return sb.toString();
    }

    public String generateLandmarkDescription(ChronicleEvent event) {
        return switch (event.getEventType()) {
            case PVP_KILL -> "Here " + event.getPlayerName() + " fell in battle.";
            case DRAGON_KILL -> "The site where " + event.getPlayerName() + " slew the Ender Dragon.";
            case BUILD -> "A monument to " + event.getPlayerName() + "'s great construction.";
            case WARS_START -> "The battlefield where the war began.";
            case WARS_END -> "The site where peace was restored.";
            case ECONOMY_MILESTONE -> "The birthplace of " + event.getPlayerName() + "'s trading empire.";
            default -> "A place of historical significance.";
        };
    }

    private String fillTemplate(String template, String... args) {
        String result = template;
        for (String arg : args) {
            result = result.replaceFirst("%s", arg != null ? arg : "Unknown");
        }
        return result;
    }

    private String pickRandom(String[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
}
