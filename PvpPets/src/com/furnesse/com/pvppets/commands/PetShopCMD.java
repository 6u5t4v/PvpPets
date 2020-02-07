package com.furnesse.com.pvppets.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.managers.MysteryEgg;
import com.furnesse.com.pvppets.utils.Items;
import com.furnesse.com.pvppets.utils.Message;

public class PetShopCMD implements Listener, CommandExecutor {
	PvpPets plugin = PvpPets.instance;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			plugin.wrapper.openShopSound(player);
			player.openInventory(plugin.petshop);
		} else {
			sender.sendMessage("This is a player command");
		}
		return true;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		ItemStack itemClicked = e.getCurrentItem();

		if (e.getClickedInventory() == null) {
			return;
		}

		if (e.getInventory().equals(plugin.petshop)) {
			e.setCancelled(true);

			if (itemClicked == null || itemClicked.getType() == null || itemClicked.getItemMeta() == null
					|| itemClicked.equals(Items.SPACER.getItem()))
				return;

			if (plugin.getPetMan().getMysteryEggFromGui(itemClicked) != null) {
				MysteryEgg egg = plugin.getPetMan().getMysteryEggFromGui(itemClicked);
				if (egg.isPurchasable()) {
					if (player.getInventory().firstEmpty() == -1) {
						player.sendMessage(Message.FULL_INVENTORY.getMessage());
						return;
					}

					if (player.getLevel() >= egg.getPrice()) {
						player.setLevel(player.getLevel() - egg.getPrice());
						player.sendMessage(Message.PURCHASE.getChatMessage().replace("%mysteryegg%", egg.getName())
								.replace("%cost%", String.valueOf(egg.getPrice())));

						plugin.getPetMan().giveMysteryegg(player, egg.getItemstack(), 1);

						plugin.wrapper.successfulBuySound(player);
					} else {
						player.sendMessage(Message.CANNOT_AFFORD.getChatMessage());
						plugin.wrapper.unsuccessfulBuySound(player);
					}
				} else {
					player.sendMessage(Message.CANNOT_BE_PURCHASED.getChatMessage());
				}
			}
		}
	}
}
