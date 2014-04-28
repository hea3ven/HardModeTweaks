/**
 * 
 * Copyright (c) 2014 Hea3veN
 * 
 *  This file is part of Angel Wings.
 *
 *  Pandora's Chest is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Pandora's Chest is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Pandora's Chest.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.hea3ven.hardmodetweaks;

import java.io.File;

import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid = "hardmodetweaks", version = "1.0a1", dependencies = "required-after:Forge@[10.12.0.1024,)")
public class ModHardModeTweaks {

	private Logger logger = LogManager.getLogger("HardModeTweaks.Mod");

	@Instance("hardmodetweaks")
	public static ModHardModeTweaks instance;

	@SidedProxy(clientSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy", serverSide = "com.hea3ven.hardmodetweaks.HardModeTweaksCommonProxy")
	public static HardModeTweaksCommonProxy proxy;

	public static double dayLengthMultiplier = 1.0d;

	private HardModeRulesManager rulesManager;
	private EatingRegenManager eatingRegenManager;

	public ModHardModeTweaks() {
		rulesManager = new HardModeRulesManager();
		eatingRegenManager = new EatingRegenManager();
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {

		logger.info("Loading config");
		File cfgFile = event.getSuggestedConfigurationFile();
		loadConfig(cfgFile);
	}

	@Subscribe
	public void modInit(FMLInitializationEvent event) {
		logger.debug("Registering event listeners on the forge bus");
		MinecraftForge.EVENT_BUS.register(eatingRegenManager);
	}

	@Subscribe
	public void postInit(FMLPostInitializationEvent event) {
	}

	private void loadConfig(File cfgFile) {
		Configuration cfg = new Configuration(cfgFile);
		try {
			cfg.load();

			dayLengthMultiplier = 1.0d / cfg.get("options",
					"dayLenthMultiplier", 1.0d).getDouble(1.0d);

			GameRules rules = new GameRules();
			for (String ruleName : rules.getRules()) {
				rulesManager.setRule(
						ruleName,
						cfg.get("gamerules", ruleName,
								rules.getGameRuleStringValue(ruleName))
								.getString());
			}
		} catch (Exception e) {
			FMLLog.log(Level.FATAL, e,
					"Pandora's Chest configuration failed to load.");
		} finally {
			cfg.save();
		}
	}

	public void registerBus(EventBus bus) {
		bus.register(rulesManager);
	}

}
