package pt.com.GrimmDuelos.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import pt.com.GrimmDuelos.methods.Duelo;
import pt.com.GrimmDuelos.methods.DueloType;

public class ItemPickup implements Listener {
	@EventHandler
	void onPickup(PlayerPickupItemEvent e) {
		if(!Duelo.PlayerIsOnDuelo(e.getPlayer())) return;
		if(Duelo.getAtual().getType() == DueloType.LIVRE) return;
		e.setCancelled(true);
	}
	
	@EventHandler
	void onPickup2(PlayerPickupItemEvent e) {
		if(e.isCancelled()) return;
		if(Duelo.getAtual() == null || Duelo.getAtual().getType() == DueloType.LIVRE) return;
		Player p = e.getPlayer();
		if(Duelo.getAtual().getDesafiado() == p || Duelo.getAtual().getDesafiador() == p) {
			e.setCancelled(true);
		}
	}
}
