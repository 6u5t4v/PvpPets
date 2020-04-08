package com.furnesse.com.pvppets.abilities;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.events.UsePetEvent;
import com.furnesse.com.pvppets.managers.Pet;
import com.furnesse.com.pvppets.mechanics.Leveling;
import com.furnesse.com.pvppets.utils.Lang;
import com.furnesse.com.pvppets.utils.Message;

public class IronSkinAbility implements Listener {
	PvpPets plugin = PvpPets.instance;

	private HashMap<String, Integer> cooldownTime = new HashMap<>();
	private HashMap<String, BukkitRunnable> cooldownTask = new HashMap<>();

	@EventHandler
	public void ironSkinAbility(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((plugin.getPetMan().isPet(p.getInventory().getItemInMainHand()))
				|| (plugin.getPetMan().isPet(p.getInventory().getItemInOffHand()))
						&& (e.getAction() == Action.RIGHT_CLICK_AIR)) {

			ItemStack mainhand = p.getInventory().getItemInMainHand();
			ItemStack offhand = p.getInventory().getItemInOffHand();

			ItemStack petItem = plugin.getPetMan().isPet(mainhand) ? mainhand : offhand;

			if (plugin.getPetMan().getAbility(petItem) == AbilityType.IRONSKIN) {
				Pet pet = plugin.getPetMan().getPet(petItem);

				if (this.cooldownTime.containsKey(p.getName())) {
					p.sendMessage(Message.ON_COOLDOWN.getMessage().replace("%petoncd%", pet.getName())
							.replace("%cooldown%", String.valueOf(this.cooldownTime.get(p.getName()))));
					return;
				}

				UsePetEvent event = new UsePetEvent(p, pet, petItem, pet.getAbility());
				Bukkit.getServer().getPluginManager().callEvent(event);

				if (!event.isCancelled()) {
					if (Leveling.getLevel(petItem) <= 20) {
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 0));
					}
					if (Leveling.getLevel(petItem) >= 21 && Leveling.getLevel(petItem) <= 39) {
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
					}
					if (Leveling.getLevel(petItem) >= 40 && Leveling.getLevel(petItem) <= 59) {
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 2));
					}
					if (Leveling.getLevel(petItem) >= 60) {
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 3));
					}

					p.sendMessage(Lang.chat(plugin.getConfig().getString("Pets." + pet.getName() + ".message")));
					Leveling.addExp(p, petItem, plugin.getPetMan().getDisplayname(petItem));
					this.cooldownTime.put(p.getName(), Integer.valueOf(20));
					this.cooldownTask.put(p.getName(), new BukkitRunnable() {
						public void run() {
							IronSkinAbility.this.cooldownTime.put(p.getName(), Integer.valueOf(
									((Integer) IronSkinAbility.this.cooldownTime.get(p.getName())).intValue() - 1));
							if (((Integer) IronSkinAbility.this.cooldownTime.get(p.getName())).intValue() == 0) {
								IronSkinAbility.this.cooldownTime.remove(p.getName());
								IronSkinAbility.this.cooldownTask.remove(p.getName());
								cancel();
							}
						}
					});
					((BukkitRunnable) this.cooldownTask.get(p.getName())).runTaskTimer((Plugin) this.plugin, 40L, 40L);
				}
			}
		}
	}
}
