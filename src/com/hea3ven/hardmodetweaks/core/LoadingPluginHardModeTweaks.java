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

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import com.hea3ven.hardmodetweaks.HardModeTweaksAccessTransformer;

public class LoadingPluginHardModeTweaks implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ClassTransformerHardModeTweaks.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return ModContainerHardModeTweaksCore.class.getName();
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
		return HardModeTweaksAccessTransformer.class.getName();
	}

}
