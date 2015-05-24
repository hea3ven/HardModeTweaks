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

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.event.FMLServerStartedEvent;

import com.hea3ven.hardmodetweaks.config.Config;

public class HardModeRulesManager {

    private Logger logger = LogManager.getLogger("HardModeTweaks.HardModeRulesManager");

    private static HardModeRulesManager instance = null;

    public static EventBus bus;

    public static void onConfigChanged() {
        if (Config.enableGameRules) {
            if (instance == null) {
                instance = new HardModeRulesManager();
                bus.register(instance);
            }
        } else {
            if (instance != null) {
                bus.unregister(instance);
                instance = null;
            }
        }
    }

    @Subscribe
    public void serverStarted(FMLServerStartedEvent e) {
        logger.debug("Applying the game rules");
        WorldServer server = MinecraftServer.getServer().worldServerForDimension(0);
        GameRules rules = server.getGameRules();
        for (Entry<String, String> entry : Config.gameRules.entrySet()) {
            rules.setOrCreateGameRule(entry.getKey(), entry.getValue());
        }
    }
}
