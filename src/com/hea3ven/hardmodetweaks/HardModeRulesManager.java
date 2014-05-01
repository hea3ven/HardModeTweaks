/**
 * 
 * Copyright (c) 2014 Hea3veN
 * 
 *  This file is part of HardModeTweaks.
 *
 *  HardModeTweaks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  HardModeTweaks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with HardModeTweaks.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
