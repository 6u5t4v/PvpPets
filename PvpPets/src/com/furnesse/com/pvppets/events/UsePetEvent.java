package com.furnesse.com.pvppets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.furnesse.com.pvppets.PvpPets;
import com.furnesse.com.pvppets.managers.Pet;

public class UsePetEvent extends Event implements Cancellable {
	PvpPets plugin = PvpPets.instance;

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean cancelled;

	private Player player;
	private Pet pet;
	private ItemStack petItem;

	public UsePetEvent(Player player, Pet pet, ItemStack petItem) {
		this.player = player;
		this.pet = pet;
		this.petItem = petItem;
		
		this.cancelled = false;
	}

	public static HandlerList getHandlersList() {
		return HANDLERS_LIST;
	}

	public Player getPlayer() {
		return player;
	}

	public Pet getPet() {
		return pet;
	}

	public ItemStack getPetItem() {
		return petItem;
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
