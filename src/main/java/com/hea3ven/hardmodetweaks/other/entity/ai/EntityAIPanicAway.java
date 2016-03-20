package com.hea3ven.hardmodetweaks.other.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIPanicAway extends EntityAIBase {
	private EntityCreature creature;
	private double speed;
	private double randPosX;
	private double randPosY;
	private double randPosZ;

	public EntityAIPanicAway(EntityCreature creature, double speed) {
		this.creature = creature;
		this.speed = speed;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.creature.getAITarget() == null && !this.creature.isBurning()) {
			return false;
		} else {

			Vec3d vec3;
			if (this.creature.getAITarget() == null)
				vec3 = RandomPositionGenerator.findRandomTarget(this.creature,
						5, 4);
			else
				vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
						this.creature, 5, 4, new Vec3d(
								this.creature.getAITarget().posX,
								this.creature.getAITarget().posY,
								this.creature.getAITarget().posZ));

			if (vec3 == null) {
				return false;
			} else {
				this.randPosX = vec3.xCoord;
				this.randPosY = vec3.yCoord;
				this.randPosZ = vec3.zCoord;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY,
				this.randPosZ, this.speed);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.creature.getNavigator().noPath();
	}
}
