package com.furnesse.com.pvppets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.managers.MysteryEgg;

public class OpenMysteryEgg extends Event implements Cancellable {

	PvpPets plugin = PvpPets.instance;

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean cancelled;

	private Player player;
	private MysteryEgg egg;
	private ItemStack eggItem;

	public OpenMysteryEgg(Player player, MysteryEgg egg, ItemStack eggItem) {
		this.player = player;
		this.egg = egg;
		this.eggItem = eggItem;
		
		this.cancelled = false;
	}

	public static HandlerList getHandlersList() {
		return HANDLERS_LIST;
	}

	public Player getPlayer() {
		return player;
	}

	public MysteryEgg getEgg() {
		return egg;
	}

	public ItemStack getEggItem() {
		return eggItem;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		// TODO Auto-generated method stub
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return HANDLERS_LIST;
	}

}
