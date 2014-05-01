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
