package com.hea3ven.hardmodetweaks.core;

import java.util.Map;

import com.hea3ven.hardmodetweaks.ModContainerHardModeTweaks;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class LoadingPluginHardModeTweaks implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ClassTransformerHardModeTweaks.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return ModContainerHardModeTweaks.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
