package me.mani.clcore.util;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilders {

	public static class NormalItemBuilder {
	
		protected Material material;
		protected int amount = 1;
		protected short durability = 0;
		protected String displayName;
		protected List<String> lore;
		protected Map<Enchantment, Integer> enchantments;
		protected ItemFlag[] hiddenFlags;
		
		protected NormalItemBuilder(Material material) {
			this.material = material;
		}
	
		public ItemStack build() {
			ItemStack itemStack = new ItemStack(material, amount, durability);
			if (displayName != null || lore != null || enchantments != null || hiddenFlags != null) {
				ItemMeta itemMeta = itemStack.getItemMeta();
				if (displayName != null)
					itemMeta.setDisplayName(displayName);
				if (lore != null)
					itemMeta.setLore(lore);
				if (enchantments != null)
					enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));
				if (hiddenFlags != null)
					itemMeta.addItemFlags(hiddenFlags);
				itemStack.setItemMeta(itemMeta);
			}
			Material.FIREWORK.ordinal();
			return itemStack;
		}
		
		public NormalItemBuilder material(Material material) {
			this.material = material;
			return this;
		}
		
		public NormalItemBuilder amount(int amount) {
			this.amount = amount;
			return this;
		}
		
		public NormalItemBuilder durability(short durability) {
			this.durability = durability;
			return this;
		}
		
		public NormalItemBuilder name(String displayName) {
			this.displayName = displayName;
			return this;
		}
		
		public NormalItemBuilder lore(List<String> lore) {
			this.lore = lore;
			return this;
		}
		
		public NormalItemBuilder enchant(Enchantment enchantment, int level) {
			if (enchantments == null)
				enchantments = new HashMap<>();
			enchantments.put(enchantment, level);
			return this;
		}

		public NormalItemBuilder hide(ItemFlag... hiddenFlags) {
			this.hiddenFlags = hiddenFlags;
			return this;
		}
		
	}

	public static class FireworkItemBuilder extends NormalItemBuilder {

		protected FireworkEffect fireworkEffect;

		protected FireworkItemBuilder() {
			super(Material.FIREWORK_CHARGE);
		}

		@Override
		public ItemStack build() {
			ItemStack itemStack = super.build();
			if (fireworkEffect != null) {
				FireworkEffectMeta itemMeta = (FireworkEffectMeta) itemStack.getItemMeta();
				itemMeta.setEffect(fireworkEffect);
				itemStack.setItemMeta(itemMeta);
			}
			return itemStack;
		}

		public FireworkItemBuilder effect(FireworkEffect fireworkEffect) {
			this.fireworkEffect = fireworkEffect;
			return this;
		}

	}
	
	public static NormalItemBuilder normal(Material material) {
		return new NormalItemBuilder(material);
	}

	public static FireworkItemBuilder firework() {
		return new FireworkItemBuilder();
	}
	
}
