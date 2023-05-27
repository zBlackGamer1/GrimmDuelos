package pt.com.GrimmDuelos.methods.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.methods.DueloType;
import pt.com.GrimmDuelos.utils.ItemBuilder;
import pt.com.GrimmDuelos.utils.NBTAPI;

public class EscolherDueloType implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase("§7Duelos - Selecionar")) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		switch (e.getSlot()) {
		case 11:
			MainMenu.Open(p, getTarget(e.getCurrentItem()), DueloType.LIVRE);
			break;
		case 13:
			MainMenu.Open(p, getTarget(e.getCurrentItem()), DueloType.FULL);
			break;
		case 15:
			MainMenu.Open(p, getTarget(e.getCurrentItem()), DueloType.HAND);
			break;

		default:
			break;
		}
	}
	
	public static void Open(Player p, Player target) {
		Inventory inv = Bukkit.createInventory(null, 3*9, "§7Duelos - Selecionar");
		
		ItemBuilder i1 = new ItemBuilder(Material.STICK).setName(Main.cache.LivreDisplay).setLore(Main.cache.LivreLore);
		ItemBuilder i2 = new ItemBuilder(Material.DIAMOND_CHESTPLATE).setName(Main.cache.FullP4Display).setLore(Main.cache.FullP4Lore);
		ItemBuilder i3 = new ItemBuilder(Material.BROWN_MUSHROOM).setName(Main.cache.MaoDisplay).setLore(Main.cache.MaoLore);
		inv.setItem(11, new NBTAPI(i1.toItemStack()).setString("grimmduelos_target", target.getName()).getItem());
		inv.setItem(13, new NBTAPI(i2.toItemStack()).setString("grimmduelos_target", target.getName()).getItem());
		inv.setItem(15, new NBTAPI(i3.toItemStack()).setString("grimmduelos_target", target.getName()).getItem());
		
		p.openInventory(inv);
	}
	
	private Player getTarget(ItemStack i) {
		NBTAPI nbt = NBTAPI.getNBT(i);
		if(!nbt.hasKey("grimmduelos_target")) return null;
		return Bukkit.getPlayer(nbt.getString("grimmduelos_target"));
	}
}
