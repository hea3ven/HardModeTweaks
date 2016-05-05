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
