package com.furnesse.com.pvppets;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.furnesse.com.pvppets.abilities.RestockerPet;
import com.furnesse.com.pvppets.abilities.CombatDamagePets;
import com.furnesse.com.pvppets.abilities.IronSkinAbility;
import com.furnesse.com.pvppets.abilities.PotionEffectPets;
import com.furnesse.com.pvppets.commands.PetShopCMD;
import com.furnesse.com.pvppets.commands.PetsCMD;
import com.furnesse.com.pvppets.managers.Pets;
import com.furnesse.com.pvppets.mechanics.Events;
import com.furnesse.com.pvppets.mechanics.RandomPet;
import com.furnesse.com.pvppets.utils.Items;
import com.furnesse.com.pvppets.utils.Lang;
import com.furnesse.com.pvppets.wrapper.Wrapper;
import com.furnesse.com.pvppets.wrapper.Wrapper1_15;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class PvpPets extends JavaPlugin implements Listener {
	public static PvpPets instance;

	private Pets petMan = new Pets(this);
	private RandomPet randPet = new RandomPet(this);

	public Wrapper wrapper;
	public HeadDatabaseAPI headAPI = null;

	@EventHandler
	public void onDatabaseLoad(DatabaseLoadEvent e) {
		try {
			petMan.loadConfigSettings();
			initShop();
		} catch (NullPointerException nullpointer) {
			getLogger().info("could not find the head you were looking for");
		}
	}

	public void onEnable() {
		instance = this;
		wrapper = new Wrapper1_15();

		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		if (getServer().getPluginManager().getPlugin("HeadDatabase") != null) {
			headAPI = new HeadDatabaseAPI();
			this.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		} else {
			petMan.loadConfigSettings();
			initShop();
		}

		registerListener();
		registerCommands();

//		int ctr = 0;
//		long start = System.currentTimeMillis();
//		for (int x = 0; x < 1000000000; x++) {
//			ctr++;
//		}
//
//		System.out.println("delay: " + (System.currentTimeMillis() - start) + "ms");

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this,
				(Runnable) new PotionEffectPets(this), 60L, 60L);
	}

	public Inventory petshop = Bukkit.createInventory(null, 9,
			Lang.chat(getConfig().getString("PetShop.inventoryName")));

	private void initShop() {
		petshop.setItem(0, Items.SPACER.getItem());
		petshop.setItem(1, Items.EXCLUSIVE.getItem());
		petshop.setItem(2, Items.SPACER.getItem());
		petshop.setItem(3, Items.ULTIMATE.getItem());
		petshop.setItem(4, Items.SPACER.getItem());
		petshop.setItem(5, Items.LEGENDARY.getItem());
		petshop.setItem(6, Items.SPACER.getItem());
		petshop.setItem(7, Items.ANCIENT.getItem());
		petshop.setItem(8, Items.SPACER.getItem());
	}

	private void registerListener() {
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvents((Listener) new Events(), this);
		pm.registerEvents((Listener) new PetShopCMD(), this);
		pm.registerEvents((Listener) new CombatDamagePets(), this);
		pm.registerEvents((Listener) new IronSkinAbility(), this);
		pm.registerEvents((Listener) new RestockerPet(), this);
	}

	private void registerCommands() {
		getCommand("petshop").setExecutor((CommandExecutor) new PetShopCMD());
		getCommand("pets").setExecutor(new PetsCMD());
	}

	public Pets getPetMan() {
		return petMan;
	}

	public RandomPet getRandPet() {
		return randPet;
	}
}
