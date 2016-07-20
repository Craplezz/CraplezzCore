package me.mani.clcore.util;

import me.mani.clcore.Core;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * Created by chasechocolate.
 * Edited by Overload.
 */
public class AnvilInventory {

    private Player player;
    private AnvilClickEventListener clickListener;
    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();
    private Inventory inventory;
    private Listener listener;

    public AnvilInventory(Player player, final AnvilClickEventListener clickListener){
        this.player = player;
        this.clickListener = clickListener;

        this.listener = new Listener() {

            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player && event.getInventory().equals(inventory)) {
                    event.setCancelled(true);

                    ItemStack itemStack = event.getCurrentItem();
                    int slot = event.getRawSlot();
                    String name = "";

                    if (itemStack != null) {
                        if (itemStack.hasItemMeta()) {
                            ItemMeta itemMeta = itemStack.getItemMeta();

                            if (itemMeta.hasDisplayName())
                                name = itemMeta.getDisplayName();
                        }
                    }

                    AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name);

                    clickListener.onAnvilClick(clickEvent);

                    if (clickEvent.getWillDestroy())
                        destroy();
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event){
                if (event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    Inventory inventory = event.getInventory();

                    if(inventory.equals(AnvilInventory.this.inventory)) {
                        inventory.clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event){
                if (event.getPlayer().equals(getPlayer()))
                    destroy();
            }

        };

        Bukkit.getPluginManager().registerEvents(listener, Core.getInstance());
    }

    public Player getPlayer() {
        return player;
    }

    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }

    public void open() {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(entityPlayer);

        // Set the items to the items from the inventory given
        inventory = container.getBukkitView().getTopInventory();

        for (AnvilSlot slot : items.keySet())
            inventory.setItem(slot.getSlot(), items.get(slot));

        // Counter stuff that the game uses to keep track of inventories
        int c = entityPlayer.nextContainerCounter();

        // Send the packet
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing"), 0));

        // Set their active container to the container
        entityPlayer.activeContainer = container;

        // Set their active container window id to that counter stuff
        entityPlayer.activeContainer.windowId = c;

        // Add the slot listener
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
    }

    public void destroy() {
        player = null;
        clickListener = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }

    /** --------- INNER CLASSES ---------

    /**
     * Used to fake the anvil container
     */
    private class AnvilContainer extends ContainerAnvil {

        public AnvilContainer(EntityHuman entity){
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman){
            return true;
        }

    }

    /**
     * Util enum for the anvil slots.
     */
    public enum AnvilSlot {
        INPUT_LEFT (0),
        INPUT_RIGHT (1),
        OUTPUT (2);

        private int slot;

        private AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values())
                if (anvilSlot.getSlot() == slot)
                    return anvilSlot;
            return null;
        }

    }

    public class AnvilClickEvent {

        private AnvilSlot slot;
        private String name;

        private boolean destroy = true;

        public AnvilClickEvent(AnvilSlot slot, String name){
            this.slot = slot;
            this.name = name;
        }

        public AnvilSlot getSlot(){
            return slot;
        }

        public String getName(){
            return name;
        }

        public boolean getWillDestroy(){
            return destroy;
        }

        public void setWillDestroy(boolean destroy){
            this.destroy = destroy;
        }
    }

    public interface AnvilClickEventListener {

        public void onAnvilClick(AnvilClickEvent event);

    }

}