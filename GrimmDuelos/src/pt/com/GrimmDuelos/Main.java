package pt.com.GrimmDuelos;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import pt.com.GrimmDuelos.comands.DueloCmd;
import pt.com.GrimmDuelos.listeners.ChatEvent;
import pt.com.GrimmDuelos.listeners.CommandEvent;
import pt.com.GrimmDuelos.listeners.DeathEvent;
import pt.com.GrimmDuelos.listeners.InventoryClick;
import pt.com.GrimmDuelos.listeners.ItemPickup;
import pt.com.GrimmDuelos.listeners.JoinEvent;
import pt.com.GrimmDuelos.listeners.PlayerMove;
import pt.com.GrimmDuelos.listeners.QuitEvent;
import pt.com.GrimmDuelos.listeners.SetLocations;
import pt.com.GrimmDuelos.methods.Cache;
import pt.com.GrimmDuelos.methods.SQL;
import pt.com.GrimmDuelos.methods.menus.EscolherDueloType;
import pt.com.GrimmDuelos.methods.menus.MainMenu;
import pt.com.GrimmDuelos.utils.CustomConfig;
import pt.com.GrimmDuelos.utils.NumberFormatter;
import pt.com.GrimmDuelos.utils.zBUtils;

public class Main extends JavaPlugin {
	private static Main instance;
	public static NumberFormatter formatter;
	public static Cache cache;
	public CustomConfig menusCfg;
	public SQL sql;
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		instance = this;
		try {
			Class.forName("org.sqlite.JDBC").newInstance();
		} catch (Exception e) {}
		formatter = new NumberFormatter();
		loadListeners();
		loadCmds();
		menusCfg = new CustomConfig("menus.yml");
		menusCfg.saveDefaultConfig();
		saveDefaultConfig();
		cache = new Cache();
		sql = new SQL();
		sql.Iniciar();
		zBUtils.ConsoleMsg("&b[§7GrimmDuelos§b] &aO plugin foi iniciado.");
	}
	
	@Override
	public void onDisable() {
		sql.Encerrar();
		zBUtils.ConsoleMsg("&b[§7GrimmDuelos§b] &cO plugin foi encerrado.");
	}
	
	private void loadListeners() {
		Bukkit.getPluginManager().registerEvents(new MainMenu(), this);
		Bukkit.getPluginManager().registerEvents(new SetLocations(), this);
		Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
		Bukkit.getPluginManager().registerEvents(new EscolherDueloType(), this);
		Bukkit.getPluginManager().registerEvents(new DeathEvent(), this);
		Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);
		Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ItemPickup(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
		Bukkit.getPluginManager().registerEvents(new CommandEvent(), this);
	}
	
	private void loadCmds() {
 		getCommand("duelo").setExecutor(new DueloCmd());
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static String serializeLocation(Location loc) {
		if(loc == null) return "null";
		return loc.getWorld().getName() + "::" + loc.getBlockX() + "::" + loc.getBlockY()
				+ "::" + loc.getBlockZ() + "::" + loc.getYaw() + "::" + loc.getPitch();
	}
	
	public static Location deserializeLocation(String string) {
		if(string.equalsIgnoreCase("null")) return null;
		if(!string.contains("::")) return null;
		String[] sp = string.split("::");
		return new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]),
				Double.parseDouble(sp[3]), Float.parseFloat(sp[4]), Float.parseFloat(sp[5]));
	}
}
