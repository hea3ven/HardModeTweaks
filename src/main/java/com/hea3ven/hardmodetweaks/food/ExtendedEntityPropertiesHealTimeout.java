package com.hea3ven.hardmodetweaks.food;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedEntityPropertiesHealTimeout implements IExtendedEntityProperties {

	private byte timeout;

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setByte("HealTimeout", timeout);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		timeout = compound.getByte("HealTimeout");
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public void tick() {
	}

	public boolean isDone() {
		timeout++;
		if (timeout >= FoodTweaksManager.healTimeout) {
			timeout = 0;
			return true;
		} else {
			return false;
		}
	}
}
