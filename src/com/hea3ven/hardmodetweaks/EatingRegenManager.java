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

import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hea3ven.hardmodetweaks.config.Config;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EatingRegenManager {

    private Logger logger = LogManager
            .getLogger("HardModeTweaks.EatingRegenManager");

    private static final int HUNGER_POTION_ID = 17;
    private static final int POISON_POTION_ID = 19;
    private static final int WITHER_POTION_ID = 20;

    @SubscribeEvent
    public void playerUseItemFinished(PlayerUseItemEvent.Finish e) {
        logger.debug("Event PlayerUseItemEvent.Finish received");
        if (Config.enableEatingHeal
                && e.item.getItem().getItemUseAction(e.item) == EnumAction.eat
                && e.item.getItem() instanceof ItemFood) {
            logger.debug("Finished eating");
            if (negativePotionEffect(e.item.getItem()))
                return;
            int foodValue = ((ItemFood) e.item.getItem()).func_150905_g(e.item);
            if (foodValue > Config.requiredFoodValue) {
                float healAmount = (foodValue - Config.healValueOffset)
                        * Config.healValueMultiplier;
                logger.debug("Healing for {}", healAmount);
                e.entityPlayer.heal(healAmount);
            }
        }
    }

    private boolean negativePotionEffect(Item item) {
        int foodPotionId = ((ItemFood) item).potionId;
        return foodPotionId == HUNGER_POTION_ID
                || foodPotionId == POISON_POTION_ID
                || foodPotionId == WITHER_POTION_ID;
    }
}
