package pt.com.GrimmDuelos.methods;

import org.bukkit.entity.Player;

import pt.com.GrimmDuelos.Main;

public class DueloPlayer {
	private String name;
	private int Participados;
	private int Vencidos;
	
	public DueloPlayer(String name, int Participados, int Vencidos) {
		this.name = name.toLowerCase();
		this.Participados = Participados;
		this.Vencidos = Vencidos;
	}

	public String getName() {
		return name;
	}

	public Integer getParticipados() {
		return Participados;
	}

	public Integer getVencidos() {
		return Vencidos;
	}
	
	public void setParticipados(int i) {
		this.Participados = i;
	}

	public void setVencidos(int i) {
		this.Vencidos = i;
	}
	
	public Integer getDerrotas() {
		return Participados - Vencidos;
	}

	public Double getKDR() {
		if(Participados == 0) return 0.0;
		if(getDerrotas() == 0) return Double.parseDouble(Vencidos + "");
		return getVencidos().doubleValue() / getDerrotas().doubleValue();
	}
	
	public static DueloPlayer get(String name) {
		if(Main.cache.allPlayers.containsKey(name.toLowerCase())) return Main.cache.allPlayers.get(name.toLowerCase());
		DueloPlayer dp = new DueloPlayer(name, 0, 0);
		Main.cache.allPlayers.put(name.toLowerCase(), dp);
		return dp;
	}
	
	public static DueloPlayer get(Player p) {
		String name = p.getName();
		if(Main.cache.allPlayers.containsKey(name.toLowerCase())) return Main.cache.allPlayers.get(name.toLowerCase());
		DueloPlayer dp = new DueloPlayer(name, 0, 0);
		Main.cache.allPlayers.put(name.toLowerCase(), dp);
		return dp;
	}
}
