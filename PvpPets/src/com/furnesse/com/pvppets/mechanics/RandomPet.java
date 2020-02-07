package com.furnesse.com.pvppets.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.managers.Pet;
import com.furnesse.com.pvppets.utils.Debug;

public class RandomPet {
	PvpPets plugin;

	public RandomPet(PvpPets plugin) {
		this.plugin = plugin;
	}

	public Map<String, List<Pet>> mysteryeggs = new HashMap<>();

	public List<Pet> createMysteryEgg(String egg) {
		mysteryeggs.put(egg, new ArrayList<Pet>());
		return mysteryeggs.get(egg);
	}

	public String getEgg(int tier) {
		Set<String> keys = mysteryeggs.keySet();
		String[] key = keys.toArray(new String[keys.size()]);

		Debug.log(key[0] + key[1] + key[2] + key[3]);
		if (key[tier - 1] != null) {
			return key[tier - 1];
		}
		return null;
	}

	public boolean addPetToEgg(Pet pet, int tier) {
		if (tier <= mysteryeggs.size()) {
			String egg = getEgg(tier);
			if (egg != null) {
				mysteryeggs.get(egg).add(pet);
				return true;
			}
		}
		plugin.getLogger().severe("Mystery egg not found: tier " + tier + " egg");
		return false;
	}

	public ItemStack getRandomMysteryPet(String egg) {
		if (mysteryeggs.get(egg) != null) {
			List<Pet> myegg = mysteryeggs.get(egg);

			int j = (new Random()).nextInt(myegg.size());
			return myegg.get(j).getItemStack();
		}

		return null;
	}

//	public ItemStack getRandomExclusivePet() {
//		int i = (new Random()).nextInt(exclusivePets.size());
//		return exclusivePets.get(i).getItemStack();
//	}
//
//	public ItemStack getRandomUltimatePet() {
//		int i = (new Random()).nextInt(ultimatePets.size());
//		return ultimatePets.get(i).getItemStack();
//	}
//
//	public ItemStack getRandomLegendaryPet() {
//		int i = (new Random()).nextInt(legendaryPets.size());
//		return legendaryPets.get(i).getItemStack();
//	}
//
//	public ItemStack getRandomAncientPet() {
//		int i = (new Random()).nextInt(ancientPets.size());
//		return ancientPets.get(i).getItemStack();
//	}
}
