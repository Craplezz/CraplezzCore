package me.mani.clcore;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClickManager {

	private static Map<Player, ClickManager> clickManagers = new HashMap<>();

	private Inventory inventory;
	private HotbarClickListener[] hotbarClickListeners = new HotbarClickListener[8];
	private ClickListener[] clickListeners = new ClickListener[256];
	private Consumer<InventoryCloseEvent> closeListener;

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setClickListeners(ClickListener[] clickListeners) {
		this.clickListeners = clickListeners;
	}

	public void addHotbarClickListener(int slot, HotbarClickListener consumer) {
		hotbarClickListeners[slot] = consumer;
	}
	
	public void removeHotbarClickListener(int slot) {
		hotbarClickListeners[slot] = null;
	}
	
	public void addInventoryClickListener(int slot, ClickListener consumer) {
		clickListeners[slot] = consumer;
	}
	
	public void removeInventoryClickListener(int slot) {
		clickListeners[slot] = null;
	}

	public void addInventoryCloseListener(Consumer<InventoryCloseEvent> consumer) {
		closeListener = consumer;
	}
	
	public static ClickManager getClickManager(Player player) {
		return clickManagers.get(player);
	}
	
	public static boolean handleHotbarClick(Player player, PlayerInteractEvent event) {
		ClickManager clickManager;
		if (clickManagers.containsKey(player) && (clickManager = clickManagers.get(player)).hotbarClickListeners[event.getPlayer().getInventory().getHeldItemSlot()] != null) {
			clickManager.hotbarClickListeners[event.getPlayer().getInventory().getHeldItemSlot()].accept(event);
			return true;
		}
		return false;
	}
	
	public static void handleClose(Player player, InventoryCloseEvent event) {
		if (clickManagers.containsKey(player)) {
			ClickManager clickManager = clickManagers.get(player);
			clickManager.clickListeners = new ClickListener[256];
			clickManager.inventory = null;
			if (clickManager.closeListener != null)
				clickManager.closeListener.accept(event);
		}
	}
	
	public static boolean handleClick(Player player, InventoryClickEvent event) {
		if (event.getSlot() < 0 || event.getSlot() >= 256)
			return false;
		ClickManager clickManager = null;
		if (clickManagers.containsKey(player) && (clickManager = clickManagers.get(player)).inventory != null && clickManager.getInventory().equals(event.getClickedInventory())) {
			if (clickManager.clickListeners != null && clickManager.clickListeners[event.getSlot()] != null)
				clickManager.clickListeners[event.getSlot()].accept(event);
			return true;
		}
		System.out.println("ClickManager: " + (clickManager == null ? "null" : clickManager.getInventory().equals(event.getClickedInventory())));
		return false;
	}
	
	public static void register(Player player) {
		clickManagers.put(player, new ClickManager());
	}
	
	public static void unregister(Player player) {
		clickManagers.remove(player);
	}
	
	public interface HotbarClickListener extends Consumer<PlayerInteractEvent> {}
	
	public interface ClickListener extends Consumer<InventoryClickEvent> {}
	
}
