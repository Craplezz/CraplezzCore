package me.mani.clcore.util;

import org.bson.Document;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ConvertUtils {

    public static Document toDocument(CachedServerInfo serverInfo) {
        return new Document("maxPlayers", serverInfo.getMaxPlayers())
                .append("onlinePlayers", serverInfo.getOnlinePlayers())
                .append("motd", serverInfo.getMotd());
    }

    public static CachedServerInfo toServerInfo(String serverName, Document document) {
        return new CachedServerInfo(serverName, document.getInteger("maxPlayers"), document.getInteger("onlinePlayers"), document.getString("motd"));
    }

}
