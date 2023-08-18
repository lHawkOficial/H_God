package me.hgod.commads;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hgod.Core;
import me.hgod.objects.Mensagens;
import me.hgod.objects.PlayerGod;

public class CommandGod implements CommandExecutor {

	public CommandGod() {
		Core.getInstance().getCommand("god").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (!(s.hasPermission("H_God.Command"))) {
			s.sendMessage(Mensagens.get().getNoPermission());
			return false;
		}
		if (s instanceof Player) ((Player) s).playSound(((Player) s).getLocation(), Sound.NOTE_BASS, 0.5f, 10f);
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload") && s.hasPermission("H_God.reload")) {
				Core.getInstance().reloadPlugin();
				s.sendMessage(Mensagens.get().getPluginReloaded());
				return false;
			}
			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
				s.sendMessage(" ");
				s.sendMessage(Core.getInstance().getTag() + " §aComandos disponíveis:");
				if (s.hasPermission("H_God.Reload")) s.sendMessage("§e/Imortal [Reload] §7- Recarregar configuração do plugin.");
				s.sendMessage("§e/Imortal §7- Entrar/Sair do modo imortal.");
				s.sendMessage("§e/Imortal [Player] §7- Colocar/Tirar um jogador no modo imortal.");
				s.sendMessage(" ");
				return false;
			}
			Player target = Bukkit.getPlayerExact(args[0]);
			if (target != null) {
				PlayerGod pg = PlayerGod.check(target);
				if (pg.isImmortal()) {
					pg.setImmortal(false);
					target.sendMessage(Mensagens.get().getDesactivated());
					s.sendMessage(Mensagens.get().getDesactivatedPlayer().replace("{player}", target.getName()));
				}else {
					pg.setImmortal(true);
					target.sendMessage(Mensagens.get().getActivated());
					s.sendMessage(Mensagens.get().getActivatedPlayer().replace("{player}", target.getName()));
				}
				target.playSound(target.getLocation(), Sound.LEVEL_UP, 0.5f, 10);
				return false;
			}else {
				s.sendMessage(Mensagens.get().getPlayerNotFound());
				return false;
			}
		}
		if (!(s instanceof Player)) return false;
		Player p = (Player)s;
		PlayerGod pg = PlayerGod.check(p);
		if (pg.isImmortal()) {
			pg.setImmortal(false);
			s.sendMessage(Mensagens.get().getDesactivated());
		}else {
			pg.setImmortal(true);
			s.sendMessage(Mensagens.get().getActivated());
		}
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5f, 10);
		return false;
	}
	
}
