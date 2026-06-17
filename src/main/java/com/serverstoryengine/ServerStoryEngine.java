package com.serverstoryengine;

import com.serverstoryengine.commands.AgeCommand;
import com.serverstoryengine.commands.BookCommand;
import com.serverstoryengine.commands.ChronicleAdminCommand;
import com.serverstoryengine.commands.ChronicleCommand;
import com.serverstoryengine.commands.DiscordCommand;
import com.serverstoryengine.commands.LandmarkCommand;
import com.serverstoryengine.database.DatabaseManager;
import com.serverstoryengine.discord.DiscordManager;
import com.serverstoryengine.gui.ChronicleGUI;
import com.serverstoryengine.listeners.AgeListener;
import com.serverstoryengine.listeners.ChronicleListener;
import com.serverstoryengine.managers.AgeManager;
import com.serverstoryengine.managers.ChronicleManager;
import com.serverstoryengine.managers.LandmarkManager;
import com.serverstoryengine.managers.NPCMemoryManager;
import com.serverstoryengine.narrative.NarrativeEngine;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerStoryEngine extends JavaPlugin {

    private static ServerStoryEngine instance;
    private DatabaseManager databaseManager;
    private ChronicleManager chronicleManager;
    private AgeManager ageManager;
    private LandmarkManager landmarkManager;
    private NPCMemoryManager npcMemoryManager;
    private NarrativeEngine narrativeEngine;
    private ChronicleGUI chronicleGUI;
    private DiscordManager discordManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.databaseManager = new DatabaseManager(this);
        this.narrativeEngine = new NarrativeEngine(this);
        this.chronicleManager = new ChronicleManager(this);
        this.ageManager = new AgeManager(this);
        this.landmarkManager = new LandmarkManager(this);
        this.npcMemoryManager = new NPCMemoryManager(this);
        this.chronicleGUI = new ChronicleGUI(this);

        this.discordManager = new DiscordManager(this);
        discordManager.connect();

        getServer().getPluginManager().registerEvents(new ChronicleListener(this), this);
        getServer().getPluginManager().registerEvents(new AgeListener(this), this);

        getCommand("chronicle").setExecutor(new ChronicleCommand(this));
        getCommand("chronicle-admin").setExecutor(new ChronicleAdminCommand(this));
        getCommand("landmark").setExecutor(new LandmarkCommand(this));
        getCommand("book").setExecutor(new BookCommand(this));
        getCommand("age").setExecutor(new AgeCommand(this));
        getCommand("discord").setExecutor(new DiscordCommand(this));

        getLogger().info("Server Story Engine has been enabled!");
    }

    @Override
    public void onDisable() {
        if (discordManager != null) {
            discordManager.disconnect();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("Server Story Engine has been disabled.");
    }

    public static ServerStoryEngine getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ChronicleManager getChronicleManager() {
        return chronicleManager;
    }

    public AgeManager getAgeManager() {
        return ageManager;
    }

    public LandmarkManager getLandmarkManager() {
        return landmarkManager;
    }

    public NPCMemoryManager getNPCMemoryManager() {
        return npcMemoryManager;
    }

    public NarrativeEngine getNarrativeEngine() {
        return narrativeEngine;
    }

    public ChronicleGUI getChronicleGUI() {
        return chronicleGUI;
    }

    public DiscordManager getDiscordManager() {
        return discordManager;
    }
}
