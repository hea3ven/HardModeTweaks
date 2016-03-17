package com.hea3ven.hardmodetweaks.other;

import java.util.function.Consumer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import com.hea3ven.tools.commonutils.mod.ProxyModModule;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;
import com.hea3ven.tweaks.Hea3venTweaks;

public class ProxyModHardModeTweaksOther extends ProxyModModule {
	private AITweaksManager aiManager;

	@Override
	public CategoryConfigManagerBuilder getConfig() {
		return new CategoryConfigManagerBuilder("Other")
				.addValue("enableAnimalAITweak", "true", Type.BOOLEAN,
						"Enable changing the animals AI to make them run from their attackers.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								if (property.getBoolean()) {
									if (aiManager == null) {
										aiManager = new AITweaksManager();
										MinecraftForge.EVENT_BUS.register(aiManager);
									}
								} else {
									if (aiManager != null) {
										MinecraftForge.EVENT_BUS.unregister(aiManager);
										aiManager = null;
									}
								}
							}
						})
				.addValue("enablePreventBoatBreak", "true", Type.BOOLEAN,
						"Enable to make it so that boats don't break when hitting blocks.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								Hea3venTweaks.setConfig("PreventBoatBreak.enabled",
										Boolean.toString(property.getBoolean()));
							}
						}, true, true)
				.addValue("enableNonSolidLeaves", "true", Type.BOOLEAN,
						"Enable tweak to make leaves blocks non-solid (like vines).",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								Hea3venTweaks.setConfig("NonSolidLeaves.enabled",
										Boolean.toString(property.getBoolean()));
							}
						}, true, true);
	}
}
