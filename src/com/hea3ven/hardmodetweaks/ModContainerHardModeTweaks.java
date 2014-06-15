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

package com.hea3ven.hardmodetweaks;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.hea3ven.hardmodetweaks.config.ProjectConfig;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ModContainerHardModeTweaks extends DummyModContainer {

    private Logger logger = LogManager
            .getLogger("HardModeTweaks.ModContainerHardModeTweaks");

    public ModContainerHardModeTweaks() {
        super(new ModMetadata());

        logger.debug("configuring the mod");

        ModMetadata meta = getMetadata();
        meta.modId = "hardmodetweaks";
        meta.name = "Hard Mode Tweaks";
        meta.version = ProjectConfig.version;
        meta.authorList = Arrays.asList("Hea3veN");
        meta.description = "Gameplay changes";
        meta.url = "https://github.com/hea3ven/HardModeTweaks";
        meta.updateUrl = "https://github.com/hea3ven/HardModeTweaks";
        meta.screenshots = new String[0];
        // meta.logoFile = "/hmt_logo.png";

        Set<ArtifactVersion> requirements = Sets.newHashSet();
        List<ArtifactVersion> dependencies = Lists.newArrayList();
        List<ArtifactVersion> dependants = Lists.newArrayList();

        if (ProjectConfig.forge_version != null) {
            Loader.instance().computeDependencies(
                    "required-after:Forge@[" + ProjectConfig.forge_version
                            + ",)", requirements, dependencies, dependants);
            meta.requiredMods = requirements;
            meta.dependencies = dependencies;
            meta.dependants = dependants;
        }

        ModHardModeTweaks.instance = new ModHardModeTweaks();
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(ModHardModeTweaks.instance);
        ModHardModeTweaks.instance.registerBus(bus);
        return true;
    }

}
