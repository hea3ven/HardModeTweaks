package com.hea3ven.hardmodetweaks.sleep;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SleepManager {

	public static int sleepPreventionTimeout = 3;

	public static class BlockPlacement {

		public long worldTime;
		public BlockPos pos;

		public BlockPlacement(long worldTime, BlockPos pos) {
			this.worldTime = worldTime;
			this.pos = pos;
		}
	}

	private Set<BlockPlacement> bedPlacements = new HashSet<>();

	@SubscribeEvent
	public void blockPlaceEvent(BlockEvent.MultiPlaceEvent event) {
		if (event.placedBlock.getBlock() instanceof BlockBed) {
			for (BlockSnapshot snap : event.getReplacedBlockSnapshots()) {
				BlockPlacement placement = getPlacement(snap.pos);
				if (placement != null)
					placement.worldTime = event.world.getTotalWorldTime();
				else
					bedPlacements.add(new BlockPlacement(event.world.getTotalWorldTime(), snap.pos));
			}
		}
	}

	@SubscribeEvent
	public void sleep(PlayerSleepInBedEvent event) {
		BlockPlacement bedPlacement = getPlacement(event.pos);
		if (bedPlacement == null)
			return;
		if (event.entity.worldObj.getTotalWorldTime() - bedPlacement.worldTime < sleepPreventionTimeout) {
			event.entityPlayer.addChatComponentMessage(
					new TextComponentTranslation("tile.bed.recentlyPlaced"));
			event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
		} else {
			bedPlacements.remove(bedPlacement);
		}
	}

	private BlockPlacement getPlacement(BlockPos pos) {
		for (BlockPlacement bedPlacement : bedPlacements) {
			if (pos.equals(bedPlacement.pos))
				return bedPlacement;
		}
		return null;
	}
}
