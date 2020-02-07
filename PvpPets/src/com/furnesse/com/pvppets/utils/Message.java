package com.furnesse.com.pvppets.utils;

import com.furnesse.com.pvppets.PvpPets;

public enum Message
{
	PREFIX("Messages.PREFIX"), CANNOT_PLACE("Messages.CANNOT_PLACE"), ILLEGAL_INTERACTION("Messages.ILLEGAL_INTERACTION"),
	FULL_INVENTORY("Messages.FULL_INVENTORY"), RECEIVE_RANDOM_PET("Messages.RECEIVE_RANDOM_PET"),
	NO_PERMISSION("Messages.NO_PERMISSION"), OFFLINE_PLAYER("Messages.OFFLINE_PLAYER"),
	ITEM_RECEIVE("Messages.ITEM_RECEIVE"), ITEM_GIVE("Messages.ITEM_GIVE"), RELOAD_CONFIG("Messages.RELOAD_CONFIG"),
	UNSTACK_EGGS("Messages.UNSTACK_EGGS"), PET_LEVEL_UP("Messages.PET_LEVEL_UP"),
	
	PURCHASE("Messages.PURCHASE"), 
	CANNOT_AFFORD("Messages.CANNOT_AFFORD"),
	CANNOT_BE_PURCHASED("Messages.CANNOT_BE_PURCHASED"), 
	INVALID_EGG("Messages.INVALID_EGG"),
	INVALID_PET("Messages.INVALID_PET"),

	ON_COOLDOWN("Messages.ON_COOLDOWN");

	private String path;
	private String message;

	Message(String path) {
		this.path = path;
		this.message = Lang.chat(PvpPets.instance.getConfig().getString(path));
	}

	public String getChatMessage() {
		return PREFIX.getMessage() + this.message;
	}

	public String getPath() {
		return path;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public static void reload() {
		for (Message m : values()) {
			m.setMessage(Lang.chat(PvpPets.instance.getConfig().getString(m.getPath())));
		}
	}
}
