package pt.com.GrimmDuelos.comands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.methods.Arena;
import pt.com.GrimmDuelos.methods.Duelo;
import pt.com.GrimmDuelos.methods.DueloType;
import pt.com.GrimmDuelos.methods.menus.MainMenu;
import pt.com.GrimmDuelos.utils.ItemBuilder;
import pt.com.GrimmDuelos.utils.NBTAPI;
import pt.com.GrimmDuelos.utils.zBUtils;

public class DueloCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] a) {
		
		if (!(s instanceof Player)) {
			s.sendMessage("§cA console não pode executar este comando!");
			return true;
		}
		
		Player p = (Player)s;
		if (a.length == 0) {
			MainMenu.Open(p, null, null);
			return true;
		}
		
		if (!p.hasPermission("grimmduelos.admin")) {
			switch (a[0]) {				
			case "aceitar":
				if (Duelo.getAtual() == null || Duelo.getAtual().getDesafiado() != p) {
					p.sendMessage(Main.cache.naoDesafiado);
					return true;
				}
				if (Duelo.getAtual().getType() == DueloType.LIVRE) {
					Duelo.getAtual().Iniciar();
					return true;
				}
				PlayerInventory inv = p.getInventory();
				if (inv.getHelmet() != null || inv.getChestplate() != null || inv.getLeggings() != null || inv.getBoots() != null || zBUtils.getEmptySlots(p) != 2304) {
					p.sendMessage(Main.cache.EsvaziarInventario);
					return true;
				}
				Duelo.getAtual().Iniciar();
				break;
			case "recusar":
				if (Duelo.getAtual() == null || Duelo.getAtual().getDesafiado() != p) {
					p.sendMessage(Main.cache.naoDesafiado);
					return true;
				}
				Duelo.getAtual().Recusar();
				break;
				
			case "help":
			case "ajuda":
			case "?":
				p.sendMessage(new String[] {
						"§6§lGRIMM DUELOS - COMANDOS",
						"§a - §f/duelo",
						"§a - §f/duelo aceitar",
						"§a - §f/duelo recusar",
					});
				break;
				
			default:
				p.sendMessage("§c§lERRO! §cArgumento §e" + a[0] + "§c não foi encontrado! (/duelo ajuda).");
				break;
			}
			return true;
		}
		
