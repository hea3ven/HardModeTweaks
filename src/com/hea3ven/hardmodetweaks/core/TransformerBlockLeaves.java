package com.hea3ven.hardmodetweaks.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TransformerBlockLeaves {
    private static final ObfuscatedClass BLOCK_LEAVES_CLASS = new ObfuscatedClass(
            "net.minecraft.block.BlockLeaves", "alt");
    private static final ObfuscatedMethod GET_COLLISION_BB = new ObfuscatedMethod(
            BLOCK_LEAVES_CLASS, "getCollisionBoundingBoxFromPool", "a");
    private static final ObfuscatedClass AXIS_ALIGNED_BB_CLASS = new ObfuscatedClass(
            "net.minecraft.util.AxisAlignedBB", "azt");

    public static boolean matches(String name) {
        return BLOCK_LEAVES_CLASS.matchesName(name);
    }

    public static ClassNode transform(String name, ClassNode klass) {
        boolean obfuscated = BLOCK_LEAVES_CLASS.isObfuscated(name);

        MethodNode getCollisionBBMethod = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC,
                GET_COLLISION_BB.get(obfuscated), "(L"
                        + ClassTransformerHardModeTweaks.WORLD.getPath(obfuscated) + ";III)L"
                        + AXIS_ALIGNED_BB_CLASS.getPath(obfuscated) + ";", null, null);
        getCollisionBBMethod.instructions.add(new InsnNode(Opcodes.ACONST_NULL));
        getCollisionBBMethod.instructions.add(new InsnNode(Opcodes.ARETURN));
        klass.methods.add(getCollisionBBMethod);

        return klass;
    }

}
