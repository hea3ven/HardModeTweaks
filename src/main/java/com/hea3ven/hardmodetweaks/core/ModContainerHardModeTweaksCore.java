package com.hea3ven.hardmodetweaks.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class ModContainerHardModeTweaksCore extends DummyModContainer {

	private static Logger logger = LogManager.getLogger("HardModeTweaks.ModContainerHardModeTweaksCore");

	public ModContainerHardModeTweaksCore() {
		super(new ModMetadata());

		logger.debug("configuring the mod");

		ModMetadata meta = getMetadata();
		meta.modId = "hardmodetweakscore";
		meta.name = "Hard Mode Tweaks Core";
		meta.version = "1.0.0";
		meta.authorList = Lists.newArrayList("Hea3veN");
		meta.description = "Core mod for Hard Mode Tweaks";
		meta.url = "https://github.com/hea3ven/HardModeTweaks";
		meta.screenshots = new String[0];
		// meta.logoFile = "/hmt_logo.png";
		meta.parent = "hardmodetweaks";

		meta.requiredMods = Sets.newHashSet();
		meta.dependencies = Lists.newArrayList();
		meta.dependants = Lists.newArrayList();
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}
