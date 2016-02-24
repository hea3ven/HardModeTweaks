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

package com.hea3ven.hardmodetweaks;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.hardmodetweaks.config.Config;

@Mod(modid = ModHardModeTweaks.MODID, version = ModHardModeTweaks.VERSION,
		dependencies = ModHardModeTweaks.DEPENDENCIES,
		guiFactory = "com.hea3ven.hardmodetweaks.config.HardModeTweaksGuiFactory")
public class ModHardModeTweaks {

	public static final String MODID = "hardmodetweaks|main";
	public static final String VERSION = "@PROJECTVERSION@";
	public static final String DEPENDENCIES = "required-after:Forge@[FORGEVERSION,)";

	private Logger logger = LogManager.getLogger("HardModeTweaks.Mod");

	@Instance("hardmodetweaks")
	public static ModHardModeTweaks instance;

	@SidedProxy(clientSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy",
			serverSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy")
	public static HardModeTweaksCommonProxy proxy;

	public ModHardModeTweaks() {
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		logger.info("Loading config");
		Config.init(new File(event.getModConfigurationDirectory(), "hardmodetweaks.cfg"));
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	public void modInit(FMLInitializationEvent event) {
		onConfigChanged(null);
	}

	@Mod.EventHandler
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
			SleepManager.onConfigChanged();
			WorldTweaksManager.onConfigChanged();
		}
	}
}
