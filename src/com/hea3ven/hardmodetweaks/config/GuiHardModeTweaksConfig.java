package com.hea3ven.hardmodetweaks.config;

import net.minecraft.client.gui.GuiScreen;

import cpw.mods.fml.client.config.GuiConfig;

public class GuiHardModeTweaksConfig extends GuiConfig {
    public GuiHardModeTweaksConfig(GuiScreen parent) {
        super(parent, Config.getElements(), "hardmodetweaks|main", "hardmodetweaksconfig", false, false,
                "Hard Mode Tweaks Config");
    }

}
