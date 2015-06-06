package com.hea3ven.hardmodetweaks.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraft.world.GameRules;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.FMLLog;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {
    public static double dayLengthMultiplier;
    public static float dayToNightRatio;

    public static boolean enableTweakAnimalAI;
    public static boolean enableEatingHeal;

    public static int requiredFoodValue;
    public static float healValueOffset;
    public static float healValueMultiplier;

    public static boolean enableMobsTweaks;
    public static boolean spidersApplySlowness;
    public static boolean replaceCaveSpiderPoison;

    public static boolean enableGameRules;
    public static Map<String, String> gameRules;

    private static Config INSTANCE;

    private File configfile;
    private Configuration generalConfig;

    private Property cycleLengthMultiplierProp;
    private Property dayToNightRatioProp;

    private Property enableEatingHealProp;
    private Property enableTweakAnimalAIProp;

    private Property requiredFoodValueProp;
    private Property healValueOffsetProp;
    private Property healValueMultiplierProp;

    private Property enableMobsTweaksProp;
    private Property spidersApplySlownessProp;
    private Property replaceCaveSpiderPoisonProp;

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
        generalConfig.getCategory("FoodHealing").setLanguageKey(
                "hardmodetweaks.config.foodheal.cat");
        generalConfig.getCategory("Mobs").setLanguageKey("hardmodetweaks.config.mobs.cat");
        generalConfig.getCategory("GameRules")
                .setLanguageKey("hardmodetweaks.config.gamerules.cat");
        generalConfig.getCategory("Other").setLanguageKey("hardmodetweaks.config.other.cat");

        cycleLengthMultiplierProp = generalConfig
                .get("DayNightCycle", "cycleLengthMultiplier", 1.0d,
                        "Change the length of the day/night cycle, 1.0 is the same as vanilla, which is 20 minutes.")
                .setLanguageKey("hardmodetweaks.config.daynightcycle.cycleLenghtMult")
                .setRequiresWorldRestart(true);
        dayToNightRatioProp = generalConfig
                .get("DayNightCycle",
                        "dayToNightRatio",
                        0.5d,
                        "Ratio between the length of the day and the length of the night, values should be between 0.0 and 1.0. A value of 0.5 means day and night are the same length, a value of 0.75 means the day is longer than the night.")
                .setLanguageKey("hardmodetweaks.config.daynightcycle.daynightratio")
                .setRequiresWorldRestart(true);

        enableEatingHealProp = generalConfig.get("FoodHealing", "enableFoodHealing", true,
                "Enable the healing when you eat food.").setLanguageKey(
                "hardmodetweaks.config.foodheal.enable");
        requiredFoodValueProp = generalConfig.get("FoodHealing", "requiredFoodValue", 3,
                "The required value of the food to apply the heal.").setLanguageKey(
                "hardmodetweaks.config.foodheal.requiredvalue");
        healValueOffsetProp = generalConfig.get("FoodHealing", "healValueOffset", 1)
                .setLanguageKey("hardmodetweaks.config.foodheal.healoffset");
        healValueMultiplierProp = generalConfig.get("FoodHealing", "healValueMultiplier", 0.3d,
                "The formula for the heal is (FoodValue - healValueOffset) * healValueMultiplier")
                .setLanguageKey("hardmodetweaks.config.foodheal.healmultiplier");

        enableTweakAnimalAIProp = generalConfig.get("Other", "enableAnimalAITweak", true,
                "Enable changing the animals AI to make them run from their attackers.")
                .setLanguageKey("hardmodetweaks.config.other.animalaitweak");

        enableMobsTweaksProp = generalConfig.get("Mobs", "enableMobsTweak", true,
                "Enable tweaks for mobs.").setLanguageKey(
                "hardmodetweaks.config.mobs.enableMobsTweak");
        spidersApplySlownessProp = generalConfig.get("Mobs", "spidersApplySlowness", true,
                "Enable to make spiders apply slowness when they attack.").setLanguageKey(
                "hardmodetweaks.config.mobs.spidersApplySlowness");
        replaceCaveSpiderPoisonProp = generalConfig.get("Mobs", "replaceCaveSpiderPoison", true,
                "Enable to make it so cave spiders apply weakness in stead of poison.")
                .setLanguageKey("hardmodetweaks.config.mobs.replaceCaveSpiderPoison");

        enableGameRulesProp = generalConfig
                .get("GameRules", "enableGameRules", true, "Enable changing the game rules.")
                .setLanguageKey("hardmodetweaks.config.gamerules.enable")
                .setRequiresWorldRestart(true);
        gameRulesProps = new HashMap<String, Property>();
        GameRules rules = new GameRules();
        for (String ruleName : rules.getRules()) {
            gameRulesProps
                    .put(ruleName,
                            generalConfig.get("GameRules", ruleName,
                                    rules.getGameRuleStringValue(ruleName)).setRequiresWorldRestart(true))
                    ;
        }
    }

    private void read() {
        generalConfig.load();
    }

    private void load() {
        Config.dayLengthMultiplier = 1.0d / cycleLengthMultiplierProp.getDouble(1.0d);
        Config.dayToNightRatio = 2.0f * (float) dayToNightRatioProp.getDouble(0.5d);

        Config.enableTweakAnimalAI = enableTweakAnimalAIProp.getBoolean();

        Config.enableEatingHeal = enableEatingHealProp.getBoolean();
        Config.requiredFoodValue = requiredFoodValueProp.getInt();
        Config.healValueOffset = (float) healValueOffsetProp.getDouble(3.0d);
        Config.healValueMultiplier = (float) healValueMultiplierProp.getDouble(0.3d);

        Config.enableMobsTweaks = enableMobsTweaksProp.getBoolean();
        Config.spidersApplySlowness = spidersApplySlownessProp.getBoolean();
        Config.replaceCaveSpiderPoison = replaceCaveSpiderPoisonProp.getBoolean();

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
        elems.add(new ConfigElement(generalConfig.getCategory("Other")));
        return elems;
    }
}
