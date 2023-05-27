package pt.com.GrimmDuelos.methods;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import pt.com.GrimmDuelos.Main;
import pt.com.GrimmDuelos.utils.ItemBuilder;
import pt.com.GrimmDuelos.utils.NBTAPI;
import pt.com.GrimmDuelos.utils.zBUtils;

public class Duelo {
	private Player desafiador;
	private Player desafiado;
	private DueloType type;
	private Arena arena;
	private boolean ignorado;
	private boolean acabado;
	
	public Duelo(Player desafiador, Player desafiado, DueloType type, Arena arena) {
		this.desafiador = desafiador;
		this.desafiado = desafiado;
		this.type = type;
		this.arena = arena;
		ignorado = true;
		acabado = false;
		for(String s : Main.cache.Desafio) Bukkit.broadcastMessage(s.replace("%tipo%", type.display).replace("%desafiador%", desafiador.getName()).replace("%desafiado%", desafiado.getName()));
		startCount();
	}
	
	private void startCount() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(ignorado) {
					for(String s : Main.cache.Ignorado) Bukkit.broadcastMessage(s.replace("%desafiador%", desafiador.getName()).replace("%desafiado%", desafiado.getName()));
					Deletar();
				}
				cancel();
			}
		}.runTaskTimer(Main.getInstance(), 20L * 60, 0L);
	}
	
	public void Deletar() {
		Main.cache.DueloAtual = null;
	}
	
	public Player getDesafiado() {
		return desafiado;
	}

	public Player getDesafiador() {
		return desafiador;
	}

	public DueloType getType() {
		return type;
	}

	public Arena getArena() {
		return arena;
	}
	
	public void Iniciar() {
		ignorado = false;
		for(String s : Main.cache.Aceite) Bukkit.broadcastMessage(s.replace("%desafiador%", desafiador.getName()).replace("%desafiado%", desafiado.getName()));
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				i++;
				switch (i) {
				case 1:
					zBUtils.sendTitle(desafiado, "§6§lDUELO", "§fArena: §e§kasdkasdk");
					zBUtils.sendTitle(desafiador, "§6§lDUELO", "§fArena: §e§kasdkasdk");
					break;
				case 2:
					Arena escolhida = Main.cache.ArenasDisponiveis().get(new Random().nextInt(Main.cache.ArenasDisponiveis().size()));
					arena = escolhida;
					zBUtils.sendTitle(desafiado, "§6§lDUELO", "§fArena: §e" + escolhida.getName());
					zBUtils.sendTitle(desafiador, "§6§lDUELO", "§fArena: §e" + escolhida.getName());
					break;
					
				default:
					if (type != DueloType.LIVRE) {
						desafiador.getInventory().clear();
						desafiado.getInventory().clear();
						desafiador.getInventory().setHelmet(null);
						desafiador.getInventory().setChestplate(null);
						desafiador.getInventory().setLeggings(null);
						desafiador.getInventory().setBoots(null);
						desafiado.getInventory().setHelmet(null);
						desafiado.getInventory().setChestplate(null);
						desafiado.getInventory().setLeggings(null);
						desafiado.getInventory().setBoots(null);
					}
					if (type == DueloType.FULL) {
						Map<Enchantment, Integer> enchants = new HashMap<>();
						enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
						enchants.put(Enchantment.DURABILITY, 3);
						ItemStack capa = new NBTAPI(new ItemBuilder(Material.DIAMOND_HELMET).addEnchantments(enchants)).setInt("grimmduelos_item", 1).getItem();
						ItemStack peito = new NBTAPI(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchantments(enchants)).setInt("grimmduelos_item", 1).getItem();
						ItemStack calca = new NBTAPI(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchantments(enchants)).setInt("grimmduelos_item", 1).getItem();
						ItemStack bota = new NBTAPI(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchantments(enchants)).setInt("grimmduelos_item", 1).getItem();
						enchants.clear();
						enchants.put(Enchantment.DAMAGE_ALL, 3);
						enchants.put(Enchantment.DURABILITY, 3);
						ItemStack espada = new NBTAPI(new ItemBuilder(Material.DIAMOND_SWORD).addEnchantments(enchants)).setInt("grimmduelos_item", 1).getItem();
						ItemStack machado = new NBTAPI(new ItemBuilder(Material.DIAMOND_AXE).addEnchantments(enchants)).setInt("grimmduelos_item", 1).getItem();
						ItemStack potion = new ItemStack(Material.POTION);
			            PotionMeta potionMeta = (PotionMeta)potion.getItemMeta();
			            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 120, 0), true);
			            potion.setItemMeta(potionMeta);
			            potion = new ItemBuilder(potion).setName("§ePoção de Força").addLoreLine("§7Força (2:00)").toItemStack();
						
						
						desafiado.getInventory().setArmorContents(new ItemStack[] {bota,calca,peito,capa});
						desafiador.getInventory().setArmorContents(new ItemStack[] {bota,calca,peito,capa});
						desafiado.getInventory().setItem(0, espada);
						desafiador.getInventory().setItem(0, espada);
						desafiado.getInventory().setItem(1, machado);
						desafiador.getInventory().setItem(1, machado);
						desafiado.getInventory().addItem(new ItemBuilder(zBUtils.getItemByID("322:1")).setAmount(16).toItemStack());
				        desafiador.getInventory().addItem(new ItemBuilder(zBUtils.getItemByID("322:1")).setAmount(16).toItemStack());
						desafiado.getInventory().addItem(potion);
				        desafiador.getInventory().addItem(potion);
						desafiado.getInventory().addItem(capa, peito, calca, bota);
						desafiador.getInventory().addItem(capa, peito, calca, bota);
					}
					desafiador.teleport(arena.getP1());
					desafiado.teleport(arena.getP2());
					DueloPlayer.get(desafiador).setParticipados(DueloPlayer.get(desafiador).getParticipados() + 1);
					DueloPlayer.get(desafiado).setParticipados(DueloPlayer.get(desafiado).getParticipados() + 1);
					StartCountDuelo();
					blockMoves();
					cancel();
					break;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 20L * 2);
	}
	
	private void blockMoves() {
		Main.cache.blockMoves.put(desafiado, arena.getP2());
		Main.cache.blockMoves.put(desafiador, arena.getP1());
		desafiado.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 10));
		desafiador.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 10));
		new BukkitRunnable() {
			int i = 11;
			@Override
			public void run() {
				i--;
				if (i == 0) {
					Main.cache.blockMoves.remove(desafiado);
					Main.cache.blockMoves.remove(desafiador);
					zBUtils.sendTitle(desafiado, "§6§lDUELO", "§fO duelo começou, vamos!");
					zBUtils.sendTitle(desafiador, "§6§lDUELO", "§fO duelo começou, vamos!");
					zBUtils.sendSound(desafiado, Sound.LEVEL_UP);
					zBUtils.sendSound(desafiador, Sound.LEVEL_UP);
					cancel();
				} else {
					zBUtils.sendTitle(desafiado, "§6§lDUELO", "§fO vai começar em §6" + i);
					zBUtils.sendTitle(desafiador, "§6§lDUELO", "§fO vai começar em §6" + i);
					zBUtils.sendSound(desafiado, Sound.CLICK);
					zBUtils.sendSound(desafiador, Sound.CLICK);
				}
			}
		}.runTaskTimer(Main.getInstance(), 20L, 20L);
	}
	
	private void StartCountDuelo() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!acabado) {
					if (type != DueloType.LIVRE) {
						for(ItemStack i : desafiado.getInventory().getContents()) {
							if(i == null || i.getType() == Material.AIR) continue;
							if(NBTAPI.getNBT(i).hasKey("grimmduelos_item")){
								desafiado.getInventory().clear();
								desafiado.setItemOnCursor(null);
								desafiado.getInventory().setHelmet(null);
								desafiado.getInventory().setChestplate(null);
								desafiado.getInventory().setLeggings(null);
								desafiado.getInventory().setBoots(null);
								return;
							}
						}
						for(ItemStack i : desafiado.getInventory().getArmorContents()) {
							if(i == null || i.getType() == Material.AIR) continue;
							if(NBTAPI.getNBT(i).hasKey("grimmduelos_item")){
								desafiado.getInventory().clear();
								desafiado.setItemOnCursor(null);
								desafiado.getInventory().setHelmet(null);
								desafiado.getInventory().setChestplate(null);
								desafiado.getInventory().setLeggings(null);
								desafiado.getInventory().setBoots(null);
								break;
							}
						}
						for(ItemStack i : desafiador.getInventory().getContents()) {
							if(i == null || i.getType() == Material.AIR) continue;
							if(NBTAPI.getNBT(i).hasKey("grimmduelos_item")){
								desafiador.getInventory().clear();
								desafiador.setItemOnCursor(null);
								desafiador.getInventory().setHelmet(null);
								desafiador.getInventory().setChestplate(null);
								desafiador.getInventory().setLeggings(null);
								desafiador.getInventory().setBoots(null);
								return;
							}
						}
						for(ItemStack i : desafiador.getInventory().getArmorContents()) {
							if(i == null || i.getType() == Material.AIR) continue;
							if(NBTAPI.getNBT(i).hasKey("grimmduelos_item")){
								desafiador.getInventory().clear();
								desafiador.setItemOnCursor(null);
								desafiador.getInventory().setHelmet(null);
								desafiador.getInventory().setChestplate(null);
								desafiador.getInventory().setLeggings(null);
								desafiador.getInventory().setBoots(null);
								break;
							}
						}
					}
					desafiado.teleport(Main.cache.saida);
					desafiador.teleport(Main.cache.saida);
					for(String s : Main.cache.TempoAcabou) Bukkit.broadcastMessage(s.replace("%desafiador%", desafiador.getName()).replace("%desafiado%", desafiado.getName()));
				}
				cancel();
			}
		}.runTaskTimer(Main.getInstance(), 1200L * 10, 0L);
	}
	
	public void Recusar() {
		ignorado = false;
		for(String s : Main.cache.Recusado) Bukkit.broadcastMessage(s.replace("%desafiador%", desafiador.getName()).replace("%desafiado%", desafiado.getName()));
		Deletar();
	}
	
	public void Terminar(Player vencedor, Player perdedor) {
		acabado = true;
		DueloPlayer.get(vencedor).setVencidos(DueloPlayer.get(vencedor).getVencidos() + 1);
		for(String s : Main.cache.DueloAcabou) Bukkit.broadcastMessage(s.replace("%vencedor%", vencedor.getName()).replace("%perdedor%", perdedor.getName()));
		if (type == DueloType.LIVRE) {
			vencedor.sendMessage(Main.cache.coletarItens);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					vencedor.teleport(Main.cache.saida);
					cancel();
				}
			}.runTaskTimer(Main.getInstance(), 20L * 15, 0L);
		} else {
			vencedor.teleport(Main.cache.saida);
			vencedor.getInventory().clear();
			vencedor.getInventory().setHelmet(null);
			vencedor.getInventory().setChestplate(null);
			vencedor.getInventory().setLeggings(null);
			vencedor.getInventory().setBoots(null);
		}
		Deletar();
	}
	
	public static Boolean isDecorrendo() {
		if(Main.cache.DueloAtual == null) return false;
		return true;
	}
	
	public static Duelo getAtual() {
		if(Main.cache.DueloAtual == null) return null;
		else return Main.cache.DueloAtual;
	}
	
	public static Boolean PlayerIsOnDuelo(Player p) {
		if (Main.cache.DueloAtual == null || Main.cache.DueloAtual.ignorado) return false;
		if (Main.cache.DueloAtual.desafiado == p || Main.cache.DueloAtual.desafiador == p) return true;
		return false;
	}
}
