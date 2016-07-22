package me.mani.clcore.listener;

import me.mani.clcore.ClickManager;
import me.mani.clcore.Core;
import mongoperms.MongoPermsAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class PlayerJoinListener implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {

        ClickManager.register(event.getPlayer());

        String localeString = "prefix-" + MongoPermsAPI.getGroup(event.getPlayer().getUniqueId()) + "-tab";
        if (Core.getLocaleManager().getLocale(localeString) != null)
            event.getPlayer().setPlayerListName(Core.getLocaleManager().translate(localeString) + event.getPlayer().getName());

        Core.getServerManager().broadcastServerInfoUpdate();

    }

}
