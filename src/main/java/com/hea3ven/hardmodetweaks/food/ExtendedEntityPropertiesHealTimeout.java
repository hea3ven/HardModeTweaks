package com.hea3ven.hardmodetweaks.food;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ExtendedEntityPropertiesHealTimeout {

	private byte timeout;

	public void saveNBTData(NBTTagCompound compound) {
		compound.setByte("HealTimeout", timeout);
	}

	public void loadNBTData(NBTTagCompound compound) {
		timeout = compound.getByte("HealTimeout");
	}

	public void init(Entity entity, World world) {
	}

	public void tick() {
	}

	public boolean isDone() {
		timeout--;
		if (timeout <= 0) {
			timeout = FoodTweaksManager.healTimeout;
			return true;
		} else {
			return false;
		}
	}
}
