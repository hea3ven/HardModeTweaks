package com.hea3ven.hardmodetweaks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.hardmodetweaks.config.Config;
import com.hea3ven.hardmodetweaks.entity.ai.EntityAIPanicAway;

public class AITweaksManager {

    private static AITweaksManager instance = null;

    public static void onConfigChanged() {
        if (Config.enableTweakAnimalAI) {
            if (instance == null) {
                instance = new AITweaksManager();
                MinecraftForge.EVENT_BUS.register(instance);
            }
        } else {
            if (instance != null) {
                MinecraftForge.EVENT_BUS.unregister(instance);
                instance = null;
            }
        }
    }

	@SubscribeEvent
	public void specialSpawnEvent(EntityJoinWorldEvent e) {
		if (Config.enableTweakAnimalAI && e.entity instanceof EntityLiving) {
			replaceAIPanicTask((EntityLiving) e.entity);
		}
	}

	private void replaceAIPanicTask(EntityLiving entity) {
		EntityAITasks.EntityAITaskEntry panicTask = null;
		for (Object taskEntryObj : entity.tasks.taskEntries) {
			EntityAITasks.EntityAITaskEntry taskEntry = (EntityAITasks.EntityAITaskEntry) taskEntryObj;
			if (taskEntry.action instanceof EntityAIPanic) {
				panicTask = taskEntry;
			}
		}
		if (panicTask != null) {
			entity.tasks.removeTask(panicTask.action);
			entity.tasks.addTask(panicTask.priority, new EntityAIPanicAway(
					(EntityCreature) entity, 2.5f));
		}
	}
}
