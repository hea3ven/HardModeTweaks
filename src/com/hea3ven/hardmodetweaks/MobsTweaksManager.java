package com.hea3ven.hardmodetweaks;

import java.util.HashSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.hardmodetweaks.config.Config;

public class MobsTweaksManager {
    private static MobsTweaksManager instance;

    public static void onConfigChanged() {
        if (Config.enableMobsTweaks) {
            if (instance == null) {
                instance = new MobsTweaksManager();
                MinecraftForge.EVENT_BUS.register(instance);
            }
        } else {
            if (instance != null) {
                MinecraftForge.EVENT_BUS.unregister(instance);
                instance = null;
            }
        }
    }

    private HashSet<EntityLivingBase> poisonedEntities = new HashSet<EntityLivingBase>();

    @SubscribeEvent
    public void onLivingAttackEvent(LivingAttackEvent event) {
        if (Config.spidersApplySlowness) {
            if (event.source.getDamageType().equals("mob")
                    && event.source.getEntity() instanceof EntitySpider) {
                event.entityLiving
                        .addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 1));
            }
        }

        if (Config.replaceCaveSpiderPoison) {
            if (event.source.getDamageType().equals("mob")
                    && event.source.getEntity() instanceof EntityCaveSpider) {
                poisonedEntities.add(event.entityLiving);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event) {
        if (poisonedEntities.contains(event.entityLiving)) {
            event.entityLiving.removePotionEffect(Potion.poison.id);
            event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 15 * 20));
            poisonedEntities.remove(event.entityLiving);
        }
    }
}
