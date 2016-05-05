package com.hea3ven.hardmodetweaks.gamerules;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.GameRules;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameRulesManager {

	private Logger logger = LogManager.getLogger("HardModeTweaks.GameRulesManager");

	public static Map<String, String> gameRules = new HashMap<>();

	@SubscribeEvent
	public void serverStarted(WorldEvent.Load event) {
		if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0) {
			logger.debug("Applying the game rules");
			GameRules rules = event.getWorld().getGameRules();
			for (Entry<String, String> entry : gameRules.entrySet()) {
				rules.setOrCreateGameRule(entry.getKey(), entry.getValue());
			}
		}
	}
}
