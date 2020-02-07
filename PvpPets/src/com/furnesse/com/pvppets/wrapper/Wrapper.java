package com.furnesse.com.pvppets.wrapper;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.furnesse.com.pvppets.PvpPets;

public abstract class Wrapper {

	public NamespacedKey itemname = new NamespacedKey(PvpPets.instance, "name");
	public NamespacedKey itemtype = new NamespacedKey(PvpPets.instance, "type");
	public NamespacedKey itemdisplayname = new NamespacedKey(PvpPets.instance, "displayname");

	public ItemStack getItemHead(String val) {
		ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta sm = (SkullMeta) is.getItemMeta();

		sm.setOwner(val);
		is.setItemMeta(sm);

		return is;

	}

//	public abstract ItemStack createPotionItem(PotionType type, int level, boolean splash, int amount);

	public abstract void openShopSound(Player player);

	public abstract void successfulBuySound(Player player);

	public abstract void unsuccessfulBuySound(Player player);

	public abstract void openMysteryeggSound(Player player);

	public abstract void useAbilitySound(Player player);

	public abstract void dodgeAttackSound(Player player);

	public abstract void closeDeathSound(Player player);

	public abstract void closeDeath2Sound(Player player);

	public abstract void stealfoodSound(Player player);

	public abstract ItemStack petItem(String name, String mat, String displayname, List<String> lore);

	public abstract ItemStack eggItem(String name, String mat, String displayname, List<String> lore);

	public abstract ItemStack guiItem(String mat, String displayname, List<String> lore);
}
