package com.hea3ven.hardmodetweaks;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;

import com.hea3ven.hardmodetweaks.config.Config;

public class SleepManager {

	public static class BlockPlacement {

		public long worldTime;
		public int x;
		public int y;
		public int z;

		public BlockPlacement(long worldTime, int x, int y, int z) {
			this.worldTime = worldTime;
			this.x = x;
			this.y = y;
			this.z = z;
		}

	}

	private static SleepManager instance = null;

	public static void onConfigChanged() {
		if (Config.enableSleepPrevention) {
			if (instance == null) {
				instance = new SleepManager();
				MinecraftForge.EVENT_BUS.register(instance);
			}
		} else {
			if (instance != null) {
				MinecraftForge.EVENT_BUS.unregister(instance);
				instance = null;
			}
		}
	}

	private Set<BlockPlacement> bedPlacements = new HashSet<BlockPlacement>();

	@SubscribeEvent
	public void blockPlaceEvent(BlockEvent.MultiPlaceEvent event) {
		if (event.block instanceof BlockBed) {
			for (BlockSnapshot snap : event.getReplacedBlockSnapshots()) {
				BlockPlacement placement = getPlacement(snap.x, snap.y, snap.z);
				if (placement != null)
					placement.worldTime = event.world.getTotalWorldTime();
				else
					bedPlacements.add(new BlockPlacement(event.world.getTotalWorldTime(), snap.x,
							snap.y, snap.z));
			}
		}
	}

	@SubscribeEvent
	public void sleep(PlayerSleepInBedEvent event) {
		BlockPlacement bedPlacement = getPlacement(event.x, event.y, event.z);
		if (bedPlacement == null)
			return;
		if (event.entity.worldObj.getTotalWorldTime()
				- bedPlacement.worldTime < Config.sleepPreventionTimeout) {
			event.entityPlayer.addChatComponentMessage(
					new ChatComponentTranslation("tile.bed.recentlyPlaced", new Object[0]));
			event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
		} else {
			bedPlacements.remove(bedPlacement);
		}
	}

	private BlockPlacement getPlacement(int x, int y, int z) {
		for (BlockPlacement bedPlacement : bedPlacements) {
			if (x == bedPlacement.x && y == bedPlacement.y && z == bedPlacement.z) {
				return bedPlacement;
			}
		}
		return null;
	}
}
