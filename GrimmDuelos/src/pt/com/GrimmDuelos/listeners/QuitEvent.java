package pt.com.GrimmDuelos.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import pt.com.GrimmDuelos.methods.Duelo;

public class QuitEvent implements Listener {
	@EventHandler
	void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(!Duelo.PlayerIsOnDuelo(p)) return;
		Duelo d = Duelo.getAtual();
		Player vencedor;
		if(d.getDesafiado() == p) vencedor = d.getDesafiador();
		else vencedor = d.getDesafiado();
		d.Terminar(vencedor, p);
	}
}
