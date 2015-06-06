package com.hea3ven.hardmodetweaks;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;

import com.hea3ven.hardmodetweaks.config.Config;

public class TimeTweaksManager {
    public static long getWorldTime(WorldInfo worldInfo) {
        if (Config.enableDayCycleTweaks)
            return (long) Math.floor(worldInfo.worldTime * Config.dayLengthMultiplier);
        else
            return worldInfo.worldTime;
    }

    public static void setWorldTime(WorldInfo worldInfo, long time) {
        if (Config.enableDayCycleTweaks)
            worldInfo.worldTime = (long) Math.floor(time / Config.dayLengthMultiplier);
        else
            worldInfo.worldTime = time;

    }

    public static void addTick(WorldInfo worldInfo) {
        worldInfo.worldTime += 1;
    }

    public static void addTick(WorldProvider provider) {
        addTick(provider.worldObj.getWorldInfo());
    }

    public static float calculateCelestialAngle(long time, float off) {
        if (Config.enableDayCycleTweaks) {
            int actualTime = (int) (time % 24000L);
            float timeRatio = ((actualTime + off) / 24000.0F);
            if (timeRatio <= (Config.dayToNightRatio / 2.0f)) {
                timeRatio /= Config.dayToNightRatio;
            } else {
                timeRatio = (timeRatio - (Config.dayToNightRatio / 2.0f))
                        / (2.0f - Config.dayToNightRatio) + 0.5f;
            }
            timeRatio -= 0.25F;
            if (timeRatio < 0.0F) {
                ++timeRatio;
            }
            if (timeRatio > 1.0F) {
                --timeRatio;
            }

            float f2 = timeRatio;
            timeRatio = 1.0F - (float) ((Math.cos(timeRatio * Math.PI) + 1.0D) / 2.0D);
            timeRatio = f2 + (timeRatio - f2) / 3.0F;
            return timeRatio;
        } else {
            int j = (int) (time % 24000L);
            float f1 = (j + off) / 24000.0F - 0.25F;

            if (f1 < 0.0F) {
                ++f1;
            }

            if (f1 > 1.0F) {
                --f1;
            }

            float f2 = f1;
            f1 = 1.0F - (float) ((Math.cos(f1 * Math.PI) + 1.0D) / 2.0D);
            f1 = f2 + (f1 - f2) / 3.0F;
            return f1;
        }
    }
}
