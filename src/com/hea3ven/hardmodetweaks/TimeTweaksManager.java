package com.hea3ven.hardmodetweaks;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;

public class TimeTweaksManager {
	public static double dayLengthMultiplier = 1.0d;

	public static long getWorldTime(WorldInfo worldInfo) {
		return (long) Math.floor(worldInfo.worldTime * dayLengthMultiplier);
	}

	public static void setWorldTime(WorldInfo worldInfo, long time) {
		worldInfo.worldTime = (long) Math.floor(time / dayLengthMultiplier);

	}

	public static void addTick(WorldInfo worldInfo) {
		worldInfo.worldTime += 1;
	}

	public static void addTick(WorldProvider provider) {
		addTick(provider.worldObj.getWorldInfo());
	}
}
