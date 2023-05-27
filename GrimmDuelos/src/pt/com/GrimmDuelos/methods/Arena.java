package pt.com.GrimmDuelos.methods;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import pt.com.GrimmDuelos.Main;

public class Arena {
	
	private String name;
	private Location camarote;
	private Location p1;
	private Location p2;
	
	public Arena(String name, Location camarote, Location p1, Location p2) {
		this.name = name;
		this.setCamarote(camarote);
		this.setP1(p1);
		this.setP2(p2);
	}
	
	public Boolean isDone() {
		if(camarote == null) return false;
		if(p1 == null) return false;
		if(p2 == null) return false;
		return true;
	}
	
	public String getName() {
		return name;
	}

	public Location getCamarote() {
		return camarote;
	}

	public void setCamarote(Location camarote) {
		this.camarote = camarote;
	}

	public Location getP1() {
		return p1;
	}

	public void setP1(Location p1) {
		this.p1 = p1;
	}

	public Location getP2() {
		return p2;
	}

	public void setP2(Location p2) {
		this.p2 = p2;
	}
	
	public void deleteOnConfig() {
		FileConfiguration cfg = Main.getInstance().getConfig();
		String s = "Arenas." + name + ".";
		cfg.set(s + ".Camarote", null);
		cfg.set(s + ".Jogador1", null);
		cfg.set(s + ".Jogador2", null);
		cfg.set("Arenas." + name, null);
		Main.getInstance().saveConfig();
	}
	
	public void saveOnConfig() {
		FileConfiguration cfg = Main.getInstance().getConfig();
		String s = "Arenas." + name + ".";
		cfg.set(s + ".Camarote", Main.serializeLocation(camarote));
		cfg.set(s + ".Jogador1", Main.serializeLocation(p1));
		cfg.set(s + ".Jogador2", Main.serializeLocation(p2));
		Main.getInstance().saveConfig();
	}
	
	public static Arena getByName(String name) {
		return Main.cache.allArenas.get(name);
	}
}
