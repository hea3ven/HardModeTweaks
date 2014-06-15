package com.hea3ven.hardmodetweaks.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.GameRules;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class Config {
    public static double dayLengthMultiplier;
    public static float dayToNightRatio;

    public static boolean enableTweakAnimalAI;
    public static boolean enableEatingHeal;

    public static int requiredFoodValue;
    public static float healValueOffset;
    public static float healValueMultiplier;

    public static boolean enableGameRules;
    public static Map<String, String> gameRules;

    private File configDir;
    private File modConfigDir;
    private Configuration generalConfig;

    private Property cycleLengthMultiplierProp;
    private Property dayToNightRatioProp;

    private Property enableEatingHealProp;
    private Property enableTweakAnimalAIProp;

    private Property requiredFoodValueProp;
    private Property healValueOffsetProp;
    private Property healValueMultiplierProp;

    private Property enableGameRulesProp;
    private Map<String, Property> gameRulesProps;

    private Config(File configDir) {
        this.configDir = configDir;
        modConfigDir = new File(configDir, "hardmodetweaks");
        generalConfig = new Configuration(new File(modConfigDir, "general.cfg"));
    }

    public static void init(File configDir) {
        try {
            Config cfg = new Config(configDir);
            if (cfg.isOldConfigPresent()) {
                cfg.loadOldConfig();
                cfg.renameOldConfig();
            } else {
                cfg.loadFiles();
            }
            cfg.load();
            cfg.save();
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e,
                    "Pandora's Chest configuration failed to load.");
        }
    }

    private void initGeneralConfig() {
        cycleLengthMultiplierProp = generalConfig
                .get("DayNightCycle",
                        "cycleLengthMultiplier",
                        1.0d,
                        "Change the length of the day/night cycle, 1.0 is the same as vanilla, which is 20 minutes.");
        dayToNightRatioProp = generalConfig
                .get("DayNightCycle",
                        "dayToNightRatio",
                        0.5d,
                        "Ratio between the length of the day and the length of the night, values should be between 0.0 and 1.0. A value of 0.5 means day and night are the same length, a value of 0.75 means the day is longer than the night.");

        enableEatingHealProp = generalConfig.get("General", "enableFoodHealing",
                true, "Enable the healing when you eat food.");
        requiredFoodValueProp = generalConfig.get("FoodHealing",
                "requiredFoodValue", 3,
                "The required value of the food to apply the heal.");
        healValueOffsetProp = generalConfig.get("FoodHealing",
                "healValueOffset", 1);
        healValueMultiplierProp = generalConfig
                .get("FoodHealing",
                        "healValueMultiplier",
                        0.3d,
                        "The formula for the heal is (FoodValue - healValueOffset) * healValueMultiplier");

        enableTweakAnimalAIProp = generalConfig
                .get("General", "enableAnimalAITweak", true,
                        "Enable changing the animals AI to make them run from their attackers.");

        enableGameRulesProp = generalConfig.get("General", "enableGameRules",
                true, "Enable changing the game rules.");
        gameRulesProps = new HashMap<String, Property>();
        GameRules rules = new GameRules();
        for (String ruleName : rules.getRules()) {
            gameRulesProps.put(
                    ruleName,
                    generalConfig.get("GameRules", ruleName,
                            rules.getGameRuleStringValue(ruleName)));
        }
    }

    private boolean isOldConfigPresent() {
        return getOldConfigFile().exists();
    }

    private File getOldConfigFile() {
        return new File(configDir, "hardmodetweaks.cfg");
    }

    private void loadOldConfig() {
        Configuration conf = new Configuration(getOldConfigFile());
        conf.load();

        cycleLengthMultiplierProp.set(conf.get("options",
                "dayLengthMultiplier", 1.0d).getDouble(1.0d));

        enableEatingHealProp.set(conf.get("options", "doEatingRegen", true)
                .getBoolean(true));
        requiredFoodValueProp.set(conf.get("options", "foodHealingMinimum", 3)
                .getInt());
        healValueMultiplierProp.set(conf.get("options",
                "foodHealingMultiplier", 0.3d).getDouble(0.3d));

        enableTweakAnimalAIProp.set(conf.get("options", "tweakPanicAI", true)
                .getBoolean(true));

        for (String ruleName : gameRulesProps.keySet()) {
            if (conf.hasKey("gamerules", ruleName))
                gameRulesProps.get(ruleName).set(
                        conf.get("gamerules", ruleName, "").getString());
        }
    }

    private void renameOldConfig() {
        File oldConfig = getOldConfigFile();
        File renamedOldConfig = new File(oldConfig.getPath() + ".save");
        oldConfig.renameTo(renamedOldConfig);
    }

    private void loadFiles() {
        generalConfig.load();
    }

    private void load() {
        initGeneralConfig();

        Config.dayLengthMultiplier = 1.0d / cycleLengthMultiplierProp
                .getDouble(1.0d);
        Config.dayToNightRatio = 2.0f * (float) dayToNightRatioProp
                .getDouble(0.5d);

        Config.enableTweakAnimalAI = enableTweakAnimalAIProp.getBoolean(true);

        Config.enableEatingHeal = enableEatingHealProp.getBoolean(true);
        Config.requiredFoodValue = requiredFoodValueProp.getInt();
        Config.healValueOffset = (float)healValueOffsetProp.getDouble(3.0d);
        Config.healValueMultiplier = (float)healValueMultiplierProp.getDouble(0.3d);

        Config.enableGameRules = enableGameRulesProp.getBoolean(true);
        Config.gameRules = new HashMap<String, String>();
        for (String ruleName : gameRulesProps.keySet()) {
            Config.gameRules.put(ruleName, gameRulesProps.get(ruleName)
                    .getString());
        }
    }

    private void save() {
        generalConfig.save();
    }

}
