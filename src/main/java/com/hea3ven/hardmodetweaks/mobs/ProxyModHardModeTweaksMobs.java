package com.hea3ven.hardmodetweaks.mobs;

import java.util.function.Consumer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import com.hea3ven.tools.commonutils.mod.ProxyModBase;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;

public class ProxyModHardModeTweaksMobs extends ProxyModBase {
	private MobsTweaksManager mobsManager;

	public ProxyModHardModeTweaksMobs(String modId) {
		super(modId);
	}

	public CategoryConfigManagerBuilder getConfig() {
		return new CategoryConfigManagerBuilder("Sleep")
				.addValue("enableMobsTweak", "true", Type.BOOLEAN, "Enable tweaks for mobs.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								if (property.getBoolean()) {
									if (mobsManager == null) {
										mobsManager = new MobsTweaksManager();
										MinecraftForge.EVENT_BUS.register(mobsManager);
									}
								} else {
									if (mobsManager != null) {
										MinecraftForge.EVENT_BUS.unregister(mobsManager);
										mobsManager = null;
									}
								}
							}
						})
				.addValue("spidersApplySlowness", "true", Type.BOOLEAN,
						"Enable to make spiders apply slowness when they attack.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								MobsTweaksManager.spidersApplySlowness = property.getBoolean();
							}
						})
				.addValue("replaceCaveSpiderPoison", "true", Type.BOOLEAN,
						"Enable to make it so cave spiders apply weakness in stead of poison.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								MobsTweaksManager.replaceCaveSpiderPoison = property.getBoolean();
							}
						})
				.addValue("creeperSpawnTweak", "true", Type.BOOLEAN,
						"Make it so that creepers only spawn on blocks without sky access.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								MobsTweaksManager.creeperSpawnTweak = property.getBoolean();
							}
						})
				.addValue("zombieKnockbackResistance", "0.6d", Type.DOUBLE,
						"The value for zombies knockback resistance, use values between 0.0 and 1.0." +
								" Set to 0.0 to disable.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								MobsTweaksManager.zombieKnockbackResistance = (float) property.getDouble();
							}
						});
	}
}
