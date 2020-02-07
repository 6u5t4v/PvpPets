package com.furnesse.com.pvppets.abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.managers.Pet;
import com.furnesse.com.pvppets.mechanics.Leveling;
import com.furnesse.com.pvppets.utils.Debug;

public class PotionEffectPets extends BukkitRunnable {
	PvpPets plugin;

	public PotionEffectPets(PvpPets plugin) {
		this.plugin = plugin;
	}

	public void run() {

		for (Player player : Bukkit.getOnlinePlayers()) {
			for (int i = 0; i < 36; i++) {
				if (plugin.getPetMan().isPet(player.getInventory().getItem(i))) {
					ItemStack petItem = player.getInventory().getItem(i);
					Debug.log("test? " + petItem.getItemMeta().getDisplayName());

//					if (petItem == null)
//						continue;

					Pet pet = plugin.getPetMan().getPet(petItem);
					Debug.log(pet.getName());

					if (pet.getAbility() == AbilityType.PASSIVE) {
						Debug.log("is passive");

						if (Leveling.getLevel(petItem) <= 20) {
							player.addPotionEffect(new PotionEffect(pet.getPassiveability(), 400, 0));
						}
						if (Leveling.getLevel(petItem) >= 21 && Leveling.getLevel(petItem) <= 59) {
							player.addPotionEffect(new PotionEffect(pet.getPassiveability(), 400, 1));
						}
						if (Leveling.getLevel(petItem) >= 60) {
							player.addPotionEffect(new PotionEffect(pet.getPassiveability(), 400, 2));
						}

						Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));
					}
				}
			}
		}
	}
}