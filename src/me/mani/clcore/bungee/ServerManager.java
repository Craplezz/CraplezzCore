package me.mani.clcore.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import me.mani.clcore.Core;
import me.mani.clcore.util.CachedServerInfo;
import me.mani.clcore.util.ConvertUtils;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ServerManager {

    private static final int CACHE_TIME = 30000;

    private MongoCollection<Document> mongoCollection;
    private Map<String, CachedServerInfo> serverInfos = new HashMap<>();

    public ServerManager(MongoClient mongoClient, String database, String collection) {
        mongoCollection = mongoClient.getDatabase(database).getCollection(collection);
    }

    public void fetchServerInfos() {
        for (CachedServerInfo serverInfo : serverInfos.values())
            serverInfo.setOffline(true);
        Document document = mongoCollection.find(new Document("type", "servers")).first();
        for (Map.Entry<String, Object> entry : document.get("data", Document.class).entrySet()) {
            Document serverDocument = (Document) entry.getValue();
            serverInfos.put(entry.getKey(), ConvertUtils.toServerInfo(entry.getKey(), serverDocument));
        }
    }

    public CachedServerInfo fetchServerInfo(String serverName, boolean force) {
        CachedServerInfo serverInfo;
        if (serverInfos.containsKey(serverName) && !force && System.currentTimeMillis() - (serverInfo = serverInfos.get(serverName)).getLastFetchTimestamp() < CACHE_TIME)
            return serverInfo;
        fetchServerInfos();
        return serverInfos.get(serverName);
    }

    public boolean connect(Player player, String serverName) {
        CachedServerInfo serverInfo;
        if ((serverInfo = fetchServerInfo(serverName, true)) != null && !serverInfo.isOffline()) {
            connectRawly(player, serverName);
            return true;
        }
        return false;
    }

    public void connectRawly(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
    }

}
