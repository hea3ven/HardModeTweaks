package com.hea3ven.hardmodetweaks.mobs;

import java.util.HashSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobsTweaksManager {
	public static boolean spidersApplySlowness;
	public static boolean replaceCaveSpiderPoison;
	public static boolean creeperSpawnTweak;
	public static float zombieKnockbackResistance;

	private HashSet<EntityLivingBase> poisonedEntities = new HashSet<>();

	@SubscribeEvent
	public void onLivingAttackEvent(LivingAttackEvent event) {
		if (spidersApplySlowness) {
			if (event.source.getDamageType().equals("mob")
					&& event.source.getEntity() instanceof EntitySpider) {
				event.entityLiving
						.addPotionEffect(new PotionEffect(MobEffects.moveSlowdown, 200, 1));
			}
		}

		if (replaceCaveSpiderPoison) {
			if (event.source.getDamageType().equals("mob")
					&& event.source.getEntity() instanceof EntityCaveSpider) {
				poisonedEntities.add(event.entityLiving);
			}
		}
	}

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		if (poisonedEntities.contains(event.entityLiving)) {
			event.entityLiving.removePotionEffect(MobEffects.poison);
			event.entityLiving.addPotionEffect(new PotionEffect(MobEffects.weakness, 15 * 20));
			poisonedEntities.remove(event.entityLiving);
		}
	}

	@SubscribeEvent
	public void onLivingSpawnCheckSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
		if (creeperSpawnTweak) {
			if (event.entityLiving instanceof EntityCreeper) {
				int skyLight = event.world.getLightFromNeighborsFor(EnumSkyBlock.SKY,
						new BlockPos(event.x, event.y, event.z));
				if (skyLight > 8) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingSpawnSpecialSpawnEvent(LivingSpawnEvent.SpecialSpawn event) {
		if (zombieKnockbackResistance > 0) {
			if (event.entityLiving instanceof EntityZombie) {
				EntityZombie zombie = (EntityZombie) event.entityLiving;
				zombie
						.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE)
						.applyModifier(new AttributeModifier("Base value",
								zombieKnockbackResistance, 0));
			}
		}
	}
}
