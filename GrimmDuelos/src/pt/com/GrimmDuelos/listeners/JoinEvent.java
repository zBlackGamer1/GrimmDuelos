package pt.com.GrimmDuelos.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmDuelos.utils.NBTAPI;

public class JoinEvent implements Listener {
	@EventHandler
	void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		for(ItemStack i : p.getInventory().getContents()) {
			if(i == null || i.getType() == Material.AIR) continue;
			if(NBTAPI.getNBT(i).hasKey("grimmduelos_item")){
				p.getInventory().clear();
				p.setItemOnCursor(null);
				p.getInventory().setHelmet(null);
				p.getInventory().setChestplate(null);
				p.getInventory().setLeggings(null);
				p.getInventory().setBoots(null);
				return;
			}
		}
		for(ItemStack i : p.getInventory().getArmorContents()) {
			if(i == null || i.getType() == Material.AIR) continue;
			if(NBTAPI.getNBT(i).hasKey("grimmduelos_item")){
				p.getInventory().clear();
				p.setItemOnCursor(null);
				p.getInventory().setHelmet(null);
				p.getInventory().setChestplate(null);
				p.getInventory().setLeggings(null);
				p.getInventory().setBoots(null);
				break;
			}
		}
	}
}
