package com.furnesse.com.pvppets.utils;

import org.bukkit.ChatColor;

public class Lang {
	public static String chat(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}
