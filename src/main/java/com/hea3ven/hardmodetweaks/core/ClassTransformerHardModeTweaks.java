package com.hea3ven.hardmodetweaks.core;

import net.minecraft.launchwrapper.IClassTransformer;

import com.hea3ven.tweaks.Hea3venTweaks;

public class ClassTransformerHardModeTweaks implements IClassTransformer {

	private Hea3venTweaks tweaks = new Hea3venTweaks();

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		return tweaks.transform(name, transformedName, basicClass);
	}
}
