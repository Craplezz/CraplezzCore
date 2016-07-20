package me.mani.clcore.listener;

import me.mani.clcore.ClickManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		
		ClickManager.handleClose((Player) event.getPlayer(), event);
		
	}
	
}
