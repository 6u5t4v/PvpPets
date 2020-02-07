package com.furnesse.com.pvppets.abilities;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.events.UsePetEvent;
import com.furnesse.com.pvppets.managers.Pet;
import com.furnesse.com.pvppets.mechanics.Leveling;
import com.furnesse.com.pvppets.utils.Lang;
import com.furnesse.com.pvppets.utils.Message;

public class RestockerPet implements Listener {
	PvpPets plugin = PvpPets.instance;
	private HashMap<String, Integer> cooldownTime = new HashMap<>();
	private HashMap<String, BukkitRunnable> cooldownTask = new HashMap<>();

	@EventHandler
	public void alchemistPet(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((plugin.getPetMan().isPet(p.getInventory().getItemInMainHand()))
				|| (plugin.getPetMan().isPet(p.getInventory().getItemInOffHand()))
						&& (e.getAction() == Action.RIGHT_CLICK_AIR)) {

			ItemStack petItem = plugin.getPetMan().isPet(p.getInventory().getItemInMainHand())
					? p.getInventory().getItemInMainHand()
					: p.getInventory().getItemInOffHand();

			if (plugin.getPetMan().getAbility(petItem) == AbilityType.RESTOCK) {

				Pet pet = plugin.getPetMan().getPet(petItem);

				if (this.cooldownTime.containsKey(p.getName())) {
					p.sendMessage(Message.ON_COOLDOWN.getMessage().replace("%petoncd%", pet.getName())
							.replace("%cooldown%", String.valueOf(this.cooldownTime.get(p.getName()))));
					return;
				}

				UsePetEvent event = new UsePetEvent(p, pet, petItem);
				Bukkit.getServer().getPluginManager().callEvent(event);

				if (!event.isCancelled()) {
					List<ItemStack> items = pet.getRestocklist();
					int size = items.size();
					int random = (new Random()).nextInt(size);
					ItemStack picked = items.get(random);

					if (Leveling.getLevel(petItem) <= 25) {
				        p.getInventory().addItem(new ItemStack[] { picked });
				      }
				      if (Leveling.getLevel(petItem) >= 26 && Leveling.getLevel(petItem) <= 49) {
				        p.getInventory().addItem(new ItemStack[] { picked });
				        p.getInventory().addItem(new ItemStack[] { picked });
				      } 
				      if (Leveling.getLevel(petItem) >= 50) {
				        p.getInventory().addItem(new ItemStack[] { picked });
				        p.getInventory().addItem(new ItemStack[] { picked });
				        p.getInventory().addItem(new ItemStack[] { picked });
				      } 

					p.sendMessage(
							Lang.chat(this.plugin.getConfig().getString("Pets." + pet.getName() + ".message")));
					Leveling.addExp(p, petItem, plugin.getPetMan().getDisplayname(petItem));

					plugin.wrapper.useAbilitySound(p);

					this.cooldownTime.put(p.getName(), Integer.valueOf(30));
					this.cooldownTask.put(p.getName(), new BukkitRunnable() {
						public void run() {
							RestockerPet.this.cooldownTime.put(p.getName(), Integer.valueOf(
									((Integer) RestockerPet.this.cooldownTime.get(p.getName())).intValue() - 1));
							if (((Integer) RestockerPet.this.cooldownTime.get(p.getName())).intValue() == 0) {
								RestockerPet.this.cooldownTime.remove(p.getName());
								RestockerPet.this.cooldownTask.remove(p.getName());
								cancel();
							}
						}
					});
					((BukkitRunnable) this.cooldownTask.get(p.getName())).runTaskTimer((Plugin) this.plugin, 60L, 60L);
				}
			}
		}
	}
}
