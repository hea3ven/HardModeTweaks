package com.hea3ven.hardmodetweaks.food;

import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import com.hea3ven.tools.commonutils.mod.ProxyModBase;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;

public class ProxyModHardModeTweaksFood extends ProxyModBase {
	private FoodTweaksManager foodManager;

	public ProxyModHardModeTweaksFood(String modId) {
		super(modId);
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		super.onInitEvent(event);

		if (foodManager != null)
			FoodValueTweaker.modifyFoodValues();
	}

	public CategoryConfigManagerBuilder getConfig() {
		return new CategoryConfigManagerBuilder("Food")
				.addValue("enableFoodTweaks", "true", Type.BOOLEAN,
						"Enable food tweaks. The food healing stacks with the normal natural regeneration, " +
								"so consider disabling the gamerule for that.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								if (property.getBoolean()) {
									if (foodManager == null) {
										foodManager = new FoodTweaksManager();
										MinecraftForge.EVENT_BUS.register(foodManager);
									}
								} else {
									if (foodManager != null) {
										MinecraftForge.EVENT_BUS.unregister(foodManager);
										foodManager = null;
									}
								}
							}
						}, true, true)
				.addValue("foodValues", new String[] {"minecraft:rabbit_stew|12|12.0",
								"minecraft:golden_carrot|8|14.4",
								"minecraft:pumpkin_pie|8|12.0",
								"minecraft:golden_apple|10|9.6",
								"minecraft:mushroom_stew|8|11",
								"minecraft:bread|6|11",
								"minecraft:cooked_salmon|7|6.4",
								"minecraft:cooked_fish|5|7.8",
								"minecraft:spider_eye|4|6.4",
								"minecraft:baked_potato|5|5.2",
								"minecraft:apple|4|5.2",
								"minecraft:cooked_beef|3|5.2",
								"minecraft:cooked_porkchop|3|5.2",
								"minecraft:cooked_mutton|3|5.2",
								"minecraft:cooked_chicken|3|5.2",
								"minecraft:cooked_rabbit|3|5.2",
								"minecraft:cookie|4|3.2",
								"minecraft:carrot|4|2.4",
								"minecraft:melon|4|2.4",
								"minecraft:potato|4|2.4",
								"minecraft:rotten_flesh|4|0.8",
								"minecraft:raw_salmon|2|2.4",
								"minecraft:raw_fish|2|2.4",
								"minecraft:rabbit|2|2.4",
								"minecraft:porkchop|2|2.4",
								"minecraft:beef|2|2.4",
								"minecraft:mutton|2|2.4",
								"minecraft:chicken|2|2.4",
								"minecraft:poisonous_potato|2|0.8"},
						Type.STRING,
						"Modifications to food's value and saturation."
								+ " Remove all entries to leave as default vanilla values.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								FoodValueTweaker.foodValuesConfig = Lists.newArrayList();
								for (String foodConfigString : property.getStringList()) {
									FoodConfig foodConfig = FoodConfig.parse(foodConfigString);
									if (foodConfig != null)
										FoodValueTweaker.foodValuesConfig.add(foodConfig);
								}
							}
						}, true, true)
				.addValue("healMinFoodLevel", "4", Type.INTEGER,
						"The minimum food level required to be able to heal.", new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								FoodTweaksManager.healMinFoodLevel = property.getInt();
							}
						})
				.addValue("healTimeout", "20", Type.INTEGER, "The timeout between heals.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								FoodTweaksManager.healTimeout = (byte) property.getInt();
							}
						})
				.addValue("healAmount", "0.2", Type.DOUBLE, "The amount of healing done.",
						new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								FoodTweaksManager.healAmount = (float) property.getDouble();
							}
						})
				.addValue("healExhaustion", "3.0", Type.DOUBLE,
						"The amount of exhaustion added when healing.", new Consumer<Property>() {
							@Override
							public void accept(Property property) {
								FoodTweaksManager.healExhaustion = (float) property.getDouble();
							}
						})
				;
	}
}
