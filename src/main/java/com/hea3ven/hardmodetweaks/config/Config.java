package com.hea3ven.hardmodetweaks.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.FMLLog;

import com.hea3ven.hardmodetweaks.EatingRegenManager.FoodConfig;

public class Config {
	public static boolean enableEatingHeal;

	public static int requiredFoodValue;
	public static float healValueOffset;
	public static float healValueMultiplier;
	public static List<FoodConfig> foodValuesConfig;

	private static Config INSTANCE;

	private File configfile;
	private Configuration generalConfig;

	private Property enableEatingHealProp;

	private Property requiredFoodValueProp;
	private Property healValueOffsetProp;
	private Property healValueMultiplierProp;
	private Property foodValuesConfigProp;



	private Config(File configFile) {
		this.configfile = configFile;
		generalConfig = new Configuration(configfile, true);
	}

	public static void init(File configFile) {
		try {
			INSTANCE = new Config(configFile);
			INSTANCE.read();
			INSTANCE.initProperties();
			INSTANCE.load();
			INSTANCE.save();
		} catch (Exception e) {
			FMLLog.log(Level.FATAL, e, "HardModeTweaks' configuration failed to load.");
		}
	}

	public static void reload() {
		INSTANCE.load();
		INSTANCE.save();
	}

	private void initProperties() {
		generalConfig.getCategory("DayNightCycle").setLanguageKey(
				"hardmodetweaks.config.daynightcycle.cat");
		generalConfig
				.getCategory("FoodHealing")
				.setLanguageKey("hardmodetweaks.config.foodheal.cat");
		generalConfig.getCategory("Mobs").setLanguageKey("hardmodetweaks.config.mobs.cat");
		generalConfig
				.getCategory("GameRules")
				.setLanguageKey("hardmodetweaks.config.gamerules.cat");
		generalConfig.getCategory("Sleep").setLanguageKey("hardmodetweaks.config.sleep.cat");
		generalConfig.getCategory("World").setLanguageKey("hardmodetweaks.config.world.cat");
		generalConfig.getCategory("Other").setLanguageKey("hardmodetweaks.config.other.cat");

		enableEatingHealProp = generalConfig
				.get("FoodHealing", "enableFoodHealing", true,
						"Enable the healing when you eat food.")
				.setLanguageKey("hardmodetweaks.config.foodheal.enable");
		requiredFoodValueProp = generalConfig
				.get("FoodHealing", "requiredFoodValue", 3,
						"The required value of the food to apply the heal.")
				.setLanguageKey("hardmodetweaks.config.foodheal.requiredvalue");
		healValueOffsetProp = generalConfig.get("FoodHealing", "healValueOffset", 1).setLanguageKey(
				"hardmodetweaks.config.foodheal.healoffset");
		healValueMultiplierProp = generalConfig
				.get("FoodHealing", "healValueMultiplier", 0.3d,
						"The formula for the heal is (FoodValue - healValueOffset) * healValueMultiplier")
				.setLanguageKey("hardmodetweaks.config.foodheal.healmultiplier");
		foodValuesConfigProp = generalConfig
				.get("FoodHealing", "foodValues",
						new String[] {"minecraft:rabbit_stew|12|12.0",
								"minecraft:golden_carrot|8|14.4", "minecraft:pumpkin_pie|8|12.0",
								"minecraft:golden_apple|10|9.6", "minecraft:mushroom_stew|8|11",
								"minecraft:bread|6|11", "minecraft:cooked_salmon|7|6.4",
								"minecraft:cooked_fish|5|7.8", "minecraft:spider_eye|4|6.4",
								"minecraft:baked_potato|5|5.2", "minecraft:apple|4|5.2",
								"minecraft:cooked_beef|3|5.2", "minecraft:cooked_porkchop|3|5.2",
								"minecraft:cooked_mutton|3|5.2", "minecraft:cooked_chicken|3|5.2",
								"minecraft:cooked_rabbit|3|5.2", "minecraft:cookie|4|3.2",
								"minecraft:carrot|4|2.4", "minecraft:melon|4|2.4",
								"minecraft:potato|4|2.4", "minecraft:rotten_flesh|4|0.8",
								"minecraft:raw_salmon|2|2.4", "minecraft:raw_fish|2|2.4",
								"minecraft:rabbit|2|2.4", "minecraft:porkchop|2|2.4",
								"minecraft:beef|2|2.4", "minecraft:mutton|2|2.4",
								"minecraft:chicken|2|2.4", "minecraft:poisonous_potato|2|0.8"},
						"Modifications to food's value and saturation."
								+ " Remove all entries to leave as default vanilla values.")
				.setLanguageKey("hardmodetweaks.config.foodheal.foodValues");
	}

	private void read() {
		generalConfig.load();
	}

	private void load() {
		Config.enableEatingHeal = enableEatingHealProp.getBoolean();
		Config.requiredFoodValue = requiredFoodValueProp.getInt();
		Config.healValueOffset = (float) healValueOffsetProp.getDouble();
		Config.healValueMultiplier = (float) healValueMultiplierProp.getDouble();
		foodValuesConfig = Lists.newArrayList();
		for (String foodConfigString : foodValuesConfigProp.getStringList()) {
			FoodConfig foodConfig = FoodConfig.parse(foodConfigString);
			if (foodConfig != null)
				foodValuesConfig.add(foodConfig);
		}

	}

	private void save() {
		if (generalConfig.hasChanged())
			generalConfig.save();
	}

	public static List<IConfigElement> getElements() {
		return INSTANCE.getConfigElements();
	}

	private List<IConfigElement> getConfigElements() {
		List<IConfigElement> elems = new ArrayList<IConfigElement>();
		elems.add(new ConfigElement(generalConfig.getCategory("DayNightCycle")));
		elems.add(new ConfigElement(generalConfig.getCategory("FoodHealing")));
		elems.add(new ConfigElement(generalConfig.getCategory("GameRules")));
		elems.add(new ConfigElement(generalConfig.getCategory("Mobs")));
		elems.add(new ConfigElement(generalConfig.getCategory("Sleep")));
		elems.add(new ConfigElement(generalConfig.getCategory("World")));
		elems.add(new ConfigElement(generalConfig.getCategory("Other")));
		return elems;
	}
}
