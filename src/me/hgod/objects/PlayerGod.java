package me.hgod.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import lombok.Setter;
import me.hgod.Core;
import me.hgod.objects.managers.Manager;

@Getter
public class PlayerGod {

	private String name;
	@Setter
	private boolean immortal = false;
	
	public PlayerGod(String name) {
		this.name = name;
		Manager.get().getPlayers().add(this);
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(name);
	}
	
	public static PlayerGod check(Player p) {
		if (p.hasMetadata("hgod")) return (PlayerGod) p.getMetadata("hgod").get(0).value();
		PlayerGod pg = Manager.get().getPlayer(p.getName());
		if (pg == null) pg = new PlayerGod(p.getName());
		p.setMetadata("hgod", new FixedMetadataValue(Core.getInstance(), pg));
		return pg;
	}
	
}
