package com.furnesse.com.pvppets.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.events.OpenMysteryEgg;
import com.furnesse.com.pvppets.managers.MysteryEgg;
import com.furnesse.com.pvppets.utils.Message;

public class Events implements Listener {
	PvpPets plugin = PvpPets.instance;

	@EventHandler
	public void onPlacePetAttempt(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		ItemStack mainhand = p.getInventory().getItemInMainHand();
		ItemStack offhand = p.getInventory().getItemInOffHand();

		if (plugin.getPetMan().isCustomItem(mainhand) || plugin.getPetMan().isCustomItem(offhand)) {
			e.setCancelled(true);

			ItemStack item = plugin.getPetMan().isCustomItem(mainhand) ? mainhand : offhand;

			p.sendMessage(Message.CANNOT_PLACE.getMessage().replace("%item%", plugin.getPetMan().getName(item)));
		}
	}

	@EventHandler
	public void onPlayerillegalInteractionPetOrEgg(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack mainhand = p.getInventory().getItemInMainHand();
		ItemStack offhand = p.getInventory().getItemInOffHand();

		if (plugin.getPetMan().isCustomItem(mainhand) || plugin.getPetMan().isCustomItem(offhand)) {
			ItemStack item = plugin.getPetMan().isCustomItem(mainhand) ? mainhand : offhand;

			if (item.getType().isInteractable()) {
				e.setCancelled(true);
				p.sendMessage(
						Message.ILLEGAL_INTERACTION.getMessage().replace("%item%", plugin.getPetMan().getName(item)));
			}
		}
	}

	@EventHandler
	public void onPlayerMysteryeggInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			ItemStack mainhand = p.getInventory().getItemInMainHand();
			ItemStack offhand = p.getInventory().getItemInOffHand();

			if ((plugin.getPetMan().isEgg(mainhand)) || (plugin.getPetMan().isEgg(offhand))) {

				ItemStack mysteryegg = plugin.getPetMan().isEgg(mainhand) ? mainhand : offhand;

				if (p.getInventory().firstEmpty() == -1) {
					p.sendMessage(Message.FULL_INVENTORY.getChatMessage());
					return;
				}

				if (mysteryegg.getAmount() > 1) {
					p.sendMessage(Message.UNSTACK_EGGS.getChatMessage());
					return;
				}

				MysteryEgg egg = plugin.getPetMan().getMysteryEgg(mysteryegg);

				OpenMysteryEgg event = new OpenMysteryEgg(p, egg, mysteryegg);
				Bukkit.getServer().getPluginManager().callEvent(event);

				if (!event.isCancelled()) {
					p.getInventory().removeItem(mysteryegg);

					plugin.wrapper.openMysteryeggSound(p);

					ItemStack rewardEgg = plugin.getRandPet().getRandomMysteryPet(egg.getName());

					p.getInventory().addItem(new ItemStack[] { rewardEgg });

					plugin.wrapper.openMysteryeggSound(p);

					p.sendMessage(Message.RECEIVE_RANDOM_PET.getChatMessage().replace("%mysterypet%",
							plugin.getPetMan().getName(rewardEgg)));
				}
			}
		}
	}
}