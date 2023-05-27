package pt.com.GrimmDuelos.methods.menus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.methods.Duelo;
import pt.com.GrimmDuelos.methods.DueloPlayer;
import pt.com.GrimmDuelos.methods.DueloType;
import pt.com.GrimmDuelos.utils.ItemBuilder;
import pt.com.GrimmDuelos.utils.NBTAPI;
import pt.com.GrimmDuelos.utils.zBUtils;

public class MainMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase("§7Duelos - Principal")) return;
		e.setCancelled(true);
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		Player p = (Player) e.getWhoClicked();
		if(e.getCurrentItem().getType() == Material.SKULL_ITEM) return;
		switch (e.getSlot()) {
		case 20:
			p.closeInventory();
			Main.cache.digitandoAdversario.add(p);
			p.sendMessage(Main.cache.MensagemDigitAdversario);
			break;
		case 22:
			EscolherDueloType.Open(p, getTarget(e.getInventory().getItem(20)));
			break;
		case 24:
			if (Duelo.isDecorrendo()) {
				p.sendMessage("§c§lERRO! §cJá está decorrendo uma batalha!");
				p.closeInventory();
				return;
			}
			if (DueloType.valueOf(new NBTAPI(e.getInventory().getItem(22)).getString("grimmduelos_duelotype")) != DueloType.LIVRE) {
				PlayerInventory inv = p.getInventory();
				if (inv.getHelmet() != null || inv.getChestplate() != null || inv.getLeggings() != null || inv.getBoots() != null) {
					p.sendMessage(Main.cache.EsvaziarInventario);
					return;
				}
				if (zBUtils.getEmptySlots(p) != 2304) {
					p.sendMessage(Main.cache.EsvaziarInventario);
					return;
				}
			}
			Main.cache.DueloAtual = new Duelo(p, getTarget(e.getInventory().getItem(20)), DueloType.valueOf(new NBTAPI(e.getInventory().getItem(22)).getString("grimmduelos_duelotype")), null);
			if(DueloType.valueOf(new NBTAPI(e.getInventory().getItem(22)).getString("grimmduelos_duelotype")) != DueloType.LIVRE) p.sendMessage(Main.cache.avisoItens);
			p.closeInventory();
			break;

		default:
			break;
		}
	}
	
	public static void Open(Player p, Player target, DueloType type) {
		if (Main.cache.saida == null) {
			p.sendMessage("§c§lERRO! §cA saída do duelo não está setada!");
			if(p.hasPermission("grimmduelo.admin")) p.sendMessage("§cUse /duelo setsaida, para setar a saída!");
			return;
		}
		if (Main.cache.ArenasDisponiveis().size() == 0) {
			OpenSemArenas(p);
			return;
		}
		if (Duelo.isDecorrendo()) {
			OpenDecorrendo(p);
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 5*9, "§7Duelos - Principal");
		
		List<Integer> slots = Arrays.asList(0,8,9,17,18,26,27,35,36,44);
		for(int i : slots) inv.setItem(i, new ItemBuilder(Material.POWERED_RAIL).setName("§7").toItemStack());
		
		slots = Arrays.asList(1,2,3,4,5,6,7,37,38,39,40,41,42,43);
		for(int i : slots) inv.setItem(i, new ItemBuilder(zBUtils.getItemByID("160:10")).setName("§7").toItemStack());
		
		DueloPlayer dp = DueloPlayer.get(p);
		
		ItemBuilder stats = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(p.getName())
				.setName(Main.cache.StatsDisplay).setLore(
						zBUtils.replaceList(zBUtils.replaceList(zBUtils.replaceList(Main.cache.StatsLore, "%duelos%",
								zBUtils.Formatar((double) dp.getParticipados())), "%vencidos%", zBUtils.Formatar((double) dp.getVencidos())),
								"%kdr%", zBUtils.Formatar2(dp.getKDR())));
		
		inv.setItem(13, stats.toItemStack());
		
		
		
		
		if (target == null) {
			inv.setItem(20, new ItemBuilder(Material.COMPASS).setName(Main.cache.AdversarioDisplay).setLore(Main.cache.AdversarioLore).toItemStack());
			inv.setItem(22, new ItemBuilder("3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025").setLore(Main.cache.LorePreencher).setName(Main.cache.TipoDeBatalhaDisplay).toItemStack());
			inv.setItem(24, new ItemBuilder("3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025").setLore(Main.cache.LorePreencher).setName(Main.cache.DesafiarDisplay).toItemStack());
		} else {
			ItemBuilder ib = new ItemBuilder("a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6").setLore(Main.cache.LorePreenchido).setName(Main.cache.AdversarioDisplay);
			inv.setItem(20, new NBTAPI(ib.toItemStack()).setString("grimmduelos_target", target.getName()).getItem());
			if (type == null) {
				inv.setItem(22, new ItemBuilder(Material.GOLD_AXE).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).setName(Main.cache.TipoDeBatalhaDisplay).setLore(Main.cache.TipoDeBatalhaLore).toItemStack());
				inv.setItem(24, new ItemBuilder("3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025").setLore(Main.cache.LorePreencher).setName(Main.cache.DesafiarDisplay).toItemStack());
			} else {
				ib = new ItemBuilder("a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6").setName(Main.cache.TipoDeBatalhaDisplay).setLore(Main.cache.LorePreenchido);
				inv.setItem(22, new NBTAPI(ib.toItemStack()).setString("grimmduelos_duelotype", type.toString()).getItem());
				inv.setItem(24, new ItemBuilder(Material.DIAMOND_SWORD).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).setName(Main.cache.DesafiarDisplay).setLore(zBUtils.replaceList(Main.cache.DesafiarLore, "%desafiado%", target.getName())).toItemStack());
			}
		}
		
		p.openInventory(inv);
	}
	
	@EventHandler
	void onClick2(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase("§7Duelos - Indisponível")) return;
		e.setCancelled(true);
	}
	
	private static void OpenSemArenas(Player p) {
		Inventory inv = Bukkit.createInventory(null, 5*9, "§7Duelos - Indisponível");
		
		
		List<Integer> slots = Arrays.asList(0,8,9,17,18,26,27,35,36,44);
		for(int i : slots) inv.setItem(i, new ItemBuilder(Material.POWERED_RAIL).setName("§7").toItemStack());
		
		slots = Arrays.asList(1,2,3,4,5,6,7,37,38,39,40,41,42,43);
		for(int i : slots) inv.setItem(i, new ItemBuilder(zBUtils.getItemByID("160:10")).setName("§7").toItemStack());
		
		ItemBuilder indisponivel = new ItemBuilder("3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025").setName("§cIndisponível")
				.setLore(Arrays.asList("§7Não existe nenhuma arena criada."));
		inv.setItem(22, indisponivel.toItemStack());
		
		p.openInventory(inv);
	}
	
	@EventHandler
	void onClick3(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase("§7Duelos - Decorrendo")) return;
		e.setCancelled(true);
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		Player p = (Player) e.getWhoClicked();
		if (e.getSlot() == 22) {
			if (Duelo.getAtual() == null || Duelo.getAtual().getArena() == null) {
				p.sendMessage(Main.cache.naoSorteada);
				p.closeInventory();
				return;
			}
			p.teleport(Duelo.getAtual().getArena().getCamarote());
			p.sendMessage(Main.cache.camarotetp);
		}
	}
	
	private static void OpenDecorrendo(Player p) {
		Inventory inv = Bukkit.createInventory(null, 5*9, "§7Duelos - Decorrendo");
		
		
		List<Integer> slots = Arrays.asList(0,8,9,17,18,26,27,35,36,44);
		for(int i : slots) inv.setItem(i, new ItemBuilder(Material.POWERED_RAIL).setName("§7").toItemStack());
		
		slots = Arrays.asList(1,2,3,4,5,6,7,37,38,39,40,41,42,43);
		for(int i : slots) inv.setItem(i, new ItemBuilder(zBUtils.getItemByID("160:10")).setName("§7").toItemStack());
		
		ItemBuilder camarote = new ItemBuilder(Material.EYE_OF_ENDER).setName(Main.cache.DecorrendoDisplay)
				.setLore(Main.cache.DecorrendoLore);
		inv.setItem(22, camarote.toItemStack());
		
		p.openInventory(inv);
	}
	
	private Player getTarget(ItemStack i) {
		NBTAPI nbt = NBTAPI.getNBT(i);
		if(!nbt.hasKey("grimmduelos_target")) return null;
		return Bukkit.getPlayer(nbt.getString("grimmduelos_target"));
	}
}
