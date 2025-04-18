package com.example.servermonitor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bson.Document;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServerMonitorPlugin extends JavaPlugin {

    private MongoCollection<Document> collection;
    private String serverId;

    @Override
    public void onEnable() {
        String uri = "mongodb+srv://Cebelian12:testke12@cluster0.0p3pv8x.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("serverMonitor");
        collection = database.getCollection("serverMetrics");

        serverId = UUID.randomUUID().toString();

        new BukkitRunnable() {
            @Override
            public void run() {
                collectAndStoreMetrics();
            }
        }.runTaskTimerAsynchronously(this, 0L, 100L); // 5 másodpercenként
    }

    private void collectAndStoreMetrics() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        int playerCount = Bukkit.getOnlinePlayers().size();
        String players = Bukkit.getOnlinePlayers().stream()
                .map(p -> p.getName())
                .collect(Collectors.joining(", "));
        String version = Bukkit.getVersion();
        double tps = Bukkit.getTPS()[0]; // csak Paper-en érhető el

        Document doc = new Document("serverId", serverId)
                .append("usedMemory", usedMemory)
                .append("maxMemory", maxMemory)
                .append("playerCount", playerCount)
                .append("players", players)
                .append("version", version)
                .append("tps", tps)
                .append("timestamp", System.currentTimeMillis());

        collection.insertOne(doc);
    }
}
