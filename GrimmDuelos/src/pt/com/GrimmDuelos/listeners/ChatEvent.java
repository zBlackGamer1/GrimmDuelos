package pt.com.GrimmDuelos.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.methods.menus.MainMenu;

public class ChatEvent implements Listener {
	@EventHandler
	void onChat(AsyncPlayerChatEvent e) {
		if(!Main.cache.digitandoAdversario.contains(e.getPlayer())) return;
		e.setCancelled(true);
		String m = e.getMessage();
		Player p = e.getPlayer();
		Main.cache.digitandoAdversario.remove(e.getPlayer());
		if (m.equalsIgnoreCase("cancelar")) {
			p.sendMessage(Main.cache.MensagemDigitCancelado);
			return;
		}
		Player t = Bukkit.getPlayer(m);
		if (t == null) {
			p.sendMessage(Main.cache.JogadorNaoEncontrado);
			return;
		}
		MainMenu.Open(p, t, null);
	}
}
