package com.hea3ven.hardmodetweaks;

import com.hea3ven.tools.commonutils.mod.ProxyModComposite;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder;

public class HardModeTweaksProxy extends ProxyModComposite {

	public HardModeTweaksProxy(String modId) {
		super(modId);

		addModule("daynightcycle",
				"com.hea3ven.hardmodetweaks.daynightcycle.ProxyModHardModeTweaksDayNightCycle");
		addModule("sleep", "com.hea3ven.hardmodetweaks.sleep.ProxyModHardModeTweaksSleep");
		addModule("food", "com.hea3ven.hardmodetweaks.food.ProxyModHardModeTweaksFood");
		addModule("gamerules", "com.hea3ven.hardmodetweaks.gamerules.ProxyModHardModeTweaksGameRules");
		addModule("mobs", "com.hea3ven.hardmodetweaks.mobs.ProxyModHardModeTweaksMobs");
		addModule("world", "com.hea3ven.hardmodetweaks.world.ProxyModHardModeTweaksWorld");
		addModule("other", "com.hea3ven.hardmodetweaks.other.ProxyModHardModeTweaksOther");
	}

	@Override
	protected void registerConfig() {
		addConfigManager(new FileConfigManagerBuilder()
				.setFileName("hardmodetweaks.cfg")
				.setDesc("Hard Mode Tweaks Config")
				.add(this.getModule("daynightcycle").getConfig())
				.add(this.getModule("sleep").getConfig())
				.add(this.getModule("food").getConfig())
				.add(this.getModule("gamerules").getConfig())
				.add(this.getModule("mobs").getConfig())
				.add(this.getModule("world").getConfig())
				.add(this.getModule("other").getConfig()));
	}
}
