package me.mani.clcore.util;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class CachedServerInfo {

    private String serverName;
    private int maxPlayers;
    private int onlinePlayers;
    private String motd;
    private boolean offline;
    private long lastFetchTimestamp = System.currentTimeMillis();

    public CachedServerInfo(String serverName, int maxPlayers, int onlinePlayers, String motd) {
        this.serverName = serverName;
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
        this.motd = motd;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public long getLastFetchTimestamp() {
        return lastFetchTimestamp;
    }

}
