package pt.com.GrimmDuelos.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import pt.com.GrimmDuelos.Main;

public class PlayerMove implements Listener {
	@EventHandler
	void onMove(PlayerMoveEvent e) {
		if(!Main.cache.blockMoves.containsKey(e.getPlayer())) return;
		e.getPlayer().teleport(Main.cache.blockMoves.get(e.getPlayer()));
	}
}
