package pt.com.GrimmDuelos.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.methods.Arena;
import pt.com.GrimmDuelos.utils.NBTAPI;

public class SetLocations implements Listener {
	@EventHandler
	void onInteract(PlayerInteractEvent e) {
		if(!Main.cache.setando.containsKey(e.getPlayer())) return;
		if(e.getItem() == null || e.getItem().getType() == Material.AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) return;
		NBTAPI nbt = NBTAPI.getNBT(e.getItem());
		if(!nbt.hasKey("grimmduelos_setloc")) return;
		e.setCancelled(true);
		Player p = e.getPlayer();
		Arena a = Main.cache.setando.get(p);
		switch (nbt.getString("grimmduelos_setloc")) {
		case "j1":
			a.setP1(p.getLocation());
			a.saveOnConfig();
			p.sendMessage("§aLocalização do §7Jogador 1 §afoi setada com sucesso!");
			break;
			
		case "j2":
			a.setP2(p.getLocation());
			a.saveOnConfig();
			p.sendMessage("§aLocalização do §7Jogador 2 §afoi setada com sucesso!");
			break;
			
		case "camarote":
			a.setCamarote(p.getLocation());
			a.saveOnConfig();
			p.sendMessage("§aLocalização do §7Camarote §afoi setada com sucesso!");
			break;
			
		case "sair":
			p.getInventory().clear();
			Main.cache.setando.remove(p);
			p.sendMessage("§cVocê saiu da edição de localizações da arena.");
			break;

		default:
			break;
		}
	}
}
