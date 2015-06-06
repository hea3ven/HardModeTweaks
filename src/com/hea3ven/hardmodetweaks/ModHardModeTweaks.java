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

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.hardmodetweaks.config.Config;
import com.hea3ven.hardmodetweaks.config.ProjectConfig;

@Mod(modid = "hardmodetweaks|main", version = ProjectConfig.version,
        dependencies = "required-after:Forge@[" + ProjectConfig.forge_version + ",)",
        guiFactory = "com.hea3ven.hardmodetweaks.config.HardModeTweaksGuiFactory")
public class ModHardModeTweaks {

    private Logger logger = LogManager.getLogger("HardModeTweaks.Mod");

    @Instance("hardmodetweaks")
    public static ModHardModeTweaks instance;

    @SidedProxy(clientSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy",
            serverSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy")
    public static HardModeTweaksCommonProxy proxy;

    public ModHardModeTweaks() {
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        logger.info("Loading config");
        Config.init(new File(event.getModConfigurationDirectory(), "hardmodetweaks.cfg"));
        FMLCommonHandler.instance().bus().register(this);
    }

    @EventHandler
    public void modInit(FMLInitializationEvent event) {
        onConfigChanged(null);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs == null || eventArgs.modID.equals("hardmodetweaks|main")) {
            logger.info("Reloading config");
            Config.reload();
            AITweaksManager.onConfigChanged();
            EatingRegenManager.onConfigChanged();
            MobsTweaksManager.onConfigChanged();
            HardModeRulesManager.onConfigChanged();
        }
    }
}
