package com.furnesse.com.pvppets.mechanics;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.utils.Lang;
import com.furnesse.com.pvppets.utils.Message;

public class Leveling {
	public static HashMap<String, Integer> progress = new HashMap<>();

	PvpPets plugin;

	public Leveling(PvpPets plugin) {
		this.plugin = plugin;
	}

	public static int getLevel(ItemStack item) {
		String petLevel = item.getItemMeta().getLore().get(7);
		String[] arrayOfString = petLevel.split(" ");
		int i = Integer.valueOf(arrayOfString[1]).intValue();
		return i;
	}

	public static void addExp(Player player, ItemStack petItem, String displayname) {
		List<String> lore = petItem.getItemMeta().getLore();
		ItemMeta meta = petItem.getItemMeta();
		String expString = lore.get(8);
		String[] expAmount = expString.split(" ");

		int currentExp = Integer.valueOf(expAmount[1]) + 10;
		int maximumExp = Integer.valueOf(expAmount[3]);
		if (progress.get(player.getName() + expAmount[1] + displayname) == null) {
			progress.put(player.getName() + expAmount[1] + displayname, 0);
		}
		lore.set(8, Lang.chat("&6&lEXP:&7 " + Integer.toString(currentExp) + " / " + Integer.toString(maximumExp)));

		int k = 50;
		int m = ((Integer) progress.get(player.getName() + expAmount[1] + displayname)).intValue();
		int n = maximumExp / 50 * 5;
		if (Integer.valueOf(expAmount[1]) % n == 0 && Integer.valueOf(expAmount[1]) != 0) {
			progress.put(player.getName() + expAmount[1] + displayname,
					progress.get(player.getName() + expAmount[1] + displayname) + 5);
		}

		StringBuffer expBar = new StringBuffer();
		expBar.append(ChatColor.YELLOW.toString());
		for (int i1 = 1; i1 <= m; i1++) {
			expBar.append('|');
		}
		int i1 = k - m;
		expBar.append(ChatColor.RED.toString());
		for (int i2 = 1; i2 <= i1; i2++) {
			expBar.append('|');
		}

		String str2 = expBar.toString();
		lore.set(9, str2);
		if (Integer.valueOf(expAmount[1]) % n == 0 || Integer.valueOf(expAmount[1]) != 0) {
			progress.put(player.getName() + currentExp + displayname,
					progress.get(player.getName() + expAmount[1] + displayname));
			progress.remove(player.getName() + expAmount[1] + displayname);
		}

		if (currentExp >= maximumExp) {
			String str3 = petItem.getItemMeta().getLore().get(7);
			String[] arrayOfString2 = str3.split(" ");
			progress.remove(String.valueOf(player.getName()) + currentExp + displayname);
			int i3 = Integer.valueOf(arrayOfString2[1]) + 1;
			meta.setDisplayName(Lang.chat(displayname + " &7[LVL" + Integer.toString(i3) + "]"));

			player.sendMessage(
					Message.PET_LEVEL_UP.getMessage().replace("%pet%", PvpPets.instance.getPetMan().getName(petItem)));

			lore.set(7, Lang.chat("&6&lLevel:&7 " + Integer.toString(i3)));
			lore.set(8, Lang.chat("&6&lEXP:&7 0 / " + Integer.toString(maximumExp + 200)));
			lore.set(9, Lang.chat("&c||||||||||||||||||||||||||||||||||||||||||||||||||"));
		}
		meta.setLore(lore);
		petItem.setItemMeta(meta);
	}
}
