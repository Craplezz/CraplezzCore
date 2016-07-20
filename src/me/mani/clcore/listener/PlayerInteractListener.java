package me.mani.clcore.listener;

import me.mani.clcore.ClickManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		event.setCancelled(ClickManager.handleHotbarClick(event.getPlayer(), event));
		
	}
	
}
