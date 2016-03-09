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

import com.hea3ven.hardmodetweaks.daynightcycle.ProxyModHardModeTweaksDayNightCycle;
import com.hea3ven.hardmodetweaks.gamerules.ProxyModHardModeTweaksGameRules;
import com.hea3ven.hardmodetweaks.mobs.ProxyModHardModeTweaksMobs;
import com.hea3ven.hardmodetweaks.other.ProxyModHardModeTweaksOther;
import com.hea3ven.hardmodetweaks.sleep.ProxyModHardModeTweaksSleep;
import com.hea3ven.hardmodetweaks.world.ProxyModHardModeTweaksWorld;
import com.hea3ven.tools.commonutils.mod.ProxyModComposite;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder;

public class HardModeTweaksProxy extends ProxyModComposite {

	public HardModeTweaksProxy(String modId) {
		super(modId);

		addModule("daynightcycle", "com.hea3ven.hardmodetweaks.daynightcycle.ProxyModHardModeDayNightCycle");
		addModule("sleep", "com.hea3ven.hardmodetweaks.sleep.ProxyModHardModeTweaksSleep");
		addModule("gamerules", "com.hea3ven.hardmodetweaks.sleep.ProxyModHardModeGameRules");
		addModule("mobs", "com.hea3ven.hardmodetweaks.mobs.ProxyModHardModeTweaksMobs");
		addModule("world", "com.hea3ven.hardmodetweaks.world.ProxyModHardModeTweaksWorld");
		addModule("other", "com.hea3ven.hardmodetweaks.other.ProxyModHardModeTweaksOther");
	}

	@Override
	protected void registerConfig() {
		addConfigManager(new FileConfigManagerBuilder()
				.setFileName("hardmodetweaks.cfg")
				.setDesc("Hard Mode Tweaks Config")
				.add(this.<ProxyModHardModeTweaksDayNightCycle>getModule("daynightcycle").getConfig())
				.add(this.<ProxyModHardModeTweaksSleep>getModule("sleep").getConfig())
				.add(this.<ProxyModHardModeTweaksGameRules>getModule("gamerules").getConfig())
				.add(this.<ProxyModHardModeTweaksMobs>getModule("mobs").getConfig())
				.add(this.<ProxyModHardModeTweaksWorld>getModule("world").getConfig())
				.add(this.<ProxyModHardModeTweaksOther>getModule("other").getConfig()));
	}
}
