package com.hea3ven.hardmodetweaks.core;

class ObfuscatedClass extends Obfuscation {
	public ObfuscatedClass(String name, String obfuscatedName) {
		super(name, obfuscatedName);
	}

	public boolean matchesName(String name) {
		return name.equals(getName()) || name.equals(getObfuscatedName());
	}

	public String getPath(boolean obfuscated) {
		return get(obfuscated).replace('.', '/');
	}

}