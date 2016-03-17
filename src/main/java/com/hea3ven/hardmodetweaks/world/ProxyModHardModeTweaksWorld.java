package com.hea3ven.hardmodetweaks.world;

import java.util.function.Consumer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import com.hea3ven.tools.commonutils.mod.ProxyModModule;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;

public class ProxyModHardModeTweaksWorld extends ProxyModModule {
	private WorldTweaksManager worldManager;

	@Override
	public CategoryConfigManagerBuilder getConfig() {
		return new CategoryConfigManagerBuilder("World")
				.addValue("enableWorldTweaks", "true", Type.BOOLEAN,
						"Enable world tweaks.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								if (property.getBoolean()) {
									if (worldManager == null) {
										worldManager = new WorldTweaksManager();
										MinecraftForge.EVENT_BUS.register(worldManager);
									}
								} else {
									if (worldManager != null) {
										MinecraftForge.EVENT_BUS.unregister(worldManager);
										worldManager = null;
									}
								}
							}
						})
				.addValue("maxBreakSpeed", "300", Type.INTEGER,
						"The maximum break speed allowed. For reference, diamond pickaxe is a" +
								" break speed of 8, and with efficiency V it's speed is 34",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								WorldTweaksManager.maxBreakSpeed = property.getInt();
							}
						});
	}
}
