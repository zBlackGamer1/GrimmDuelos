package pt.com.GrimmDuelos.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import pt.com.GrimmDuelos.methods.Duelo;
import pt.com.GrimmDuelos.methods.DueloType;

public class DeathEvent implements Listener {
	@EventHandler
	void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(!Duelo.PlayerIsOnDuelo(p)) return;
		Duelo d = Duelo.getAtual();
		if(d.getType() != DueloType.LIVRE) e.getDrops().clear();
		Player vencedor;
		if(d.getDesafiado() == p) vencedor = d.getDesafiador();
		else vencedor = d.getDesafiado();
		d.Terminar(vencedor, p);
	}
}
