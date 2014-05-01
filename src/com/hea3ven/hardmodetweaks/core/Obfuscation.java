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

class Obfuscation {

	private String name;
	private String obfuscatedName;

	public Obfuscation(String name, String obfuscatedName) {
		this.name = name;
		this.obfuscatedName = obfuscatedName;
	}

	public String get(boolean obfuscated) {
		return obfuscated ? obfuscatedName : name;
	}

	public String getName() {
		return name;
	}

	public String getObfuscatedName() {
		return obfuscatedName;
	}

	public boolean isObfuscated(String name) {
		return this.obfuscatedName.equals(name);
	}
}
