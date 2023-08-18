package me.hgod;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.hgod.commads.CommandGod;
import me.hgod.objects.Mensagens;
import me.hgod.objects.PlayerGod;
import me.hgod.objects.managers.Manager;

@Getter
public class Core extends JavaPlugin {

	@Getter
	private static Core instance;
	
	private String tag, version = "§dv" + getDescription().getVersion();
	private Manager manager;
	private Mensagens mensagens;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		reloadPlugin();
		new CommandGod();
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			public void join(PlayerJoinEvent e) {
				PlayerGod.check(e.getPlayer());
			}
			
			@EventHandler
			public void quit(PlayerQuitEvent e) {
				Player p = e.getPlayer();
				PlayerGod pg = PlayerGod.check(p);
				manager.getPlayers().remove(pg);
				p.removeMetadata("hgod", instance);
			}
			
			@EventHandler(priority = EventPriority.LOWEST)
			public void damage(EntityDamageEvent e) {
				if (!(e.getEntity() instanceof Player)) return;
				Player p = (Player)e.getEntity();
				PlayerGod pg = PlayerGod.check(p);
				if (!pg.isImmortal()) return;
				Bukkit.getScheduler().runTask(instance, ()->p.setHealth(p.getMaxHealth()));
				for(ItemStack item : p.getInventory().getArmorContents()) {
					if (item == null || item.getType() == Material.AIR) continue;
					if (!canFix(item) && item.getDurability() > 0) continue;
					item.setDurability((short)0);
					p.updateInventory();
				}
			}
			
			@EventHandler
			public void breakBlock(BlockBreakEvent e) {
				Player p = e.getPlayer();
				PlayerGod pg = PlayerGod.check(p);
				if (!pg.isImmortal()) return;
				ItemStack item = p.getItemInHand();
				if (item == null || item.getType() == Material.AIR) return;
				if (!canFix(item) && item.getDurability() > 0) return;
				Bukkit.getScheduler().runTask(instance, ()->{
					item.setDurability((short)0);
					p.updateInventory();
				});
			}
			
			@EventHandler(priority = EventPriority.LOWEST)
			public void interactEntity(PlayerInteractAtEntityEvent e) {
				Player p = e.getPlayer();
				PlayerGod pg = PlayerGod.check(p);
				if (!pg.isImmortal()) return;
				ItemStack item = p.getItemInHand();
				if (item == null || item.getType() == Material.AIR) return;
				if (!canFix(item) && item.getDurability() > 0) return;
				item.setDurability((short)0);
				p.updateInventory();
			}
			
			@EventHandler(priority = EventPriority.LOWEST)
			public void interact(PlayerInteractEvent e) {
				Player p = e.getPlayer();
				PlayerGod pg = PlayerGod.check(p);
				if (!pg.isImmortal()) return;
				ItemStack item = p.getItemInHand();
				if (item == null || item.getType() == Material.AIR) return;
				if (!canFix(item) && item.getDurability() > 0) return;
				item.setDurability((short)0);
				p.updateInventory();
			}
			
			@EventHandler(priority = EventPriority.LOWEST)
			public void entityDamageEntity(EntityDamageByEntityEvent e) {
				Entity entity = e.getDamager();
				if (entity instanceof Projectile && ((Projectile)entity).getShooter() instanceof Player) entity = (Entity) ((Projectile)entity).getShooter();
				if (!(entity instanceof Player)) return;
				Player p = (Player) entity;
				PlayerGod pg = PlayerGod.check(p);
				if (!pg.isImmortal()) return;
				ItemStack item = p.getItemInHand();
				if (item == null || item.getType() == Material.AIR) return;
				if (!canFix(item)) return;
				item.setDurability((short)0);
				p.updateInventory();
			}
			
			@EventHandler
			public void food(FoodLevelChangeEvent e) {
				Player p = (Player) e.getEntity();
				PlayerGod pg = PlayerGod.check(p);
				if (!pg.isImmortal()) return;
				e.setFoodLevel(20);
			}
			
		}, this);
		
		sendConsole(" ");
		sendConsole(tag + " &aH_God iniciado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.removeMetadata("hgod", instance);
		}
		sendConsole(" ");
		sendConsole(tag + " &cH_God desligado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	public void reloadPlugin() {
		reloadConfig();
		tag = getConfig().getString("Config.tag").replace("&", "§");
		manager = new Manager();
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.removeMetadata("hgod", instance);
			PlayerGod.check(p);
		}
		mensagens = new Mensagens();
	}
	
	private Boolean canFix(ItemStack item) {
		if (item == null || item.getType().isEdible() || item.getType() == Material.AIR || item.getType().isBlock() || item.getDurability() == 0 || item.getType().getMaxDurability() <= 0) return false;
		return true;
	}
	
	public void sendConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));
	}
}
