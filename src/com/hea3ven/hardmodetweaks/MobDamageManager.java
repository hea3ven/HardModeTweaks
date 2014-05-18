package com.hea3ven.hardmodetweaks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MobDamageManager {

	private Logger logger = LogManager
			.getLogger("HardModeTweaks.MobDamageManager");

	public double skeletonDamageMultiplier = 1.0d;
	public int creeperExplosionRadius = 3;
	public double zombieDamageMultiplier = 1.0d;
	public double spiderDamageMultiplier = 1.0d;
	public double endermanDamageMultiplier = 1.0d;
	public int ghastExplosionRadius = 1;
	public double silverfishDamageMultiplier = 1.0d;
	public boolean arrowsIgnoreArmor = true;

	@SubscribeEvent
	public void specialSpawnEvent(SpecialSpawn e) {
		if (e.entityLiving.isCreatureType(EnumCreatureType.monster, false)) {
			String entityName = e.entityLiving.getCommandSenderName();
			if (entityName.equals("Zombie")) {
				tweakZombie(e.entityLiving);
			} else if (entityName.equals("Creeper")) {
				tweakCreeper(e);
			} else if (entityName.equals("Spider")) {
				tweakSpider(e.entityLiving);
			} else if (entityName.equals("Enderman")) {
				tweakEnderman(e.entityLiving);
			} else if (entityName.equals("Zombie Pigman")) {
				tweakZombiePigman(e.entityLiving);
			} else if (entityName.equals("Ghast")) {
				tweakGhast(e.entityLiving);
			}
		}
	}

	@SubscribeEvent
	public void entityJoinWorldEvent(EntityJoinWorldEvent e) {
		if (e.entity instanceof EntityArrow) {
			EntityArrow arrow = (EntityArrow) e.entity;
			if (arrow.shootingEntity != null
					&& arrow.shootingEntity.getCommandSenderName().equals(
							"Skeleton")) {
				tweakSkeletonArrow(arrow);
			}
		} else if (e.entity instanceof EntitySilverfish) {
			tweakSilverfish((EntitySilverfish) e.entity);
		}
	}

	@SubscribeEvent
	public void livingAttackEvent(LivingAttackEvent e) {
		if (arrowsIgnoreArmor && e.source.getDamageType() == "arrow")
			e.source.setDamageBypassesArmor();
	}

	private void tweakZombie(EntityLivingBase zombie) {
		logger.trace("Changing zombie data");
		IAttributeInstance dmgAttr = zombie.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.attackDamage);
		dmgAttr.setBaseValue(dmgAttr.getBaseValue() * zombieDamageMultiplier);
	}

	private void tweakSpider(EntityLivingBase spider) {
		logger.trace("Changing spider data");
		IAttributeInstance dmgAttr = spider.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.attackDamage);
		dmgAttr.setBaseValue(dmgAttr.getBaseValue() * spiderDamageMultiplier);
	}

	private void tweakCreeper(SpecialSpawn e) {
		logger.trace("Changing creeper data");
		((EntityCreeper) e.entityLiving).explosionRadius = creeperExplosionRadius;
	}

	private void tweakSkeletonArrow(EntityArrow arrow) {
		logger.trace("Changing arrow data");
		arrow.setDamage(arrow.getDamage() * skeletonDamageMultiplier);
	}

	private void tweakEnderman(EntityLivingBase enderman) {
		logger.trace("Changing enderman data");
		IAttributeInstance dmgAttr = enderman.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.attackDamage);
		dmgAttr.setBaseValue(dmgAttr.getBaseValue() * endermanDamageMultiplier);
	}

	private void tweakZombiePigman(EntityLivingBase zombiePigman) {
		logger.trace("Changing zombie pigman data");
		IAttributeInstance dmgAttr = zombiePigman.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.attackDamage);
		dmgAttr.setBaseValue(dmgAttr.getBaseValue() * endermanDamageMultiplier);
	}

	private void tweakGhast(EntityLivingBase ghast) {
		logger.trace("Changing creeper data");
		((EntityGhast) ghast).explosionStrength = ghastExplosionRadius;
	}

	private void tweakSilverfish(EntityLivingBase silverfish) {
		logger.trace("Changing silverfish data");
		IAttributeInstance dmgAttr = silverfish.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.attackDamage);
		dmgAttr.setBaseValue(dmgAttr.getBaseValue()
				* silverfishDamageMultiplier);
	}

}
