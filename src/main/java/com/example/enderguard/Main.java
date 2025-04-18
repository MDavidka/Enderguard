package com.example.enderguard;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("EnderGuard plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EnderGuard plugin disabled!");
    }
}
