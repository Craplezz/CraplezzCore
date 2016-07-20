package me.mani.clcore.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class InventoryUtils {

    public static void fillUp(Inventory inventory, ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++)
            if (inventory.getItem(i) == null)
                inventory.setItem(i, itemStack);
    }

}
