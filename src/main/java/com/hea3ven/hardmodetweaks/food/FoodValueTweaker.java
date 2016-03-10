package com.hea3ven.hardmodetweaks.food;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood.FishType;
import net.minecraft.item.ItemFood;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FoodValueTweaker {
	private static Logger logger = LogManager.getLogger("HardModeTweaks.Food");

	public static List<FoodConfig> foodValuesConfig;

	public static void modifyFoodValues() {
		for (FoodConfig foodCfg : foodValuesConfig) {
			if (foodCfg.getName().equals("minecraft:raw_fish")) {
				modifyFishFoodValue(FishType.COD, foodCfg, false);
			} else if (foodCfg.getName().equals("minecraft:cooked_fish")) {
				modifyFishFoodValue(FishType.COD, foodCfg, true);
			} else if (foodCfg.getName().equals("minecraft:raw_salmon")) {
				modifyFishFoodValue(FishType.SALMON, foodCfg, false);
			} else if (foodCfg.getName().equals("minecraft:cooked_salmon")) {
				modifyFishFoodValue(FishType.SALMON, foodCfg, true);
			} else {
				Item item = Item.getByNameOrId(foodCfg.getName());
				if (!(item instanceof ItemFood)) {
					logger.warn("The item '{}' is not a food item", foodCfg.getName());
					continue;
				}
				ItemFood food = (ItemFood) item;

				ReflectionHelper.setPrivateValue(ItemFood.class, food, foodCfg.getValue(), 1);
				ReflectionHelper.setPrivateValue(ItemFood.class, food, foodCfg.getSaturation(), 2);
			}
		}
	}

	private static void modifyFishFoodValue(FishType fishType, FoodConfig foodCfg, boolean cooked) {
		ReflectionHelper.setPrivateValue(FishType.class, fishType, foodCfg.getValue(),
				!cooked ? 7 : 9);
		ReflectionHelper.setPrivateValue(FishType.class, fishType, foodCfg.getSaturation(),
				!cooked ? 8 : 10);
	}
}
