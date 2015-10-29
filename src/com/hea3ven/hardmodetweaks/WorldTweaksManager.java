package com.hea3ven.hardmodetweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.hardmodetweaks.config.Config;

public class WorldTweaksManager {

	private static WorldTweaksManager instance = null;

	public static void onConfigChanged() {
		if (Config.enableWorldTweaks) {
			if (instance == null) {
				instance = new WorldTweaksManager();
				MinecraftForge.EVENT_BUS.register(instance);
			}
		} else {
			if (instance != null) {
				MinecraftForge.EVENT_BUS.unregister(instance);
				instance = null;
			}
		}
	}

	@SubscribeEvent
	public void onPlayerBreakSpeedEvent(PlayerEvent.BreakSpeed event) {
		if (event.newSpeed > Config.maxBreakSpeed)
			event.newSpeed = Config.maxBreakSpeed;
	}
}
