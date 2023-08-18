package me.hgod.objects.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import me.hgod.Core;
import me.hgod.objects.PlayerGod;

@Getter
public class Manager {

	private List<PlayerGod> players = new ArrayList<>();
	
	public PlayerGod getPlayer(String name) {
		Iterator<PlayerGod> it = players.iterator();
		while(it.hasNext()) {
			PlayerGod pg = it.next();
			if (pg.getName().equalsIgnoreCase(name)) return pg;
		}
		return null;
	}
	
	public static Manager get() {
		return Core.getInstance().getManager();
	}
	
}
