package com.furnesse.com.pvppets.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.abilities.AbilityType;
import com.furnesse.com.pvppets.utils.Debug;
import com.furnesse.com.pvppets.utils.Items;

public class Pets {

	PvpPets plugin;

	public Pets(PvpPets plugin) {
		this.plugin = plugin;
	}

	public List<Pet> pvppets = new ArrayList<>();
	public List<MysteryEgg> mysteryeggs = new ArrayList<>();

	public List<String> petitems = new ArrayList<>();
	public List<String> eggitems = new ArrayList<>();

	public void loadConfigSettings() {
		FileConfiguration conf = plugin.getConfig();
		pvppets.clear();
		mysteryeggs.clear();
		petitems.clear();
		eggitems.clear();

		for (String eggName : conf.getConfigurationSection("PetShop").getKeys(false)) {
			if (eggName != null) {
				String displayname = conf.getString("PetShop." + eggName + ".item.displayname");
				String material = conf.getString("PetShop." + eggName + ".item.material");
				List<String> lore = conf.getStringList("PetShop." + eggName + ".item.lore");
				if (displayname == null || material == null) {
					continue;
				}
				ItemStack item = plugin.wrapper.eggItem(eggName, material, displayname, lore);

				boolean purchasable = conf.getBoolean("PetShop." + eggName + ".purchasable");
				boolean useVault = conf.getBoolean("PetShop." + eggName + ".useVault");
				int price = conf.getInt("PetShop." + eggName + ".cost");

				List<Pet> contain = plugin.getRandPet().createMysteryEgg(eggName);

				ItemStack guiitem = Items.valueOf(eggName.toUpperCase()).getItem();
				Debug.log(guiitem.getItemMeta().getDisplayName());
				mysteryeggs.add(new MysteryEgg(eggName, price, useVault, purchasable, item, guiitem, contain));
				eggitems.add(eggName);
			}
		}

		for (String petName : conf.getConfigurationSection("Pets").getKeys(false)) {
			if (petName != null) {
				boolean inEgg = conf.getBoolean("Pets." + petName + ".eggSettings.inEgg");
				int tier = conf.getInt("Pets." + petName + ".eggSettings.tier");
				AbilityType ability = AbilityType.valueOf(conf.getString("Pets." + petName + ".ability"));
				String displayname = "";
				ItemStack item = null;
				try {
					displayname = conf.getString("Pets." + petName + ".item.displayname");
					String material = conf.getString("Pets." + petName + ".item.material");
					List<String> lore = conf.getStringList("Pets." + petName + ".item.lore");

					item = plugin.wrapper.petItem(petName, material, displayname, lore);
				} catch (Exception e) {
					// TODO: handle exception
					plugin.getLogger().severe("could not create item for " + petName);
					e.printStackTrace();
				}

				Pet pet = new Pet(petName, inEgg, tier, item, ability);

				if (!plugin.getRandPet().addPetToEgg(pet, tier)) {
					plugin.getLogger().severe("Could not create " + petName + ", please check config");
					continue;
				}

				pvppets.add(pet);
				petitems.add(petName);
			}
		}

		Debug.log("Loaded " + pvppets.size() + " pets");
		Debug.log("Loaded " + mysteryeggs.size() + " eggs");
	}

	public AbilityType getAbility(ItemStack item) {
		if (isPet(item)) {
			return getPet(item).getAbility();
		}

		return null;
	}

	public String getDisplayname(ItemStack item) {
		return item.getItemMeta().getCustomTagContainer().getCustomTag(plugin.wrapper.itemdisplayname,
				ItemTagType.STRING);
	}

	public boolean isPet(ItemStack item) {
		if (!isCustomItem(item))
			return false;

		Debug.log(item.getItemMeta().getCustomTagContainer().getCustomTag(plugin.wrapper.itemtype, ItemTagType.STRING));

		return item.getItemMeta().getCustomTagContainer().getCustomTag(plugin.wrapper.itemtype,
				ItemTagType.STRING) == "pet" ? true : false;
	}

	public boolean isEgg(ItemStack item) {
		if (!isCustomItem(item))
			return false;

		return item.getItemMeta().getCustomTagContainer().getCustomTag(plugin.wrapper.itemtype,
				ItemTagType.STRING) == "egg" ? true : false;
	}

	public boolean isCustomItem(ItemStack item) {
		if (item == null || item.getItemMeta() == null)
			return false;

		return item.getItemMeta().getCustomTagContainer().hasCustomTag(plugin.wrapper.itemtype, ItemTagType.STRING)
				? true
				: false;
	}

	public boolean isPet(String name) {
		for (Pet pet : pvppets) {
			if (pet.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPetWithAbility(Player p, AbilityType ability) {
		for (int i = 0; i < 36; i++) {
			if (isPet(p.getInventory().getItem(i)) && getPet(p.getInventory().getItem(i)).getAbility() == ability) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPet(Player p) {
		for (int i = 0; i < 36; i++) {
			if (isPet(p.getInventory().getItem(i))) {
				return true;
			}
		}
		return false;
	}

	public ItemStack getPet(Player p, AbilityType ability) {
		for (int i = 0; i < 36; i++) {
			if (getPet(p.getInventory().getItem(i)).getAbility() == ability) {
				return p.getInventory().getItem(i);
			}
		}
		return null;
	}

	public Pet getPet(ItemStack item) {
		if (!isCustomItem(item))
			return null;

		CustomItemTagContainer ctc = item.getItemMeta().getCustomTagContainer();

		return ctc.hasCustomTag(plugin.wrapper.itemname, ItemTagType.STRING)
				? getPet(ctc.getCustomTag(plugin.wrapper.itemname, ItemTagType.STRING))
				: null;
	}

	public ItemStack getPet(AbilityType ability) {
		for (Pet pet : pvppets) {
			if (pet.getAbility() == ability) {
				return pet.getItemStack();
			}
		}
		return null;
	}

	public String getName(ItemStack item) {
		return item.getItemMeta().getCustomTagContainer().getCustomTag(plugin.wrapper.itemname, ItemTagType.STRING);
	}

	public Pet getPet(String name) {
		for (Pet pet : pvppets) {
			if (pet.getName().equalsIgnoreCase(name)) {
				return pet;
			}
		}
		return null;
	}

	public MysteryEgg getMysteryEgg(String name) {
		for (MysteryEgg egg : mysteryeggs) {
			if (egg.getName().equalsIgnoreCase(name)) {
				return egg;
			}
		}
		return null;
	}

	public MysteryEgg getMysteryEgg(ItemStack item) {
		if (item == null || item.getItemMeta() == null)
			return null;

		CustomItemTagContainer ctc = item.getItemMeta().getCustomTagContainer();

		return ctc.hasCustomTag(plugin.wrapper.itemname, ItemTagType.STRING)
				? getMysteryEgg(ctc.getCustomTag(plugin.wrapper.itemname, ItemTagType.STRING))
				: null;
	}

	public MysteryEgg getMysteryEggFromGui(ItemStack item) {
		for (MysteryEgg egg : mysteryeggs) {
			if (egg.getGuiitem().equals(item)) {
				return egg;
			}
		}
		return null;
	}

	public void givePet(Player player, ItemStack item, int amount) {
		for (int i = 0; i < amount; i++) {
			player.getInventory().addItem(item);
		}
	}

	public void giveMysteryegg(Player p, ItemStack item, int amount) {
		for (int i = 0; i < amount; i++) {
			p.getInventory().addItem(item);
		}
	}
}
