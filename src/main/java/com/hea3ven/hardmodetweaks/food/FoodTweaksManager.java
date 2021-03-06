package com.hea3ven.hardmodetweaks.food;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class FoodTweaksManager {
	public static int healMinFoodLevel = 4;
	public static byte healTimeout = 20;
	public static float healAmount = 0.2f;
	public static float healExhaustion = 3.0f;

	private Map<EntityPlayer, ExtendedEntityPropertiesHealTimeout> timeouts = new HashMap<>();

	@SubscribeEvent
	public void onPlayerTickEvent(PlayerTickEvent event) {
		if (event.side == Side.SERVER) {
			if (event.player.getHealth() < event.player.getMaxHealth()) {
				if (event.player.getFoodStats().getFoodLevel() > healMinFoodLevel) {
					if (isTimeoutDone(event.player)) {
						event.player.heal(healAmount);
						event.player.getFoodStats().addExhaustion(healExhaustion);
					}
				}
			}
		}
	}

	private boolean isTimeoutDone(EntityPlayer player) {
		ExtendedEntityPropertiesHealTimeout timeout = timeouts.get(player);
		if (timeout == null) {
			timeouts.put(player, new ExtendedEntityPropertiesHealTimeout());
			return false;
		} else {
			timeout.tick();
			return timeout.isDone();
		}
	}
}
