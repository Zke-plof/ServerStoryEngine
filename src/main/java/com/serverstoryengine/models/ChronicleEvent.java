package com.serverstoryengine.models;

public class ChronicleEvent {

    public enum EventType {
        PVP_KILL,
        PLAYER_DEATH,
        DRAGON_KILL,
        WARS_START,
        WARS_END,
        ECONOMY_MILESTONE,
        BUILD,
        ADVANCEMENT,
        PLAYER_JOIN,
        PLAYER_LEAVE,
        AGE_TRANSITION,
        FIRST_JOIN,
        CUSTOM
    }

    private int id;
    private final EventType eventType;
    private final String title;
    private final String narrative;
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final String playerName;
    private final long timestamp;
    private final String metadata;

    public ChronicleEvent(EventType eventType, String title, String narrative, String world, int x, int y, int z, String playerName, long timestamp, String metadata) {
        this.eventType = eventType;
        this.title = title;
        this.narrative = narrative;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.playerName = playerName;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public EventType getEventType() { return eventType; }
    public String getTitle() { return title; }
    public String getNarrative() { return narrative; }
    public String getWorld() { return world; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public String getPlayerName() { return playerName; }
    public long getTimestamp() { return timestamp; }
    public String getMetadata() { return metadata; }
}
