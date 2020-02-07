package com.furnesse.com.pvppets.abilities;

public abstract class Ability {

	public abstract void activateAbility();
	
	private boolean activeInInv;
	private boolean useCooldown;

	public Ability(boolean useCooldown, boolean activeInInv) {
		this.useCooldown = useCooldown;
		this.activeInInv = activeInInv;
	}

	public boolean isActiveInInv() {
		return activeInInv;
	}

	public boolean isUseCooldown() {
		return useCooldown;
	}
}
