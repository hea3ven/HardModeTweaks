package com.hea3ven.hardmodetweaks;

import java.io.IOException;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

public class HardModeTweaksAccessTransformer extends AccessTransformer {

	public HardModeTweaksAccessTransformer() throws IOException {
		super("hardmodetweaks_at.cfg");
	}

}
