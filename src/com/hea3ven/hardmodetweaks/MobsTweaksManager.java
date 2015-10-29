package com.hea3ven.hardmodetweaks;

import java.util.HashSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
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

	@SubscribeEvent
	public void onLivingSpawnCheckSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
		if (Config.creeperSpawnTweak) {
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
		if (Config.zombieKnockbackResistance > 0) {
			if (event.entityLiving instanceof EntityZombie) {
				EntityZombie zombie = (EntityZombie) event.entityLiving;
				zombie
						.getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
						.applyModifier(new AttributeModifier("Base value",
								Config.zombieKnockbackResistance, 0));
			}
		}
	}
}
