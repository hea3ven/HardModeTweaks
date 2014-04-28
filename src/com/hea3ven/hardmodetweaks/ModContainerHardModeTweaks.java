package com.hea3ven.hardmodetweaks;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ModContainerHardModeTweaks extends DummyModContainer {

	public ModContainerHardModeTweaks() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId       = "hardmodetweaks";
        meta.name        = "Hard Mode Tweaks";
        meta.version     = "1.0a1";
        meta.authorList  = Arrays.asList("Hea3veN");
        meta.description = "Gameplay changes";
        meta.url         = "https://github.com/hea3ven/HardModeTweaks";
        meta.updateUrl   = "https://github.com/hea3ven/HardModeTweaks";
        meta.screenshots = new String[0];
        //meta.logoFile    = "/hmt_logo.png";
        
        Set<ArtifactVersion> requirements = Sets.newHashSet();
        List<ArtifactVersion> dependencies = Lists.newArrayList();
        List<ArtifactVersion> dependants = Lists.newArrayList();
        Loader.instance().computeDependencies("required-after:Forge@[10.12.0.1024,)", requirements, dependencies, dependants);
        meta.requiredMods = requirements;
        meta.dependencies = dependencies;
        meta.dependants = dependants;
        
        ModHardModeTweaks.instance = new ModHardModeTweaks();
 	}

	@Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(ModHardModeTweaks.instance);
        return true;
    }

}
