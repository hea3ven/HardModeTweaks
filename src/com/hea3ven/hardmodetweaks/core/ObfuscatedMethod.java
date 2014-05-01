package com.hea3ven.hardmodetweaks.core;

import org.objectweb.asm.tree.MethodInsnNode;

public class ObfuscatedMethod extends Obfuscation {

	private ObfuscatedClass owner;

	public ObfuscatedMethod(ObfuscatedClass owner, String name,
			String obfuscatedName) {
		super(name, obfuscatedName);
		this.owner = owner;
	}

	public boolean matchesNode(MethodInsnNode node, String desc,
			boolean obfuscated) {
		return node.name.equals(get(obfuscated))
				&& node.owner.equals(owner.getPath(obfuscated))
				&& node.desc.equals(desc);
	}

}
