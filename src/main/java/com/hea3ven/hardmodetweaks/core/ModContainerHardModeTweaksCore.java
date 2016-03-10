/**
 * 
 * Copyright (c) 2014 Hea3veN
 * 
 *  This file is part of HardModeTweaks.
 *
 *  HardModeTweaks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  HardModeTweaks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with HardModeTweaks.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.hea3ven.hardmodetweaks.core;

import java.util.Arrays;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class ModContainerHardModeTweaksCore extends DummyModContainer {

    private Logger logger = LogManager.getLogger("HardModeTweaks.ModContainerHardModeTweaksCore");

    public ModContainerHardModeTweaksCore() {
        super(new ModMetadata());

        logger.debug("configuring the mod");

        ModMetadata meta = getMetadata();
        meta.modId = "hardmodetweakscore";
        meta.name = "Hard Mode Tweaks Core";
        meta.version = "1.0.0";
        meta.authorList = Arrays.asList("Hea3veN");
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
