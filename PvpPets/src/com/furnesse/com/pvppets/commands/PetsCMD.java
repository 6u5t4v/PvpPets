package com.furnesse.com.pvppets.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.managers.MysteryEgg;
import com.furnesse.com.pvppets.managers.Pet;
import com.furnesse.com.pvppets.utils.Lang;
import com.furnesse.com.pvppets.utils.Message;

public class PetsCMD implements CommandExecutor {
	PvpPets plugin = PvpPets.instance;

	public void sendHelp(CommandSender sender) {
		for (String str : plugin.getConfig().getStringList("Messages.COMMAND_FORMAT")) {
			sender.sendMessage(Lang.chat(str).replace("%pets%", plugin.getPetMan().petitems.toString())
					.replace("%mysteryeggs%", plugin.getPetMan().eggitems.toString()));
		}
	}

	private void givePet(CommandSender sender, Player target, Pet pet, int amount) {
		plugin.getPetMan().givePet(target, pet.getItemStack(), amount);
		target.sendMessage(Message.ITEM_RECEIVE.getChatMessage().replace("%item%", pet.getName()));
		sender.sendMessage(Message.ITEM_GIVE.getChatMessage().replace("%item%", pet.getName()).replace("%player%",
				target.getName()));
	}

	private void giveEgg(CommandSender sender, Player target, MysteryEgg egg, int amount) {
		plugin.getPetMan().giveMysteryegg(target, egg.getItemstack(), amount);
		target.sendMessage(Message.ITEM_RECEIVE.getChatMessage().replace("%item%", egg.getName()));
		sender.sendMessage(Message.ITEM_GIVE.getChatMessage().replace("%item%", egg.getName()).replace("%player%",
				target.getName()));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length == 0) {
				if (player.hasPermission("PvpPets.admin")) {
					sendHelp(player);

				} else {
					player.sendMessage(Message.NO_PERMISSION.getChatMessage());
				}
				return true;
			}

			String firstArg = args[0];

			switch (firstArg) {
			case "help":
				if (player.hasPermission("PvpPets.admin")) {
					sendHelp(player);
				} else {
					player.sendMessage(Message.NO_PERMISSION.getChatMessage());
				}
				break;
			case "reload":
				if (player.hasPermission("PvpPets.admin")) {
					this.plugin.reloadConfig();
					Message.reload();
					plugin.getPetMan().loadConfigSettings();
					sender.sendMessage(Message.RELOAD_CONFIG.getChatMessage());
				} else {
					player.sendMessage(Message.NO_PERMISSION.getChatMessage());
				}
				break;
			case "giveegg":
				if (player.hasPermission("PvpPets.admin")) {
					if (args.length > 1 && args.length <= 4) {
						Player target = player;
						MysteryEgg egg = null;
						int amount = 1;

						if (args.length == 2) {
							if (plugin.getPetMan().getMysteryEgg(args[1]) == null) {
								player.sendMessage(Lang.chat("&c(!) No egg with name &7&n" + args[1] + "&c."));
								return true;
							}
							egg = plugin.getPetMan().getMysteryEgg(args[1]);
						}

						if (args.length == 3) {
							if (plugin.getPetMan().getMysteryEgg(args[1]) == null) {
								if (Bukkit.getPlayer(args[1]) == null) {
									player.sendMessage(Message.OFFLINE_PLAYER.getChatMessage());
									return true;
								}

								if (plugin.getPetMan().getMysteryEgg(args[2]) == null) {
									player.sendMessage(Lang.chat("&c(!) No egg with name &7&n" + args[2] + "&c."));
									return true;
								}
								egg = plugin.getPetMan().getMysteryEgg(args[2]);

								target = Bukkit.getPlayer(args[1]);
							} else {
								amount = Integer.parseInt(args[2]);
							}
						}

						if (args.length == 4) {
							amount = Integer.parseInt(args[3]);
						}

						giveEgg(player, target, egg, amount);

					} else {
						player.sendMessage(Lang.chat("&c(!) /pets giveegg <player> <egg> [amount]"));
					}
				} else {
					player.sendMessage(Message.NO_PERMISSION.getChatMessage());
				}
				break;
			case "givepet":
				if (player.hasPermission("PvpPets.admin")) {
					if (args.length > 1 && args.length <= 4) {
						Player target = player;

						if (plugin.getPetMan().getPet(args[2]) == null) {
							player.sendMessage(Lang.chat("&c(!) No pet with name &7&n" + args[2] + "&c."));
							return true;
						}

						Pet pet = plugin.getPetMan().getPet(args[2]);

						int amount = 1;

						if (args.length == 3) {
							target = Bukkit.getPlayer(args[1]);

							if (target == null) {
								player.sendMessage(Message.OFFLINE_PLAYER.getChatMessage());
								return true;
							}
						}

						if (args.length == 4) {
							amount = Integer.parseInt(args[3]);
						}

						givePet(player, target, pet, amount);

					} else {
						player.sendMessage(Lang.chat("&c(!) /pets givepet [player] <pet> [amount]"));
					}

				} else {
					player.sendMessage(Message.NO_PERMISSION.getChatMessage());
				}
				break;
			}
		}

		return true;
	}
}
