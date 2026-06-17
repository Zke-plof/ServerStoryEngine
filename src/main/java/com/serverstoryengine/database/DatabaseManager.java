package com.serverstoryengine.database;

import com.serverstoryengine.ServerStoryEngine;
import com.serverstoryengine.models.ChronicleEvent;
import com.serverstoryengine.models.ServerAge;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final ServerStoryEngine plugin;
    private Connection connection;

    public DatabaseManager(ServerStoryEngine plugin) {
        this.plugin = plugin;
        connect();
        createTables();
    }

    private void connect() {
        try {
            File dbFile = new File(plugin.getDataFolder(), "chronicle.db");
            plugin.getDataFolder().mkdirs();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            plugin.getLogger().info("Connected to SQLite database.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
        }
    }

    private void createTables() {
        try {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS chronicle_events (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    event_type TEXT NOT NULL,
                    title TEXT NOT NULL,
                    narrative TEXT NOT NULL,
                    world TEXT,
                    x INTEGER,
                    y INTEGER,
                    z INTEGER,
                    player_name TEXT,
                    timestamp BIGINT NOT NULL,
                    metadata TEXT
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS server_ages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    age_type TEXT NOT NULL,
                    start_time BIGINT NOT NULL,
                    end_time BIGINT,
                    start_event_id INTEGER,
                    end_event_id INTEGER,
                    is_current BOOLEAN DEFAULT 1
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS landmarks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    event_id INTEGER,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    world TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    created_at BIGINT NOT NULL,
                    FOREIGN KEY (event_id) REFERENCES chronicle_events(id)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS historical_books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    content TEXT NOT NULL,
                    author TEXT,
                    created_at BIGINT NOT NULL,
                    event_ids TEXT
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS npc_memories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    event_id INTEGER,
                    memory_text TEXT NOT NULL,
                    used_count INTEGER DEFAULT 0,
                    last_used BIGINT,
                    FOREIGN KEY (event_id) REFERENCES chronicle_events(id)
                )
            """);

            stmt.close();
            plugin.getLogger().info("Database tables created successfully.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create tables: " + e.getMessage());
        }
    }

    public void saveEvent(ChronicleEvent event) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO chronicle_events (event_type, title, narrative, world, x, y, z, player_name, timestamp, metadata) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, event.getEventType().name());
            stmt.setString(2, event.getTitle());
            stmt.setString(3, event.getNarrative());
            stmt.setString(4, event.getWorld());
            stmt.setInt(5, event.getX());
            stmt.setInt(6, event.getY());
            stmt.setInt(7, event.getZ());
            stmt.setString(8, event.getPlayerName());
            stmt.setLong(9, event.getTimestamp());
            stmt.setString(10, event.getMetadata());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                event.setId(keys.getInt(1));
            }
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save event: " + e.getMessage());
        }
    }

    public List<ChronicleEvent> getEvents(int page, int pageSize) {
        List<ChronicleEvent> events = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM chronicle_events ORDER BY timestamp DESC LIMIT ? OFFSET ?"
            );
            stmt.setInt(1, pageSize);
            stmt.setInt(2, page * pageSize);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                events.add(eventFromResultSet(rs));
            }
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get events: " + e.getMessage());
        }
        return events;
    }

    public List<ChronicleEvent> getEventsByType(ChronicleEvent.EventType type) {
        List<ChronicleEvent> events = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM chronicle_events WHERE event_type = ? ORDER BY timestamp DESC"
            );
            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                events.add(eventFromResultSet(rs));
            }
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get events by type: " + e.getMessage());
        }
        return events;
    }

    public int getEventCount() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM chronicle_events");
            int count = rs.next() ? rs.getInt(1) : 0;
            stmt.close();
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }

    public void deleteEvent(int eventId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM chronicle_events WHERE id = ?");
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to delete event: " + e.getMessage());
        }
    }

    public void saveAge(ServerAge age) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO server_ages (age_type, start_time, end_time, start_event_id, end_event_id, is_current) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, age.getAgeType().name());
            stmt.setLong(2, age.getStartTime());
            stmt.setLong(3, age.getEndTime());
            stmt.setObject(4, age.getStartEventId());
            stmt.setObject(5, age.getEndEventId());
            stmt.setBoolean(6, age.isCurrent());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                age.setId(keys.getInt(1));
            }
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save age: " + e.getMessage());
        }
    }

    public ServerAge getCurrentAge() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM server_ages WHERE is_current = 1 ORDER BY start_time DESC LIMIT 1");
            ServerAge age = null;
            if (rs.next()) {
                age = ageFromResultSet(rs);
            }
            stmt.close();
            return age;
        } catch (SQLException e) {
            return null;
        }
    }

    public void endCurrentAge(long endTime, int endEventId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE server_ages SET is_current = 0, end_time = ?, end_event_id = ? WHERE is_current = 1"
            );
            stmt.setLong(1, endTime);
            stmt.setInt(2, endEventId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to end current age: " + e.getMessage());
        }
    }

    public List<ServerAge> getAllAges() {
        List<ServerAge> ages = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM server_ages ORDER BY start_time ASC");
            while (rs.next()) {
                ages.add(ageFromResultSet(rs));
            }
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get ages: " + e.getMessage());
        }
        return ages;
    }

    public void saveLandmark(int eventId, String name, String description, String world, int x, int y, int z) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO landmarks (event_id, name, description, world, x, y, z, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, eventId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, world);
            stmt.setInt(5, x);
            stmt.setInt(6, y);
            stmt.setInt(7, z);
            stmt.setLong(8, System.currentTimeMillis());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save landmark: " + e.getMessage());
        }
    }

    public void saveBook(String title, String content, String author, String eventIds) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO historical_books (title, content, author, created_at, event_ids) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, author);
            stmt.setLong(4, System.currentTimeMillis());
            stmt.setString(5, eventIds);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save book: " + e.getMessage());
        }
    }

    public void saveNPCMemory(int eventId, String memoryText) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO npc_memories (event_id, memory_text) VALUES (?, ?)"
            );
            stmt.setInt(1, eventId);
            stmt.setString(2, memoryText);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save NPC memory: " + e.getMessage());
        }
    }

    public String getRandomNPCMemory() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT memory_text FROM npc_memories ORDER BY RANDOM() LIMIT 1"
            );
            String memory = rs.next() ? rs.getString(1) : null;
            stmt.close();
            return memory;
        } catch (SQLException e) {
            return null;
        }
    }

    public int getTotalPvPKills() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM chronicle_events WHERE event_type = 'PVP_KILL'"
            );
            int count = rs.next() ? rs.getInt(1) : 0;
            stmt.close();
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }

    public int getUniquePlayers() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(DISTINCT player_name) FROM chronicle_events WHERE player_name IS NOT NULL"
            );
            int count = rs.next() ? rs.getInt(1) : 0;
            stmt.close();
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }

    public int getStructureCount() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM chronicle_events WHERE event_type = 'BUILD'"
            );
            int count = rs.next() ? rs.getInt(1) : 0;
            stmt.close();
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }

    private ChronicleEvent eventFromResultSet(ResultSet rs) throws SQLException {
        ChronicleEvent event = new ChronicleEvent(
            ChronicleEvent.EventType.valueOf(rs.getString("event_type")),
            rs.getString("title"),
            rs.getString("narrative"),
            rs.getString("world"),
            rs.getInt("x"),
            rs.getInt("y"),
            rs.getInt("z"),
            rs.getString("player_name"),
            rs.getLong("timestamp"),
            rs.getString("metadata")
        );
        event.setId(rs.getInt("id"));
        return event;
    }

    private ServerAge ageFromResultSet(ResultSet rs) throws SQLException {
        ServerAge age = new ServerAge(
            ServerAge.AgeType.valueOf(rs.getString("age_type")),
            rs.getLong("start_time"),
            rs.getObject("end_time") != null ? rs.getLong("end_time") : null,
            rs.getObject("start_event_id") != null ? rs.getInt("start_event_id") : null,
            rs.getObject("end_event_id") != null ? rs.getInt("end_event_id") : null,
            rs.getBoolean("is_current")
        );
        age.setId(rs.getInt("id"));
        return age;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to close database: " + e.getMessage());
        }
    }
}
