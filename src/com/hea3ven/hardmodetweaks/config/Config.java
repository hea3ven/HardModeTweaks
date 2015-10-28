package com.hea3ven.hardmodetweaks.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import org.apache.logging.log4j.Level;

import net.minecraft.world.GameRules;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.FMLLog;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.hea3ven.hardmodetweaks.EatingRegenManager.FoodConfig;
import com.hea3ven.tweaks.DayNightCycle;
import com.hea3ven.tweaks.Hea3venTweaks;

public class Config {
	public static boolean enableTweakAnimalAI;
	public static boolean enableEatingHeal;

	public static int requiredFoodValue;
	public static float healValueOffset;
	public static float healValueMultiplier;
	public static List<FoodConfig> foodValuesConfig;

	public static boolean enableMobsTweaks;
	public static boolean spidersApplySlowness;
	public static boolean replaceCaveSpiderPoison;
	public static boolean creeperSpawnTweak;
	public static float zombieKnockbackResistance;

	public static boolean enableSleepPrevention;
	public static int sleepPreventionTimeout;

	public static boolean enableGameRules;
	public static Map<String, String> gameRules;

	private static Config INSTANCE;

	private File configfile;
	private Configuration generalConfig;

	private Property enableDayCycleTweaksProp;
	private Property cycleLengthMultiplierProp;
	private Property dayToNightRatioProp;

	private Property enableEatingHealProp;
	private Property enableTweakAnimalAIProp;
	private Property enablePreventBoatBreakProp;
	private Property enableNonSolidLeavesProp;

	private Property requiredFoodValueProp;
	private Property healValueOffsetProp;
	private Property healValueMultiplierProp;
	private Property foodValuesConfigProp;

	private Property enableSleepPreventionProp;
	private Property sleepPreventionTimeoutProp;

	private Property enableMobsTweaksProp;
	private Property spidersApplySlownessProp;
	private Property replaceCaveSpiderPoisonProp;
	private Property creeperSpawnTweakProp;
	private Property zombieKnockbackResistanceProp;

	private Property enableGameRulesProp;
	private Map<String, Property> gameRulesProps;

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
		generalConfig.getCategory("Other").setLanguageKey("hardmodetweaks.config.other.cat");

