package pt.com.GrimmDuelos.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.utils.CustomConfig;
import pt.com.GrimmDuelos.utils.zBUtils;

public class Cache {
	public Map<Player, Location> blockMoves;
	public Duelo DueloAtual;
	
	public String[] MensagemDigitAdversario;
	public String MensagemDigitCancelado;
	public String JogadorNaoEncontrado;
	public String EsvaziarInventario;
	public String NaoPodeCMD;
	
	public List<String> cmdsLiberados;
	
	public List<Player> digitandoAdversario;
	public Map<Player, Arena> setando;
	public Location saida;
	public Map<String, Arena> allArenas;
	public Map<String, DueloPlayer> allPlayers;
	
	public String StatsDisplay;
	public List<String> StatsLore;
	public String AdversarioDisplay;
	public List<String> AdversarioLore;
	public String TipoDeBatalhaDisplay;
	public List<String> TipoDeBatalhaLore;
	public String DesafiarDisplay;
	public List<String> DesafiarLore;
	
	public String LivreDisplay;
	public List<String> LivreLore;
	public String FullP4Display;
	public List<String> FullP4Lore;
	public String MaoDisplay;
	public List<String> MaoLore;
	
	public List<String> LorePreenchido;
	public List<String> LorePreencher;
	
	public String DecorrendoDisplay;
	public List<String> DecorrendoLore;
	
	public String naoDesafiado;
	public String naoSorteada;
	public String camarotetp;
	public String coletarItens;
	public String avisoItens;
	public List<String> Desafio;
	public List<String> Ignorado;
	public List<String> Recusado;
	public List<String> Aceite;
	public List<String> TempoAcabou;
	public List<String> DueloAcabou;
	
	private FileConfiguration cfg;
	
	public Cache() {
		cfg = Main.getInstance().getConfig();
		digitandoAdversario = new ArrayList<>();
		allArenas = new HashMap<>();
		setando = new HashMap<>();
		allPlayers = new HashMap<>();
		DueloAtual = null;
		blockMoves = new HashMap<>();
		cmdsLiberados = cfg.getStringList("comandos-liberados");
		loadLocations();
		loadMessages();
		loadMenus();
	}
	
	private void loadLocations() {
		if(cfg.getString("saida") != null) saida = Main.deserializeLocation(cfg.getString("saida"));
		for(String arena : cfg.getConfigurationSection("Arenas").getKeys(false)) {
			String c = "Arenas." + arena;
			Arena a = new Arena(arena, Main.deserializeLocation(cfg.getString(c + ".Camarote")),
					Main.deserializeLocation(cfg.getString(c + ".Jogador1")),
					Main.deserializeLocation(cfg.getString(c + ".Jogador2")));
			allArenas.put(arena, a);
		}
		zBUtils.ConsoleMsg("&b[§7GrimmDuelos§b] &aForam carregadas um total de §f" + allArenas.size() + "§a arenas.");
	}
	
	private void loadMessages() {
		List<String> msg1 = cfg.getStringList("mensagem-digit-adversario");
		String[] msg1s = new String[msg1.size()];
		for (int i = 0; i < msg1s.length; i++) msg1s[i] = msg1.get(i).replace("&", "§");
		MensagemDigitAdversario = msg1s;
		MensagemDigitCancelado = cfg.getString("mensagem-digit-cancelado").replace("&", "§");
		JogadorNaoEncontrado = cfg.getString("player-nao-encontrado").replace("&", "§");
		EsvaziarInventario = cfg.getString("esvaziar-inventario").replace("&", "§");
		
		naoSorteada = cfg.getString("nao-sorteada").replace("&", "§");
		camarotetp = cfg.getString("camarote-tp").replace("&", "§");
		coletarItens = cfg.getString("coletar-itens").replace("&", "§");
		
		Desafio = zBUtils.replaceList(cfg.getStringList("duelo-desafio"), "&", "§");
		Ignorado = zBUtils.replaceList(cfg.getStringList("duelo-ignorado"), "&", "§");
		Recusado = zBUtils.replaceList(cfg.getStringList("duelo-recusado"), "&", "§");
		Aceite = zBUtils.replaceList(cfg.getStringList("duelo-aceite"), "&", "§");
		naoDesafiado = cfg.getString("nao-desafiado").replace("&", "§");
		avisoItens = cfg.getString("aviso-itens").replace("&", "§");
		NaoPodeCMD = cfg.getString("nao-pode-cmd").replace("&", "§");
		TempoAcabou = zBUtils.replaceList(cfg.getStringList("duelo-acabou-tempo"), "&", "§");
		DueloAcabou = zBUtils.replaceList(cfg.getStringList("duelo-acabou"), "&", "§");
	}
	
	private void loadMenus() {
		CustomConfig menucfg = Main.getInstance().menusCfg;
		StatsDisplay = menucfg.getString("Menu-Principal.Stats.Display").replace("&", "§");
		StatsLore = zBUtils.replaceList(menucfg.getStringList("Menu-Principal.Stats.Lore"), "&", "§");
		AdversarioDisplay = menucfg.getString("Menu-Principal.Adversario.Display").replace("&", "§");
		AdversarioLore = zBUtils.replaceList(menucfg.getStringList("Menu-Principal.Adversario.Lore"), "&", "§");
		TipoDeBatalhaDisplay = menucfg.getString("Menu-Principal.TipoDeBatalha.Display").replace("&", "§");
		TipoDeBatalhaLore = zBUtils.replaceList(menucfg.getStringList("Menu-Principal.TipoDeBatalha.Lore"), "&", "§");
		DesafiarDisplay = menucfg.getString("Menu-Principal.Desafiar.Display").replace("&", "§");
		DesafiarLore = zBUtils.replaceList(menucfg.getStringList("Menu-Principal.Desafiar.Lore"), "&", "§");
		
		LorePreenchido = zBUtils.replaceList(menucfg.getStringList("Menu-Principal.Lore-Pronto"), "&", "§");
		LorePreencher = zBUtils.replaceList(menucfg.getStringList("Menu-Principal.Lore-Preencher"), "&", "§");

		DecorrendoDisplay = menucfg.getString("Menu-Principal.Decorrendo.Display").replace("&", "§");
		DecorrendoLore = zBUtils.replaceList(menucfg.getStringList("Menu-Principal.Decorrendo.Lore"), "&", "§");
		
		LivreDisplay = menucfg.getString("Escolher-Batalha.Livre.Display").replace("&", "§");
		LivreLore = zBUtils.replaceList(menucfg.getStringList("Escolher-Batalha.Livre.Lore"), "&", "§");
		FullP4Display = menucfg.getString("Escolher-Batalha.FullP4.Display").replace("&", "§");
		FullP4Lore = zBUtils.replaceList(menucfg.getStringList("Escolher-Batalha.FullP4.Lore"), "&", "§");
		MaoDisplay = menucfg.getString("Escolher-Batalha.Hand.Display").replace("&", "§");
		MaoLore = zBUtils.replaceList(menucfg.getStringList("Escolher-Batalha.Hand.Lore"), "&", "§");
	}
	
	public List<Arena> ArenasDisponiveis() {
		List<Arena> l = new ArrayList<>();
		for(Arena a : allArenas.values()) if(a.isDone()) l.add(a);
		return l;
	}
}
