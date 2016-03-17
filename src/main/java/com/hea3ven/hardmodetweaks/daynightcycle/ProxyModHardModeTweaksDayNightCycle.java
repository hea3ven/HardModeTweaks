package com.hea3ven.hardmodetweaks.daynightcycle;

import java.util.function.Consumer;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import com.hea3ven.tools.commonutils.mod.ProxyModModule;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;
import com.hea3ven.tweaks.DayNightCycle;
import com.hea3ven.tweaks.Hea3venTweaks;

public class ProxyModHardModeTweaksDayNightCycle extends ProxyModModule {
	@Override
	public CategoryConfigManagerBuilder getConfig() {
		return new CategoryConfigManagerBuilder("DayNightCycle")
				.addValue("enableDayCycleTweaks", "true", Type.BOOLEAN, "Enable the day/night cycle tweaks",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								Hea3venTweaks.setConfig("DayNightCycle.enabled", property.getString());
							}
						}, true, true)
				.addValue("cycleLengthMultiplier", "1.0", Type.DOUBLE, "Change the length of the day/night " +
						"cycle, 1.0 is the same as vanilla, which is 20 minutes.", new Consumer<Property>() {
					@Override
					public void accept(Property property) {
						Hea3venTweaks.setConfig("DayNightCycle.cycleLengthMultiplier",
								Double.toString(property.getDouble()));
						DayNightCycle.dayLengthMultiplier = 1.0d / property.getDouble();
					}
				}, false, true)
				.addValue("dayToNightRatio", "0.5", Type.DOUBLE, "Ratio between the length of the day and " +
						"the length of the night, values should be between 0.0 and 1.0. A value of 0.5 " +
						"means day and night are the same length, a value of 0.75 means the day is longer " +
						"than the night.", new Consumer<Property>() {
					@Override
					public void accept(Property property) {
						Hea3venTweaks.setConfig("DayNightCycle.dayToNightRatio",
								Float.toString((float) property.getDouble()));
						DayNightCycle.dayToNightRatio = 2.0f * (float) property.getDouble();
					}
				}, false, true);
	}
}
