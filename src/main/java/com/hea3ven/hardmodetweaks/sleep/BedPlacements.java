package com.hea3ven.hardmodetweaks.sleep;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import net.minecraftforge.common.util.Constants.NBT;

import static com.hea3ven.hardmodetweaks.sleep.SleepManager.sleepPreventionTimeout;

public class BedPlacements extends WorldSavedData {
	private Map<BlockPos, Long> placements = new HashMap<>();

	public BedPlacements(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList placeNbt = nbt.getTagList("bedPlacements", NBT.TAG_COMPOUND);
		for (int i = 0; i < placeNbt.tagCount(); i++) {
			NBTTagCompound placementNbt = placeNbt.getCompoundTagAt(i);
			placements.put(BlockPos.fromLong(placementNbt.getLong("pos")), placementNbt.getLong("time"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList placeNbt = new NBTTagList();
		for (Entry<BlockPos, Long> entry : placements.entrySet()) {
			NBTTagCompound placementNbt = new NBTTagCompound();
			placementNbt.setLong("pos", entry.getKey().toLong());
			placementNbt.setLong("time", entry.getValue());
			placeNbt.appendTag(placementNbt);
		}
		nbt.setTag("bedPlacements", placeNbt);
		return nbt;
	}

	public void onBedPlaced(World world, BlockPos pos) {
		placements.put(pos, world.getTotalWorldTime());
		markDirty();
	}

	public boolean canSleep(Entity entity, BlockPos pos) {
		Long placement = placements.get(pos);
		if (placement == null)
			return true;
		if ((entity.worldObj.getTotalWorldTime() - placement) > sleepPreventionTimeout) {
			placements.remove(pos);
			return true;
		}
		return false;
	}
}
