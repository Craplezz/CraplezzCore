package me.mani.clcore.listener;

import me.mani.clcore.ClickManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		event.setCancelled(ClickManager.handleClick((Player) event.getWhoClicked(), event));
		
	}
	
}
