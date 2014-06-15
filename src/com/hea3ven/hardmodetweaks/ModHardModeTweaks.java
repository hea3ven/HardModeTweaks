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

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.hea3ven.hardmodetweaks.config.Config;

import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ModHardModeTweaks {

    private Logger logger = LogManager.getLogger("HardModeTweaks.Mod");

    @Instance("hardmodetweaks")
    public static ModHardModeTweaks instance;

    @SidedProxy(clientSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy", serverSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy")
    public static HardModeTweaksCommonProxy proxy;

    private HardModeRulesManager rulesManager;
    private EatingRegenManager eatingRegenManager;
    private MobDamageManager mobDamageManager;
    private AITweaksManager aiTweaksManager;

    private boolean doEatingRegen;

    public ModHardModeTweaks() {
        rulesManager = new HardModeRulesManager();
        eatingRegenManager = new EatingRegenManager();
        mobDamageManager = new MobDamageManager();
        aiTweaksManager = new AITweaksManager();
        doEatingRegen = true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {

        logger.info("Loading config");
        Config.init(event.getModConfigurationDirectory());
    }

    @Subscribe
    public void modInit(FMLInitializationEvent event) {
        logger.debug("Registering event listeners on the forge bus");
        if (doEatingRegen)
            MinecraftForge.EVENT_BUS.register(eatingRegenManager);
        MinecraftForge.EVENT_BUS.register(mobDamageManager);
        MinecraftForge.EVENT_BUS.register(aiTweaksManager);
    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent event) {
    }

    public void registerBus(EventBus bus) {
        bus.register(rulesManager);
    }

}
