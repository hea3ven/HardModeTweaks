package com.hea3ven.hardmodetweaks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.event.FMLServerStartedEvent;

public class HardModeRulesManager {

	private Logger logger = LogManager
			.getLogger("HardModeTweaks.HardModeRulesManager");

	private Map<String, String> rulesValues;

	public HardModeRulesManager() {
		rulesValues = new HashMap<String, String>();
	}

	@Subscribe
	public void serverStarted(FMLServerStartedEvent e) {
		logger.debug("Event FMLServerStartedEvent received");
		WorldServer server = MinecraftServer.getServer()
				.worldServerForDimension(0);
		if (server.difficultySetting == EnumDifficulty.HARD) {
			logger.info("Assinging hard mode rules");
			GameRules rules = server.getGameRules();
			for (Entry<String, String> entry : rulesValues.entrySet()) {
				rules.setOrCreateGameRule(entry.getKey(), entry.getValue());
			}
		} else {
			logger.info("Assinging default rules");
			GameRules defaultRules = new GameRules();
			GameRules rules = server.getGameRules();
			for (String ruleName : defaultRules.getRules()) {
				rules.setOrCreateGameRule(ruleName,
						defaultRules.getGameRuleStringValue(ruleName));
			}
		}
	}

	public void setRule(String ruleName, String value) {
		rulesValues.put(ruleName, value);
	}
}
