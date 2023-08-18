package me.hgod.objects;

import lombok.Getter;
import me.hgod.Core;

@Getter
public class Mensagens {

	private String activated,
	desactivated,
	activatedPlayer,
	desactivatedPlayer,
	pluginReloaded,
	playerNotFound,
	noPermission;
	
	public Mensagens() {
		noPermission = replace("noPermission");
		playerNotFound = replace("playerNotFound");
		pluginReloaded = replace("pluginReloaded");
		desactivatedPlayer = replace("desactivatedPlayer");
		activatedPlayer = replace("activatedPlayer");
		desactivated = replace("desactivated");
		activated = replace("activated");
	}
	
	private String replace(String msg) {
		return Core.getInstance().getConfig().getString("Mensagens." + msg).replace("{tag}", Core.getInstance().getTag()).replace("&", "§");
	}
	
	public static Mensagens get() {
		return Core.getInstance().getMensagens();
	}
	
}
