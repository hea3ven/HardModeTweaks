package com.hea3ven.hardmodetweaks.sleep;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SleepManager {

	public static int sleepPreventionTimeout = 3;

	@SubscribeEvent
	public void blockPlaceEvent(BlockEvent.MultiPlaceEvent event) {
		if (event.getPlacedBlock().getBlock() instanceof BlockBed) {
			BedPlacements bedPlacements = getBedPlacements(event.getWorld());
			for (BlockSnapshot snap : event.getReplacedBlockSnapshots()) {
				bedPlacements.onBedPlaced(event.getWorld(), snap.getPos());
			}
		}
	}

	@SubscribeEvent
	public void sleep(PlayerSleepInBedEvent event) {
		BedPlacements bedPlacements = getBedPlacements(event.getEntity().worldObj);
		if (bedPlacements.canSleep(event.getEntity(), event.getPos()))
			return;

		event.getEntityPlayer()
				.addChatComponentMessage(new TextComponentTranslation("tile.bed.recentlyPlaced"));
		event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
	}

	@Nonnull
	private BedPlacements getBedPlacements(World world) {
		BedPlacements bedPlacements =
				(BedPlacements) world.loadItemData(BedPlacements.class, "beds_data");
		if (bedPlacements == null) {
			bedPlacements = new BedPlacements("beds_data");
			world.setItemData("beds_data", bedPlacements);
		}
		return bedPlacements;
	}
}
