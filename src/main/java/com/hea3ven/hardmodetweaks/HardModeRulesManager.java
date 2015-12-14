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

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.GameRules;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.hardmodetweaks.config.Config;

public class HardModeRulesManager {

    private Logger logger = LogManager.getLogger("HardModeTweaks.HardModeRulesManager");

    private static HardModeRulesManager instance = null;

    public static void onConfigChanged() {
        if (Config.enableGameRules) {
            if (instance == null) {
                instance = new HardModeRulesManager();
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
    public void serverStarted(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.getDimensionId() == 0) {
            logger.debug("Applying the game rules");
            GameRules rules = event.world.getGameRules();
            for (Entry<String, String> entry : Config.gameRules.entrySet()) {
                rules.setOrCreateGameRule(entry.getKey(), entry.getValue());
            }
        }
    }
}