		switch (a[0]) {
		case "arenas":
			p.sendMessage("§eTodas as arenas disponiveis:");
			for(Arena arena : Main.cache.allArenas.values()) {
				String done = (arena.isDone()) ? "§a✔" : "§c✘";
				p.sendMessage(done + " " + arena.getName());
			}
			break;
		case "criar":
			if(a.length != 2) {
				p.sendMessage("§c§lERRO! §cUse /duelo criar <Nome>");
				return true;
			}
			String name = a[1];
			if (Main.cache.allArenas.containsKey(name)) {
				p.sendMessage("§c§lERRO! §cEssa arena já exite, você pode edita-la.");
				return true;
			}
			if (zBUtils.getEmptySlots(p) != 2304) {
				p.sendMessage("§c§lERRO! §cVocê precisa ter o inventário vazio!");
				return true;
			}
			setItens(p);
			Main.cache.allArenas.put(name, new Arena(name, null, null, null));
			Arena.getByName(name).saveOnConfig();
			Main.cache.setando.put(p, Arena.getByName(name));
			p.sendMessage("§aA arena §7" + name + "§a foi criada com sucesso!");
			p.sendMessage("§eUse os itens no inventário para setar localizações!");
			break;
			
		case "deletar":
		case "remover":
			if(a.length != 2) {
				p.sendMessage("§c§lERRO! §cUse /duelo deletar <Nome>");
				return true;
			}
			String name1 = a[1];
			if (!Main.cache.allArenas.containsKey(name1)) {
				p.sendMessage("§c§lERRO! §cEssa arena não existe, veja em /duelo arenas!");
				return true;
			}
			
			Arena arena = Arena.getByName(name1);
			arena.deleteOnConfig();
			Main.cache.allArenas.remove(name1);
			p.sendMessage("§aA arena §7" + name1 + "§a foi deletada com sucesso!");
			break;
			
		case "editar":
		case "edit":
			if(a.length != 2) {
				p.sendMessage("§c§lERRO! §cUse /duelo editar <Nome>");
				return true;
			}
			String name2 = a[1];
			if (!Main.cache.allArenas.containsKey(name2)) {
				p.sendMessage("§c§lERRO! §cEssa arena não existe, veja em /duelo arenas!");
				return true;
			}
			if (zBUtils.getEmptySlots(p) != 2304) {
				p.sendMessage("§c§lERRO! §cVocê precisa ter o inventário vazio!");
				return true;
			}
			setItens(p);
			Main.cache.setando.put(p, Arena.getByName(name2));
			p.sendMessage("§aVocê está editando a arena §7" + name2 + "§a!");
			p.sendMessage("§eUse os itens no inventário para setar localizações!");
			break;
			
		case "setsaida":
			Main.cache.saida = p.getLocation();
			Main.getInstance().getConfig().set("saida", Main.serializeLocation(p.getLocation()));
			Main.getInstance().saveConfig();
			p.sendMessage("§aLocalização de saída para duelos foi setada!");
			break;
			
		case "aceitar":
			if (Duelo.getAtual() == null || Duelo.getAtual().getDesafiado() != p) {
				p.sendMessage(Main.cache.naoDesafiado);
				return true;
			}
			if (Duelo.getAtual().getType() == DueloType.LIVRE) {
				Duelo.getAtual().Iniciar();
				return true;
			}
			PlayerInventory inv = p.getInventory();
			if (inv.getHelmet() != null || inv.getChestplate() != null || inv.getLeggings() != null || inv.getBoots() != null || zBUtils.getEmptySlots(p) != 2304) {
				p.sendMessage(Main.cache.EsvaziarInventario);
				return true;
			}
			Duelo.getAtual().Iniciar();
			break;
		case "recusar":
			if (Duelo.getAtual() == null || Duelo.getAtual().getDesafiado() != p) {
				p.sendMessage(Main.cache.naoDesafiado);
				return true;
			}
			Duelo.getAtual().Recusar();
			break;
			
		case "help":
		case "ajuda":
		case "?":
			p.sendMessage(new String[] {
					"§6§lGRIMM DUELOS - COMANDOS",
					"§a - §f/duelo",
					"§a - §f/duelo aceitar",
					"§a - §f/duelo recusar",
					"§a - §f/duelo arenas",
					"§a - §f/duelo criar <Nome>",
					"§a - §f/duelo deletar <Nome>",
					"§a - §f/duelo editar <Nome>",
					"§a - §f/duelo setsaida"
				});
			break;
			
		default:
			p.sendMessage("§c§lERRO! §cArgumento §e" + a[0] + "§c não foi encontrado! (/duelo ajuda).");
			break;
		}
		
		return true;
	}
	
	private void setItens(Player p) {
		NBTAPI nbt0 = NBTAPI.getNBT(new ItemBuilder("3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025").setName("§c§lSAIR").toItemStack());
		NBTAPI nbt1 = NBTAPI.getNBT(new ItemBuilder("37cc76e7af29f5f3fbfd6ece794160811eff96f753459fa61d7ad176a064e3c5").setName("§a§lCAMAROTE").toItemStack());
		NBTAPI nbt2 = NBTAPI.getNBT(new ItemBuilder("71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530").setName("§e§lJOGADOR 1").toItemStack());
		NBTAPI nbt3 = NBTAPI.getNBT(new ItemBuilder("4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847").setName("§e§lJOGADOR 2").toItemStack());
		nbt0.setString("grimmduelos_setloc", "sair");
		nbt1.setString("grimmduelos_setloc", "camarote");
		nbt2.setString("grimmduelos_setloc", "j1");
		nbt3.setString("grimmduelos_setloc", "j2");
		p.getInventory().setItem(7, nbt0.getItem()); 
		p.getInventory().setItem(1, nbt1.getItem());
		p.getInventory().setItem(3, nbt2.getItem());
		p.getInventory().setItem(5, nbt3.getItem());
		p.getInventory().setHeldItemSlot(4);
	}
}
