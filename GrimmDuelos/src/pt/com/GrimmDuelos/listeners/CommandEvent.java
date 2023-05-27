package pt.com.GrimmDuelos.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.methods.Duelo;

public class CommandEvent implements Listener {
	@EventHandler
	void onCommand(PlayerCommandPreprocessEvent e) {
		if(!Duelo.PlayerIsOnDuelo(e.getPlayer()) || Main.cache.cmdsLiberados.contains(e.getMessage().split(" ")[0])) return;
		e.setCancelled(true);
		e.getPlayer().sendMessage(Main.cache.NaoPodeCMD);
	}
}
