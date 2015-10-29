/**
 * 
 * Copyright (c) 2014 Hea3veN
 * 
 *  This file is part of HardModeTweaks.
 *
 *  HardModeTweaks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  HardModeTweaks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with HardModeTweaks.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.hea3ven.hardmodetweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood.FishType;
import net.minecraft.item.ItemFood;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.hea3ven.hardmodetweaks.config.Config;

public class EatingRegenManager {

    private static final Logger logger = LogManager.getLogger("HardModeTweaks.EatingRegenManager");

    private static final int HUNGER_POTION_ID = 17;
    private static final int POISON_POTION_ID = 19;
    private static final int WITHER_POTION_ID = 20;

    private static EatingRegenManager instance = null;

    public static void onConfigChanged() {
        if (Config.enableEatingHeal) {
            if (instance == null) {
                instance = new EatingRegenManager();
                instance.modifyFoodValues();
                MinecraftForge.EVENT_BUS.register(instance);
            }
        } else {
            if (instance != null) {
                MinecraftForge.EVENT_BUS.unregister(instance);
                instance = null;
            }
        }
    }

    public void modifyFoodValues() {
        for (FoodConfig foodCfg : Config.foodValuesConfig) {
            if (foodCfg.getName().equals("minecraft:raw_fish")) {
                modifyFishFoodValue(FishType.COD, foodCfg, false);
            } else if (foodCfg.getName().equals("minecraft:cooked_fish")) {
                modifyFishFoodValue(FishType.COD, foodCfg, true);
            } else if (foodCfg.getName().equals("minecraft:raw_salmon")) {
                modifyFishFoodValue(FishType.SALMON, foodCfg, false);
            } else if (foodCfg.getName().equals("minecraft:cooked_salmon")) {
                modifyFishFoodValue(FishType.SALMON, foodCfg, true);
            } else {
                Item item = (Item) Item.itemRegistry.getObject(foodCfg.getName());
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

    private void modifyFishFoodValue(FishType fishType, FoodConfig foodCfg, boolean cooked) {
        ReflectionHelper.setPrivateValue(FishType.class, fishType, foodCfg.getValue(),
                !cooked ? 9 : 11);
        ReflectionHelper.setPrivateValue(FishType.class, fishType, foodCfg.getSaturation(),
                !cooked ? 10 : 12);
    }

    @SubscribeEvent
    public void playerUseItemFinished(PlayerUseItemEvent.Finish e) {
        logger.debug("Event PlayerUseItemEvent.Finish received");
        if (Config.enableEatingHeal && e.item.getItem().getItemUseAction(e.item) == EnumAction.EAT
                && e.item.getItem() instanceof ItemFood) {
            logger.debug("Finished eating");
            if (negativePotionEffect(e.item.getItem()))
                return;
            int foodValue = ((ItemFood) e.item.getItem()).getHealAmount(e.item);
            if (foodValue > Config.requiredFoodValue) {
                float healAmount = (foodValue - Config.healValueOffset)
                        * Config.healValueMultiplier;
                logger.debug("Healing for {}", healAmount);
                e.entityPlayer.heal(healAmount);
            }
        }
    }

    private boolean negativePotionEffect(Item item) {
        int foodPotionId = ReflectionHelper.getPrivateValue(ItemFood.class, (ItemFood)item, 5);
        return foodPotionId == HUNGER_POTION_ID || foodPotionId == POISON_POTION_ID
                || foodPotionId == WITHER_POTION_ID;
    }

    public static class FoodConfig {
        private String name;
        private int value;
        private float saturation;

        public FoodConfig(String name, int value, float saturation) {
            this.name = name;
            this.value = value;
            this.saturation = saturation;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public float getSaturation() {
            return saturation;
        }

        public static FoodConfig parse(String foodConfigString) {
            String[] parts = foodConfigString.split("\\|", 3);
            if (parts.length != 3) {
                logger.warn("Could not parse food config from '{}'", foodConfigString);
                return null;
            }
            try {
                return new FoodConfig(parts[0], Integer.parseInt(parts[1]),
                        Float.parseFloat(parts[2]));
            } catch (NumberFormatException e) {
                return null;
            }
        }

    }

}
