package com.furnesse.com.pvppets.managers;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class MysteryEgg {

	private String name;
	private int price;
	private boolean useVault;
	private boolean purchasable;
	private ItemStack itemstack;
	private ItemStack guiitem;
	private List<Pet> contains;

	public MysteryEgg(String name, int price, boolean useVault, boolean purchasable, ItemStack itemstack, ItemStack guiitem, List<Pet> contains) {
		this.name = name;
		this.price = price;
		this.useVault = useVault;
		this.purchasable = purchasable;
		this.itemstack = itemstack;
		this.contains = contains;
		this.guiitem = guiitem;
	}

	public ItemStack getGuiitem() {
		return guiitem;
	}

	public ItemStack getItemstack() {
		return itemstack;
	}

	public boolean isPurchasable() {
		return purchasable;
	}

	public List<Pet> getContains() {
		return contains;
	}

	public void setContains(List<Pet> contains) {
		this.contains = contains;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public boolean isUseVault() {
		return useVault;
	}
}
