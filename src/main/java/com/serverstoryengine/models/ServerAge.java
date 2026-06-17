package com.serverstoryengine.models;

public class ServerAge {

    public enum AgeType {
        SETTLEMENT("Age of Settlement", "The land was quiet, few souls called it home."),
        EXPANSION("Age of Expansion", "New structures rose across the horizon."),
        CONFLICT("Age of Conflict", "The clash of steel echoed through the land."),
        PROSPERITY("Age of Prosperity", "Wealth flowed like rivers through the markets.");

        private final String displayName;
        private final String introNarrative;

        AgeType(String displayName, String introNarrative) {
            this.displayName = displayName;
            this.introNarrative = introNarrative;
        }

        public String getDisplayName() { return displayName; }
        public String getIntroNarrative() { return introNarrative; }
    }

    private int id;
    private final AgeType ageType;
    private final long startTime;
    private final Long endTime;
    private final Integer startEventId;
    private final Integer endEventId;
    private final boolean current;

    public ServerAge(AgeType ageType, long startTime, Long endTime, Integer startEventId, Integer endEventId, boolean current) {
        this.ageType = ageType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startEventId = startEventId;
        this.endEventId = endEventId;
        this.current = current;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public AgeType getAgeType() { return ageType; }
    public long getStartTime() { return startTime; }
    public Long getEndTime() { return endTime; }
    public Integer getStartEventId() { return startEventId; }
    public Integer getEndEventId() { return endEventId; }
    public boolean isCurrent() { return current; }

    public long getDuration() {
        long end = endTime != null ? endTime : System.currentTimeMillis();
        return end - startTime;
    }
}
