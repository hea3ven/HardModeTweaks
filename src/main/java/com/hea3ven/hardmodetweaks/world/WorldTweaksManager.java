package com.hea3ven.hardmodetweaks.world;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldTweaksManager {

	public static float maxBreakSpeed = 300;

	@SubscribeEvent
	public void onPlayerBreakSpeedEvent(PlayerEvent.BreakSpeed event) {
		if (event.getNewSpeed() > maxBreakSpeed)
			event.setNewSpeed(maxBreakSpeed);
	}
}
