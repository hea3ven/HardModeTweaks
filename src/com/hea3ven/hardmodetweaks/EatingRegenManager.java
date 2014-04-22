package com.hea3ven.hardmodetweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EatingRegenManager {

	private Logger logger = LogManager
			.getLogger("HardModeTweaks.EatingRegenManager");

	@SubscribeEvent
	public void playerUseItemFinished(PlayerUseItemEvent.Finish e) {
		if (e.item.getItem().getItemUseAction(e.item) == EnumAction.eat
				&& e.item.getItem() instanceof ItemFood) {
			float healAmount = (float) Math.ceil((((ItemFood) e.item.getItem())
					.func_150905_g(e.item) - 3) / 3.0f);
			if (healAmount > 0) {
				logger.debug("Finished eating, healing for {}", healAmount);
				e.entityPlayer.heal(healAmount);
			}
		}
	}

}
