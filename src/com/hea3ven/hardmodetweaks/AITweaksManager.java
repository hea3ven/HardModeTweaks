package com.hea3ven.hardmodetweaks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.hea3ven.hardmodetweaks.entity.ai.EntityAIPanicAway;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AITweaksManager {
	public boolean tweakPanicAI = true;

	@SubscribeEvent
	public void specialSpawnEvent(EntityJoinWorldEvent e) {
		if (tweakPanicAI && e.entity instanceof EntityLiving) {
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
