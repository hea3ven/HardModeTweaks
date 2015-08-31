package com.hea3ven.hardmodetweaks;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.hardmodetweaks.config.Config;

public class SleepManager {

	public static class BlockPlacement {

		private long worldTime;
		private BlockPos pos;

		public BlockPlacement(long worldTime, BlockPos pos) {
			this.worldTime = worldTime;
			this.pos = pos;
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
		if (event.placedBlock instanceof BlockBed) {
			for (BlockSnapshot snap : event.getReplacedBlockSnapshots()) {
				BlockPlacement placement = getPlacement(snap.pos);
				if (placement != null)
					placement.worldTime = event.world.getTotalWorldTime();
				else
					bedPlacements
							.add(new BlockPlacement(event.world.getTotalWorldTime(), snap.pos));
			}
		}
	}

	@SubscribeEvent
	public void sleep(PlayerSleepInBedEvent event) {
		BlockPlacement bedPlacement = getPlacement(event.pos);
		if (event.entity.worldObj.getTotalWorldTime()
				- bedPlacement.worldTime < Config.sleepPreventionTimeout) {
			event.entityPlayer.addChatComponentMessage(
					new ChatComponentTranslation("tile.bed.recentlyPlaced", new Object[0]));
			event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
			bedPlacements.remove(bedPlacement);
		}
	}

	private BlockPlacement getPlacement(BlockPos pos) {
		for (BlockPlacement bedPlacement : bedPlacements) {
			if (pos.equals(bedPlacement.pos)) {
				return bedPlacement;
			}
		}
		return null;
	}
}
