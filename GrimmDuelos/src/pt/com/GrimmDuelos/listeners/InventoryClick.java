package pt.com.GrimmDuelos.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import pt.com.GrimmDuelos.methods.Duelo;
import pt.com.GrimmDuelos.methods.DueloType;

public class InventoryClick implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(e.isCancelled()) return;
		if(e.getWhoClicked().getType() != EntityType.PLAYER) return;
		if(Duelo.getAtual() == null || Duelo.getAtual().getType() == DueloType.LIVRE) return;
		Player p = (Player) e.getWhoClicked();
		if(Duelo.getAtual().getDesafiado() == p || Duelo.getAtual().getDesafiador() == p) {
			e.setCancelled(true);
			p.sendMessage("§cAguarde o duelo acabar para clicar no inventário.");
		}
	}
}
