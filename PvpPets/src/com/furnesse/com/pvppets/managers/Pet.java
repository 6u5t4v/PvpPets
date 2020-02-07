package com.furnesse.com.pvppets.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.abilities.AbilityType;
import com.furnesse.com.pvppets.utils.Debug;

public class Pet {

	private String name;
	private boolean inEgg;
	private int tier;
	private ItemStack itemstack;
//	private String displayname;
	private AbilityType ability;
	private PotionEffectType passiveability;
	private List<ItemStack> restocklist;

	public Pet(String name, boolean inEgg, int tier, ItemStack itemstack, AbilityType ability) {
		this.name = name;
		this.inEgg = inEgg;
		this.tier = tier;
		this.itemstack = itemstack;
//		this.displayname = displayname;
		this.ability = ability;

		this.passiveability = potionEffect();
		this.restocklist = createRestockList();
	}

	public List<ItemStack> getRestocklist() {
		return restocklist;
	}

	public PotionEffectType potionEffect() {
		if (ability == AbilityType.PASSIVE) {
			try {
				if (PvpPets.instance.getConfig().get("Pets." + this.name + ".potioneffect") != null) {
					return PotionEffectType
							.getByName(PvpPets.instance.getConfig().getString("Pets." + this.name + ".potioneffect"));
				} else {
					PvpPets.instance.getLogger().severe("Make sure you have set a potioneffect for " + this.name
							+ ", please stop the server a fix otherwise you will hit errors");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		return null;
	}

	public List<ItemStack> createRestockList() {
		if (ability == AbilityType.RESTOCK) {
			try {
				if (PvpPets.instance.getConfig().get("Pets." + this.name + ".itemslist") != null) {
					List<ItemStack> items = new ArrayList<>();
					for (String str : PvpPets.instance.getConfig().getStringList("Pets." + this.name + ".itemslist")) {
						String[] itemStr = str.split(";", 2);
						int amount = Integer.valueOf(itemStr[1]);

						ItemStack item = null;

						if (itemStr[0].startsWith("potion(") && itemStr[0].endsWith(")")) {
							String potionStr = itemStr[0].substring(7, itemStr[0].length() - 1);
							String[] potion = potionStr.split(",", 3);
							Debug.log("effect: " + potion[0] + " | level: " + potion[1] + " | splash: " + potion[2]);
							try {
								PotionType type = PotionType.valueOf(potion[0]);
								int level = Integer.valueOf(potion[1]);
								boolean splash = Boolean.valueOf(potion[2]);

								item = new Potion(type, level, splash, false).toItemStack(amount);

							} catch (NullPointerException e) {
								PvpPets.instance.getLogger().severe(
										"Could not create " + potion[0] + " for " + this.name + " pet, skipping");
								continue;
							}
						} else {
							item = new ItemStack(Material.getMaterial(itemStr[0]), amount);
						}

						if (item != null) {
							items.add(item);
						} else {
							Debug.log("test " + str);
						}
					}
					return items;
				} else {
					PvpPets.instance.getLogger().severe("Make sure you have created an itemslist for " + this.name
							+ ", please stop the server a fix otherwise you will hit errors");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return null;
	}

	public PotionEffectType getPassiveability() {
		return passiveability;
	}

	public AbilityType getAbility() {
		return ability;
	}

//	public String getDisplayname() {
//		return displayname;
//	}

	public String getName() {
		return name;
	}

	public boolean isInEgg() {
		return inEgg;
	}

	public int getTier() {
		return tier;
	}

	public ItemStack getItemStack() {
		return itemstack;
	}
}
