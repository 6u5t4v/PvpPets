package com.furnesse.com.pvppets.abilities;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.events.UsePetEvent;
import com.furnesse.com.pvppets.managers.Pet;
import com.furnesse.com.pvppets.mechanics.Leveling;
import com.furnesse.com.pvppets.utils.Lang;

public class CombatDamagePets implements Listener {
	Random rand = new Random();
	PvpPets plugin = PvpPets.instance;

	@EventHandler
	public void dodgeAbility(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();

			for (int i = 0; i < 9; i++) {
				if (!plugin.getPetMan().isPet(p.getInventory().getItem(i))) {
					return;
				}

				ItemStack petItem = p.getInventory().getItem(i);

				if (plugin.getPetMan().getAbility(petItem) == AbilityType.DODGE) {
					Pet pet = plugin.getPetMan().getPet(petItem);

					if (Leveling.getLevel(petItem) <= 15 && this.rand.nextInt(100) <= 2) {
						dodge(p, damager, petItem, pet, e);
					}

					if (Leveling.getLevel(petItem) >= 16 && Leveling.getLevel(petItem) <= 29
							&& this.rand.nextInt(1000) <= 35) {
						dodge(p, damager, petItem, pet, e);
					}

					if (Leveling.getLevel(petItem) >= 30 && this.rand.nextInt(100) <= 5) {
						dodge(p, damager, petItem, pet, e);
					}
				}
			}
		}
	}

	private void dodge(Player p, Player damager, ItemStack item, Pet pet, EntityDamageByEntityEvent e) {
		UsePetEvent event = new UsePetEvent(p, pet, item, pet.getAbility());
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			e.setCancelled(true);

			Leveling.addExp(p, item, plugin.getPetMan().getDisplayname(item));

			p.sendMessage(Lang.chat(plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));
			damager.sendMessage(Lang.chat("&c*** &4&n" + p.getName() + "&c dodged your attack ***"));

			plugin.wrapper.dodgeAttackSound(p);
		}
	}

	private void closeDeath(Player player, ItemStack item, Pet pet, Double heal) {
		UsePetEvent event = new UsePetEvent(player, pet, item, pet.getAbility());
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			player.setHealth(player.getHealth() + heal);
			player.sendMessage(Lang.chat(plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));
			Leveling.addExp(player, item, plugin.getPetMan().getDisplayname(item));

			plugin.wrapper.closeDeathSound(player);

		}
	}

	@EventHandler
	public void closeDeathAbility(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player player = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			for (int i = 0; i < 9; i++) {
				if (!plugin.getPetMan().isPet(player.getInventory().getItem(i))) {
					return;
				}

				ItemStack petItem = player.getInventory().getItem(i);

				if (plugin.getPetMan().getAbility(petItem) == AbilityType.CLOSEDEATH) {
					Pet pet = plugin.getPetMan().getPet(petItem);

					if (this.rand.nextInt(100) <= 30) {
						Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));
					}

					if (Leveling.getLevel(petItem) <= 15 && this.rand.nextInt(100) <= 10
							&& player.getHealth() <= 6.0D) {
						closeDeath(player, petItem, pet, 4.0D);
					}

					if (Leveling.getLevel(petItem) >= 16 && Leveling.getLevel(petItem) <= 29
							&& this.rand.nextInt(100) <= 15 && player.getHealth() <= 6.0D) {
						player.setHealth(player.getHealth() + 5.0D);
						closeDeath(player, petItem, pet, 5.0D);
					}

					if (Leveling.getLevel(petItem) >= 30 && this.rand.nextInt(100) <= 20
							&& player.getHealth() <= 6.0D) {
						closeDeath(player, petItem, pet, 6.0D);
					}
				}

				if (plugin.getPetMan().getAbility(petItem) == AbilityType.CLOSEDEATH2) {
					List<Entity> nearbyEntities = player.getNearbyEntities(5.0D, 5.0D, 5.0D);
					if (nearbyEntities.size() > 0) {
						for (Entity yoink : nearbyEntities) {

							Pet pet = plugin.getPetMan().getPet(petItem);

							if (this.rand.nextInt(100) <= 15) {
								Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));
							}
							if (Leveling.getLevel(petItem) <= 15 && this.rand.nextInt(100) <= 5
									&& player.getHealth() <= 6.0D) {
								UsePetEvent event = new UsePetEvent(player, pet, petItem, pet.getAbility());
								Bukkit.getServer().getPluginManager().callEvent(event);

								if (!event.isCancelled()) {
									player.setHealth(player.getHealth() + 4.0D);
									((Player) yoink)
											.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
									((Player) yoink).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
									((Player) yoink).playEffect(damager.getEyeLocation(), Effect.SMOKE, 200);
									player.sendMessage(Lang.chat(
											plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));
									Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));
									plugin.wrapper.closeDeath2Sound(player);
								}
							}

							if (Leveling.getLevel(petItem) >= 16 && Leveling.getLevel(petItem) <= 29
									&& this.rand.nextInt(100) <= 10 && player.getHealth() <= 6.0D) {
								UsePetEvent event = new UsePetEvent(player, pet, petItem, pet.getAbility());
								Bukkit.getServer().getPluginManager().callEvent(event);

								if (!event.isCancelled()) {
									player.setHealth(player.getHealth() + 5.0D);
									((Player) yoink).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 2));
									((Player) yoink).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 2));
									((Player) yoink).playEffect(damager.getEyeLocation(), Effect.SMOKE, 200);
									player.sendMessage(Lang.chat(
											plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));
									Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));
									plugin.wrapper.closeDeath2Sound(player);
								}
							}

							if (Leveling.getLevel(petItem) >= 30 && this.rand.nextInt(100) <= 15
									&& player.getHealth() <= 6.0D) {
								UsePetEvent event = new UsePetEvent(player, pet, petItem, pet.getAbility());
								Bukkit.getServer().getPluginManager().callEvent(event);

								if (!event.isCancelled()) {
									player.setHealth(player.getHealth() + 5.0D);
									((Player) yoink).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 2));
									((Player) yoink).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
									((Player) yoink).playEffect(damager.getEyeLocation(), Effect.SMOKE, 200);
									player.sendMessage(Lang.chat(
											plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));
									Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));
									plugin.wrapper.closeDeath2Sound(player);
								}
							}

						}
					}
				}

				if (plugin.getPetMan().getAbility(petItem) == AbilityType.STEALFOOD) {
					Pet pet = plugin.getPetMan().getPet(petItem);
					if (Leveling.getLevel(petItem) <= 15 && this.rand.nextInt(100) <= 1) {
						UsePetEvent event = new UsePetEvent(player, pet, petItem, pet.getAbility());
						Bukkit.getServer().getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
							damager.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 160, 2));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 160, 2));

							player.sendMessage(
									Lang.chat(plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));

							Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));

							plugin.wrapper.stealfoodSound(player);
						}
					}

					if (Leveling.getLevel(petItem) >= 16 && Leveling.getLevel(petItem) <= 29
							&& this.rand.nextInt(1000) <= 25) {
						UsePetEvent event = new UsePetEvent(player, pet, petItem, pet.getAbility());
						Bukkit.getServer().getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
							damager.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 220, 3));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 220, 3));

							player.sendMessage(
									Lang.chat(plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));

							Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));

							plugin.wrapper.stealfoodSound(player);
						}
					}
					if (Leveling.getLevel(petItem) >= 30 && this.rand.nextInt(100) <= 3) {
						UsePetEvent event = new UsePetEvent(player, pet, petItem, pet.getAbility());
						Bukkit.getServer().getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
							damager.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 300, 4));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 300, 4));

							player.sendMessage(
									Lang.chat(plugin.getConfig().getString("Pets." + pet.getName() + ".item.message")));

							Leveling.addExp(player, petItem, plugin.getPetMan().getDisplayname(petItem));

							plugin.wrapper.stealfoodSound(player);
						}
					}
				}
			}
		}
	}
}
