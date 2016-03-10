package com.hea3ven.hardmodetweaks;

import net.minecraft.client.gui.GuiScreen;

import com.hea3ven.tools.commonutils.config.ModGuiFactoryAbstract;
import com.hea3ven.tools.commonutils.mod.config.GuiConfigAutomatic;

public class ModGuiFactoryHardModeTweaks extends ModGuiFactoryAbstract {

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiConfigHardModeTweaks.class;
	}

	public static class GuiConfigHardModeTweaks extends GuiConfigAutomatic {
		public GuiConfigHardModeTweaks(GuiScreen parentScreen) {
			super(parentScreen, ModHardModeTweaks.proxy);
		}
	}
}
