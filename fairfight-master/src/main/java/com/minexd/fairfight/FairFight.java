package com.minexd.fairfight;


import com.minexd.spigot.SpigotX;
import lombok.Getter;

import com.minexd.fairfight.client.ClientManager;
import com.minexd.fairfight.command.FairFightCommand;
import com.minexd.fairfight.handler.CustomMovementHandler;
import com.minexd.fairfight.handler.CustomPacketHandler;
import com.minexd.fairfight.listener.BungeeListener;
import com.minexd.fairfight.manager.PlayerDataManager;
import com.minexd.fairfight.mongo.FairFightMongo;
import com.minexd.fairfight.task.InsertLogsTask;

import com.minexd.fairfight.util.nucleus.config.FileConfig;
import com.minexd.fairfight.util.nucleus.listener.ListenerHandler;

import net.minecraft.server.v1_8_R3.MinecraftServer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class FairFight extends JavaPlugin {

    @Getter
    private static FairFight instance;

    private FileConfig mainFileConfig;
    private FairFightMongo mongo;
    private PlayerDataManager playerDataManager;
    private ClientManager clientManager;
    private Set<UUID> receivingAlerts;
    private Set<String> disabledChecks;
    private double rangeVl;
    
    public FairFight() {
        this.rangeVl = 30.0;
    }
    
    public void onEnable() {
        instance = this;

        this.receivingAlerts = new HashSet<>();
        this.disabledChecks = new HashSet<>();

        this.mainFileConfig = new FileConfig(this, "config.yml");
        this.mongo = new FairFightMongo();

        SpigotX.INSTANCE.addPacketHandler(new CustomPacketHandler(this));
        SpigotX.INSTANCE.addMovementHandler(new CustomMovementHandler(this));

        getCommand("fairfight").setExecutor(new FairFightCommand());

        ListenerHandler.loadListenersFromPackage(this, "com.minexd.fairfight.listener");

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getServer().getScheduler().runTaskTimer(this, new InsertLogsTask(), 20L * 60L * 5L, 20L * 60L * 5L);

        this.playerDataManager = new PlayerDataManager();
        this.clientManager = new ClientManager();
    }
    
    public boolean isAntiCheatEnabled() {
        return MinecraftServer.getServer().tps1.getAverage() > 19.0 && MinecraftServer.LAST_TICK_TIME + 100L > System.currentTimeMillis();
    }

    public boolean canAlert(Player player) {
        return this.receivingAlerts.contains(player.getUniqueId());
    }

    public boolean toggleAlerts(Player player) {
        boolean current = this.receivingAlerts.remove(player.getUniqueId());

        if (!current) {
            this.receivingAlerts.add(player.getUniqueId());
        }

        return !current;
    }

    private String oof = "This is the most bullshit thing I've ever made.";

}
