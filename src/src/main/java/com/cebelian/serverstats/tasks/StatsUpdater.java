package com.cebelian.serverstats.tasks;

import com.mongodb.client.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.stream.Collectors;

public class StatsUpdater extends org.bukkit.scheduler.BukkitRunnable {

    private final String serverId;
    private final MongoCollection<Document> collection;

    public StatsUpdater(String serverId) {
        this.serverId = serverId;

        String uri = "mongodb+srv://Cebelian12:testke12@cluster0.0p3pv8x.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("serverMonitor");
        collection = database.getCollection("serverMetrics");
    }

    @Override
    public void run() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double systemLoad = osBean.getSystemLoadAverage();

        List<String> playerNames = Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());

        Document doc = new Document("serverId", serverId)
                .append("owner", "Cebelian12")
                .append("status", Bukkit.getServer().isRunning() ? "online" : "offline")
                .append("ramUsedMB", usedMemory)
                .append("ramMaxMB", maxMemory)
                .append("cpuLoad", systemLoad)
                .append("onlinePlayers", playerNames)
                .append("playerCount", playerNames.size())
                .append("serverVersion", Bukkit.getVersion())
                .append("timestamp", System.currentTimeMillis());

        collection.deleteMany(new Document("serverId", serverId));
        collection.insertOne(doc);
    }
}
