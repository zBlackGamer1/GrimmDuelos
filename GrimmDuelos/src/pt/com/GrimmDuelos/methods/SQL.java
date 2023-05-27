package pt.com.GrimmDuelos.methods;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.utils.zBUtils;

public class SQL {
	public Connection con = null;
	private String url;
	private String user;
	private String password;

	public void Iniciar() {
		openSQLite();
		String adress = Main.getInstance().getConfig().getString("MySQL.address");
		String database = Main.getInstance().getConfig().getString("MySQL.database");
		url = "jdbc:mysql://" + adress + "/" + database;
		user = Main.getInstance().getConfig().getString("MySQL.username");
		password = Main.getInstance().getConfig().getString("MySQL.password");
		if (openMySQL()) {
			zBUtils.ConsoleMsg("§6[GrimmDuelos] §aLigação com MySQL criada com sucesso.");
			loadDados();
		} else {
			if (openSQLite()) loadDados();
			else return;
		}
		AutoSaveTimer();
	}

	public void Encerrar() {
		close();
		saveDados();
	}

	private Boolean openMySQL() {
		if (Main.getInstance().getConfig().getBoolean("MySQL.ativo")) {
			try {
				con = DriverManager.getConnection(url, user, password);
				createtable();
				return true;
			} catch (SQLException e) {}
		}
		return false;
	}

	private Boolean openSQLite() {
		File file = new File(Main.getInstance().getDataFolder(), "database.db");
		String url = "jdbc:sqlite:" + file;
		try {
			con = DriverManager.getConnection(url);
			createtable();
			return true;
		} catch (SQLException e) {
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			zBUtils.ConsoleMsg("§6[GrimmDuelos] §cNão foi possível conectar com o banco de dados.");
		}
		return false;
	}

	private void close() {
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {}
		}
	}

	private void createtable() {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `grimmduelos` (`jogador` TEXT, `stats` TEXT)");
			stm.execute();
			stm.close();
		} catch (SQLException e) {
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			zBUtils.ConsoleMsg("§6[GrimmDuelos] §cOcorreu um erro ao criar tabela no banco de dados.");
		}
	}

	private void loadDados() {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `grimmduelos`");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String nick = rs.getString("jogador");
				String[] stats = rs.getString("stats").split("::");
				int vencidos = Integer.parseInt(stats[0]);
				int participados = Integer.parseInt(stats[1]);

				DueloPlayer.get(nick).setParticipados(participados);
				DueloPlayer.get(nick).setVencidos(vencidos);
			}
		} catch (SQLException e) {}
		close();
	}

	public void saveDados() {
		if (!openMySQL()) {
			openSQLite();
		}
		for (String nick : Main.cache.allPlayers.keySet()) {
			if (!BPTcontains(nick)) {
				criarPlayer(nick);
			} else {
				PreparedStatement stm = null;
				try {
					DueloPlayer dp = DueloPlayer.get(nick);
					stm = con.prepareStatement("UPDATE `grimmduelos` SET `stats` = ? WHERE `jogador` = ?");
					stm.setString(2, nick.toLowerCase());
					stm.setString(1, serializeStats(dp));
					stm.executeUpdate();
				} catch (SQLException e) {
				}
			}
		}
		close();
	}

	private Boolean BPTcontains(String PlayerName) {
		PreparedStatement stm = null;
		Boolean b = false;
		try {
			stm = con.prepareStatement("SELECT * FROM `grimmduelos` WHERE `jogador` = ?");
			stm.setString(1, PlayerName.toLowerCase());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				b = true;
			}
		} catch (SQLException e) {
		}
		return b;
	}

	private void criarPlayer(String PlayerName) {
		PreparedStatement stm = null;
		try {
			DueloPlayer dp = DueloPlayer.get(PlayerName);
			stm = con.prepareStatement("INSERT INTO `grimmduelos` (`jogador`, `stats`) VALUES (?,?)");
			stm.setString(1, dp.getName().toLowerCase());
			stm.setString(2, serializeStats(dp));
			stm.executeUpdate();
		} catch (SQLException e) {}
	}

	private void AutoSaveTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				AutoSave();
			}
		}.runTaskTimer(Main.getInstance(), 1200 * 30L, 1200 * 30L);
	}

	private void AutoSave() {
		saveDados();
		zBUtils.ConsoleMsg("§6[GrimmDuelos] §aAuto-Save completo.");
	}

	private String serializeStats(DueloPlayer dp) {
		return dp.getVencidos() + "::" + dp.getParticipados();
	}
}
