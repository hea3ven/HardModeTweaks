/**
 * Copyright (c) 2014 Hea3veN
 * <p/>
 * This file is part of HardModeTweaks.
 * <p/>
 * HardModeTweaks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * HardModeTweaks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with HardModeTweaks.  If not, see <http://www.gnu.org/licenses/>.
 */

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
		if (!event.world.isRemote && event.world.provider.getDimensionId() == 0) {
			logger.debug("Applying the game rules");
			GameRules rules = event.world.getGameRules();
			for (Entry<String, String> entry : gameRules.entrySet()) {
				rules.setOrCreateGameRule(entry.getKey(), entry.getValue());
			}
		}
	}
}
