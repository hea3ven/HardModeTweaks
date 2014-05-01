package com.hea3ven.hardmodetweaks.core;

public class ObfuscatedField extends Obfuscation {

	private ObfuscatedClass owner;

	public ObfuscatedField(ObfuscatedClass owner, String name,
			String obfuscatedName) {
		super(name, obfuscatedName);
		this.owner = owner;
	}

	public ObfuscatedClass getOwner() {
		return owner;
	}

}
