package com.cebelian.serverstats;

import com.cebelian.serverstats.tasks.StatsUpdater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class ServerStatsPlugin extends JavaPlugin {

    private static ServerStatsPlugin instance;
    private String serverId;

    @Override
    public void onEnable() {
        instance = this;
        serverId = UUID.randomUUID().toString(); // Egyedi ID
        getLogger().info("Szerver ID: " + serverId);
        new StatsUpdater(serverId).runTaskTimerAsynchronously(this, 0L, 6000L);
    }

    public static ServerStatsPlugin getInstance() {
        return instance;
    }

    public String getServerId() {
        return serverId;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("getid")) {
            sender.sendMessage("Ez a szerver azonosítója: " + serverId);
            return true;
        }
        return false;
    }
}
