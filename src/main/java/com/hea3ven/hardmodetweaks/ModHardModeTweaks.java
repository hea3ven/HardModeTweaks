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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModHardModeTweaks.MODID, version = ModHardModeTweaks.VERSION,
		dependencies = ModHardModeTweaks.DEPENDENCIES,
		guiFactory = "com.hea3ven.hardmodetweaks.ModGuiFactoryHardModeTweaks")
public class ModHardModeTweaks {

	public static final String MODID = "hardmodetweaks";
	public static final String VERSION = "@PROJECTVERSION@";
	public static final String DEPENDENCIES = "required-after:Forge@[FORGEVERSION,)";

	public static HardModeTweaksProxy proxy = new HardModeTweaksProxy(MODID);

	public ModHardModeTweaks() {
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.onPreInitEvent(event);
	}

	@Mod.EventHandler
	public void modInit(FMLInitializationEvent event) {
		proxy.onInitEvent(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.onPostInitEvent(event);
	}
}
