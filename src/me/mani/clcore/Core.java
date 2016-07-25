package me.mani.clcore;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import me.mani.clcore.bungee.ServerManager;
import me.mani.clcore.listener.*;
import me.mani.clcore.locale.LocaleManager;
import me.mani.clcore.util.CachedServerInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class Core extends JavaPlugin {

    private static Core instance;
    private static LocaleManager localeManager;
    private static ServerManager serverManager;

    private MongoClient mongoClient;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);

        mongoClient = new MongoClient(new ServerAddress("craplezz.de", 27017), Collections.singletonList(MongoCredential.createCredential("Overload", "admin", "1999mani123".toCharArray())));
        localeManager = new LocaleManager(mongoClient, "todo", "general");
        serverManager = new ServerManager(new CachedServerInfo(getServer().getServerName(), getServer().getMaxPlayers(), getServer().getOnlinePlayers().size(), getServer().getMotd()));

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Broadcast own server info
        serverManager.broadcastServerInfoData();

        // Let everybody know that we started
        serverManager.requestServerInfoUpdate();
    }

    public static Core getInstance() {
        return instance;
    }

    public static LocaleManager getLocaleManager() {
        return localeManager;
    }

    public static ServerManager getServerManager() {
        return serverManager;
    }
}
