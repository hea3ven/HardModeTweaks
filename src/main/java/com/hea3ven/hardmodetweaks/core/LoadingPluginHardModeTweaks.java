package com.hea3ven.hardmodetweaks.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class LoadingPluginHardModeTweaks implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"com.hea3ven.hardmodetweaks.core.ClassTransformerHardModeTweaks"};
	}

	@Override
	public String getModContainerClass() {
		return "com.hea3ven.hardmodetweaks.core.ModContainerHardModeTweaksCore";
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
