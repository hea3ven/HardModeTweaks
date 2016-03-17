package com.hea3ven.hardmodetweaks.gamerules;

import java.util.function.Consumer;

import net.minecraft.world.GameRules;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import com.hea3ven.tools.commonutils.mod.ProxyModModule;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;

public class ProxyModHardModeTweaksGameRules extends ProxyModModule {

	private GameRulesManager rulesManager;

	@Override
	public CategoryConfigManagerBuilder getConfig() {
		CategoryConfigManagerBuilder cfg = new CategoryConfigManagerBuilder("GameRules")
				.addValue("enableGameRules", "true", Type.BOOLEAN, "Enable changing the game rules.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								if (property.getBoolean()) {
									if (rulesManager == null) {
										rulesManager = new GameRulesManager();
										MinecraftForge.EVENT_BUS.register(rulesManager);
									}
								} else {
									if (rulesManager != null) {
										MinecraftForge.EVENT_BUS.unregister(rulesManager);
										rulesManager = null;
									}
								}
							}
						});
		GameRules rules = new GameRules();
		for (String ruleName : rules.getRules()) {
			cfg = cfg.addValue(ruleName, rules.getString(ruleName), Type.STRING, "",
					new Consumer<Property>() {
						@Override
						public void accept(Property property) {
							GameRulesManager.gameRules.put(property.getName(), property.getString());
						}
					}, false, true);
		}
		return cfg;
	}
}
