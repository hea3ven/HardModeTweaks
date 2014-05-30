package com.hea3ven.hardmodetweaks;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;

public class TimeTweaksManager {
	public static double dayLengthMultiplier = 1.0d;
	public static float dayToNightRatio = 1.0f;

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

	public static float calculateCelestialAngle(long time, float off) {
		int actualTime = (int) (time % 24000L);
		float timeRatio = (((float) actualTime + off) / 24000.0F);
		if (timeRatio <= (dayToNightRatio / 2.0f)) {
			timeRatio /= dayToNightRatio;
		} else {
			timeRatio = (timeRatio - (dayToNightRatio / 2.0f))
					/ (2.0f - dayToNightRatio) + 0.5f;
		}
		timeRatio -= 0.25F;
		if (timeRatio < 0.0F) {
			++timeRatio;
		}
		if (timeRatio > 1.0F) {
			--timeRatio;
		}

		float f2 = timeRatio;
		timeRatio = 1.0F - (float) ((Math.cos((double) timeRatio * Math.PI) + 1.0D) / 2.0D);
		timeRatio = f2 + (timeRatio - f2) / 3.0F;
		return timeRatio;
	}
}