		enableDayCycleTweaksProp = generalConfig
				.get("DayNightCycle", "enableDayCycleTweaks", true,
						"Enable the day/night cycle tweaks")
				.setLanguageKey("hardmodetweaks.config.daynightcycle.enableDayCycleTweaks")
				.setRequiresWorldRestart(true);
		cycleLengthMultiplierProp = generalConfig
				.get("DayNightCycle", "cycleLengthMultiplier", 1.0d,
						"Change the length of the day/night cycle, 1.0 is the same as vanilla, which is 20 minutes.")
				.setLanguageKey("hardmodetweaks.config.daynightcycle.cycleLenghtMult")
				.setRequiresWorldRestart(true);
		dayToNightRatioProp = generalConfig
				.get("DayNightCycle", "dayToNightRatio", 0.5d,
						"Ratio between the length of the day and the length of the night, values should be between 0.0 and 1.0. A value of 0.5 means day and night are the same length, a value of 0.75 means the day is longer than the night.")
				.setLanguageKey("hardmodetweaks.config.daynightcycle.daynightratio")
				.setRequiresWorldRestart(true);

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
						new String[] {"minecraft:golden_carrot|8|14.4",
								"minecraft:pumpkin_pie|8|12.0", "minecraft:golden_apple|10|9.6",
								"minecraft:mushroom_stew|8|11", "minecraft:bread|6|11",
								"minecraft:cooked_salmon|7|6.4", "minecraft:cooked_fish|5|7.8",
								"minecraft:spider_eye|4|6.4", "minecraft:baked_potato|5|5.2",
								"minecraft:apple|4|5.2", "minecraft:cooked_beef|3|5.2",
								"minecraft:cooked_porkchop|3|5.2", "minecraft:cooked_chicken|3|5.2",
								"minecraft:cookie|4|3.2", "minecraft:carrot|4|2.4",
								"minecraft:melon|4|2.4", "minecraft:potato|4|2.4",
								"minecraft:rotten_flesh|4|0.8", "minecraft:raw_salmon|2|2.4",
								"minecraft:raw_fish|2|2.4", "minecraft:porkchop|2|2.4",
								"minecraft:beef|2|2.4", "minecraft:chicken|2|2.4",
								"minecraft:poisonous_potato|2|0.8"},
						"Modifications to food's value and saturation."
								+ " Remove all entries to leave as default vanilla values.")
				.setLanguageKey("hardmodetweaks.config.foodheal.foodValues");

		enableTweakAnimalAIProp = generalConfig
				.get("Other", "enableAnimalAITweak", true,
						"Enable changing the animals AI to make them run from their attackers.")
				.setLanguageKey("hardmodetweaks.config.other.animalaitweak");
		enablePreventBoatBreakProp = generalConfig
				.get("Other", "enablePreventBoatBreak", true,
						"Enable to make it so that boats don't break when hitting blocks.")
				.setLanguageKey("hardmodetweaks.config.other.preventboatbreak")
				.setRequiresWorldRestart(true);
		enableNonSolidLeavesProp = generalConfig
				.get("Other", "enableNonSolidLeaves", true,
						"Enable tweak to make leaves blocks non-solid (like vines).")
				.setLanguageKey("hardmodetweaks.config.other.nonsolidleaves")
				.setRequiresWorldRestart(true);

		enableSleepPreventionProp = generalConfig
				.get("Sleep", "enableSleepPrevention", true,
						"Enable beds requiring a timeout after being placed before they can be used.")
				.setLanguageKey("hardmodetweaks.config.sleep.enable");
		sleepPreventionTimeoutProp = generalConfig
				.get("Sleep", "sleepTimeout", 3,
						"The number of days that must pass after placing a bed before it can be used.")
				.setLanguageKey("hardmodetweaks.config.sleep.timeout");

		enableMobsTweaksProp = generalConfig
				.get("Mobs", "enableMobsTweak", true, "Enable tweaks for mobs.")
				.setLanguageKey("hardmodetweaks.config.mobs.enableMobsTweak");
		spidersApplySlownessProp = generalConfig
				.get("Mobs", "spidersApplySlowness", true,
						"Enable to make spiders apply slowness when they attack.")
				.setLanguageKey("hardmodetweaks.config.mobs.spidersApplySlowness");
		replaceCaveSpiderPoisonProp = generalConfig
				.get("Mobs", "replaceCaveSpiderPoison", true,
						"Enable to make it so cave spiders apply weakness in stead of poison.")
				.setLanguageKey("hardmodetweaks.config.mobs.replaceCaveSpiderPoison");
		creeperSpawnTweakProp = generalConfig
				.get("Mobs", "creeperSpawnTweak", true,
						"Make it so that creepers only spawn on blocks without sky access.")
				.setLanguageKey("hardmodetweaks.config.mobs.creeperSpawnTweak");
		zombieKnockbackResistanceProp = generalConfig
				.get("Mobs", "zombieKnockbackResistance", 0.6d,
						"The value for zombies knockback resistance, use values between 0.0 and 1.0. Set to 0.0 to disable.")
				.setLanguageKey("hardmodetweaks.config.mobs.zombieKnockbackResistance");

		enableGameRulesProp = generalConfig
				.get("GameRules", "enableGameRules", true, "Enable changing the game rules.")
				.setLanguageKey("hardmodetweaks.config.gamerules.enable")
				.setRequiresWorldRestart(true);
		gameRulesProps = new HashMap<String, Property>();
		GameRules rules = new GameRules();
		for (String ruleName : rules.getRules()) {
			gameRulesProps.put(ruleName,
					generalConfig
							.get("GameRules", ruleName, rules.getGameRuleStringValue(ruleName))
							.setRequiresWorldRestart(true));
		}
	}

	private void read() {
		generalConfig.load();
	}

	private void load() {
		Hea3venTweaks.setConfig("DayNightCycle.enabled",
				Boolean.toString(enableDayCycleTweaksProp.getBoolean()));
		Hea3venTweaks.setConfig("DayNightCycle.cycleLengthMultiplier",
				Double.toString(cycleLengthMultiplierProp.getDouble()));
		DayNightCycle.dayLengthMultiplier = 1.0d / cycleLengthMultiplierProp.getDouble();
		Hea3venTweaks.setConfig("DayNightCycle.dayToNightRatio",
				Float.toString((float) dayToNightRatioProp.getDouble()));
		DayNightCycle.dayToNightRatio = 2.0f * (float) dayToNightRatioProp.getDouble();

		Config.enableTweakAnimalAI = enableTweakAnimalAIProp.getBoolean();
		Hea3venTweaks.setConfig("PreventBoatBreak.enabled",
				Boolean.toString(enablePreventBoatBreakProp.getBoolean()));
		Hea3venTweaks.setConfig("NonSolidLeaves.enabled",
				Boolean.toString(enableNonSolidLeavesProp.getBoolean()));

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

		Config.enableMobsTweaks = enableMobsTweaksProp.getBoolean();
		Config.spidersApplySlowness = spidersApplySlownessProp.getBoolean();
		Config.replaceCaveSpiderPoison = replaceCaveSpiderPoisonProp.getBoolean();
		Config.creeperSpawnTweak = creeperSpawnTweakProp.getBoolean();
		Config.zombieKnockbackResistance = (float) zombieKnockbackResistanceProp.getDouble();

		Config.enableSleepPrevention = enableSleepPreventionProp.getBoolean();
		Config.sleepPreventionTimeout = (int) (sleepPreventionTimeoutProp.getInt() * 24000
				* cycleLengthMultiplierProp.getDouble());

		Config.enableGameRules = enableGameRulesProp.getBoolean(true);
		Config.gameRules = new HashMap<String, String>();
		for (String ruleName : gameRulesProps.keySet()) {
			Config.gameRules.put(ruleName, gameRulesProps.get(ruleName).getString());
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
		elems.add(new ConfigElement(generalConfig.getCategory("Other")));
		return elems;
	}
}
