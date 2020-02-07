package com.furnesse.com.pvppets.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.furnesse.com.pvppets.PvpPets;

public class ItemUtils {

	static PvpPets plugin = PvpPets.instance;

	public static ItemStack loadItemFromConfig(String configName, String path) {
		String mat = null;
		if (plugin.getConfig().get(path + ".material") != null) {
			mat = plugin.getConfig().getString(path + ".material");
		} else {
			plugin.getLogger().severe(path + " material cannot be null, please check config.yml");
			return null;
		}

		String displayname;
		if (plugin.getConfig().get(path + ".displayname") != null) {
			displayname = plugin.getConfig().getString(path + ".displayname");
		} else {
			plugin.getLogger().severe("displayname cannot be null, please check config.yml");
			return null;
		}

		List<String> lore = new ArrayList<>();
		if (plugin.getConfig().get(path + ".lore") != null) {
			lore = plugin.getConfig().getStringList(path + ".lore");
		}

		return create(mat, displayname, lore);
	}

	public static List<String> convertLore(List<String> list) {
		List<String> lore = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				lore.add(Lang.chat(list.get(i)));
			}
		}
		return lore;
	}

	public static ItemStack create(String mat, String displayname, List<String> lore) {
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

		ItemStack is = PvpPets.instance.headAPI == null && mat.startsWith("hdb-") ? plugin.wrapper.getItemHead(hdbId)
				: PvpPets.instance.headAPI != null && mat.startsWith("hdb-")
						? PvpPets.instance.headAPI.getItemHead(hdbId)
						: new ItemStack(m, 1);
		ItemMeta im = is.getItemMeta();

		im.setDisplayName(Lang.chat(displayname));
		im.setLore(convertLore(lore));
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		is.setItemMeta(im);
		return is;
	}
}
