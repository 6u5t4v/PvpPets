package com.furnesse.com.pvppets.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.utils.Lang;

public class Wrapper1_15 extends Wrapper {

	@Override
	public void openShopSound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
	}

	@Override
	public void successfulBuySound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_YES, 1.0F, 1.0F);

	}

	@Override
	public void unsuccessfulBuySound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
	}

	@Override
	public void openMysteryeggSound(Player player) {
		// TODO Auto-generated method stub
		player.playSound(player.getEyeLocation(), Sound.ENTITY_CAT_HISS, 1.0F, 1.0F);
	}

	@Override
	public void useAbilitySound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);

	}

	@Override
	public void dodgeAttackSound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
	}

	@Override
	public void closeDeathSound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.ENTITY_CAT_HURT, 1.0F, 1.0F);

	}

	@Override
	public void closeDeath2Sound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.ENTITY_BAT_AMBIENT, 1.0F, 1.0F);
	}

	@Override
	public void stealfoodSound(Player player) {
		player.playSound(player.getEyeLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.0F, 1.0F);
	}

	@Override
	public ItemStack petItem(String name, String mat, String displayname, List<String> lore) {
		String hdbId = null;

		Material m = null;
		if (mat.startsWith("hdb-")) {
			String[] hdbItem = mat.split("-", 2);
			hdbId = hdbItem[1];
		} else if (Material.getMaterial(mat) != null) {
			m = Material.getMaterial(mat);
		} else {
			PvpPets.instance.getLogger().severe("Could not find a valid material for " + displayname);
			return null;
		}

		/*
		 * Checks: 1. If HeadAPI is available and the mat starts with "hdb-" then set a
		 * custom head with hdbId. 2. Else if HeadAPI is not available, but the mat
		 * starts with "hdb-" then use a player head with hdbId as val. 3. Else create
		 * item from material.
		 */
		ItemStack is = PvpPets.instance.headAPI == null && mat.startsWith("hdb-") ? getItemHead(hdbId)
				: PvpPets.instance.headAPI != null && mat.startsWith("hdb-")
						? PvpPets.instance.headAPI.getItemHead(hdbId)
						: new ItemStack(m, 1);
		ItemMeta im = is.getItemMeta();

		im.setDisplayName(Lang.chat(displayname + " &7[LVL 0]"));
		List<String> newLore = new ArrayList<>();
		for (String str : lore) {
			newLore.add(Lang.chat(str));
		}

		newLore.add(Lang.chat("&6&lLevel:&7 0"));
		newLore.add(Lang.chat("&6&lEXP:&7 0 / 200"));
		newLore.add(Lang.chat("&c||||||||||||||||||||||||||||||||||||||||||||||||||"));

		im.setLore(newLore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		CustomItemTagContainer ctc = im.getCustomTagContainer();
		ctc.setCustomTag(itemname, ItemTagType.STRING, name);
		ctc.setCustomTag(itemtype, ItemTagType.STRING, "pet");
		ctc.setCustomTag(itemdisplayname, ItemTagType.STRING, displayname);

		is.setItemMeta(im);
		return is;
	}

	@Override
	public ItemStack eggItem(String name, String mat, String displayname, List<String> lore) {
		String hdbId = null;

		Material m = null;
		if (mat.startsWith("hdb-")) {
			String[] hdbItem = mat.split("-", 2);
			hdbId = hdbItem[1];
		} else if (Material.getMaterial(mat) != null) {
			m = Material.getMaterial(mat);
		} else {
			PvpPets.instance.getLogger().severe("Could not find a valid material for " + displayname);
			return null;
		}

		ItemStack is = PvpPets.instance.headAPI == null && mat.startsWith("hdb-") ? getItemHead(hdbId)
				: PvpPets.instance.headAPI != null && mat.startsWith("hdb-")
						? PvpPets.instance.headAPI.getItemHead(hdbId)
						: new ItemStack(m, 1);
		ItemMeta im = is.getItemMeta();

		im.setDisplayName(Lang.chat(displayname));
		List<String> newLore = new ArrayList<>();
		for (String str : lore) {
			newLore.add(Lang.chat(str));
		}

		im.setLore(newLore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		CustomItemTagContainer ctc = im.getCustomTagContainer();
		ctc.setCustomTag(itemname, ItemTagType.STRING, name);
		ctc.setCustomTag(itemtype, ItemTagType.STRING, "egg");
		ctc.setCustomTag(itemdisplayname, ItemTagType.STRING, displayname);

		is.setItemMeta(im);
		return is;
	}

	@Override
	public ItemStack guiItem(String mat, String displayname, List<String> lore) {
		String hdbId = null;

		Material m = null;
		if (mat.startsWith("hdb-")) {
			String[] hdbItem = mat.split("-", 2);
			hdbId = hdbItem[1];
		} else if (Material.getMaterial(mat) != null) {
			m = Material.getMaterial(mat);
		} else {
			PvpPets.instance.getLogger().severe("Could not find a valid material for " + displayname);
			return null;
		}

		ItemStack is = PvpPets.instance.headAPI == null && mat.startsWith("hdb-") ? getItemHead(hdbId)
				: PvpPets.instance.headAPI != null && mat.startsWith("hdb-")
						? PvpPets.instance.headAPI.getItemHead(hdbId)
						: new ItemStack(m, 1);
		ItemMeta im = is.getItemMeta();

		List<String> newLore = new ArrayList<>();
		for (String str : lore) {
			newLore.add(Lang.chat(str));
		}

		im.setLore(newLore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		is.setItemMeta(im);
		return is;
	}
}
