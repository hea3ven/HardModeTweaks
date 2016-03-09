package com.hea3ven.hardmodetweaks.sleep;

import java.util.function.Consumer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import com.hea3ven.tools.commonutils.mod.ProxyModBase;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;
import com.hea3ven.tweaks.DayNightCycle;

public class ProxyModHardModeTweaksSleep extends ProxyModBase {

	private SleepManager sleepManager = null;

	public ProxyModHardModeTweaksSleep(String modId) {
		super(modId);
	}

	public CategoryConfigManagerBuilder getConfig() {
		return new CategoryConfigManagerBuilder("Sleep")
				.addValue("enableSleepPrevention", "true", Type.BOOLEAN,
						"Enable beds requiring a timeout after being placed before they can be used.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								if (property.getBoolean()) {
									if (sleepManager == null) {
										sleepManager = new SleepManager();
										MinecraftForge.EVENT_BUS.register(sleepManager);
									}
								} else {
									if (sleepManager != null) {
										MinecraftForge.EVENT_BUS.unregister(sleepManager);
										sleepManager = null;
									}
								}
							}
						})
				.addValue("sleepTimeout", "3", Type.INTEGER,
						"The number of days that must pass after placing a bed before it can be used.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								SleepManager.sleepPreventionTimeout = (int) (property.getInt() * 24000 /
										DayNightCycle.dayLengthMultiplier);
							}
						});
	}
}
