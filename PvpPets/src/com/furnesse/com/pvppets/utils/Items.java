package com.furnesse.com.pvppets.utils;

import org.bukkit.inventory.ItemStack;

public enum Items
{
	EXCLUSIVE("PetShop.Exclusive.guiitem"), ULTIMATE("PetShop.Ultimate.guiitem"),
	LEGENDARY("PetShop.Legendary.guiitem"), ANCIENT("PetShop.Ancient.guiitem"),

	SPACER("PetShop.Spacer");

	private String path;
	private ItemStack item;

	Items(String path) {
		this.path = path;
		this.item = ItemUtils.loadItemFromConfig("config.yml", path);
	}

	public String getPath() {
		return path;
	}

	public ItemStack getItem() {
		return item;
	}
}
