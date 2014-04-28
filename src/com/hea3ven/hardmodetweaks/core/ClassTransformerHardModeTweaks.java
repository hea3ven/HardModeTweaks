package com.hea3ven.hardmodetweaks.core;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassTransformerHardModeTweaks implements IClassTransformer {

	private static final String WORLD_SERVER_CLASS = "net.minecraft.world.WorldServer";
	private static final String WORLD_SERVER_OBF_CLASS = "mj";
	private static final String WORLD_SERVER_TICK_METHOD = "tick";
	private static final String WORLD_SERVER_TICK_OBF_METHOD = "b";

	private static final String WORLD_CLIENT_CLASS = "net.minecraft.client.multiplayer.WorldClient";
	private static final String WORLD_CLIENT_OBF_CLASS = "biz";
	private static final String WORLD_CLIENT_TICK_METHOD = "tick";
	private static final String WORLD_CLIENT_TICK_OBF_METHOD = "b";

	private static final String WORLD_INFO_CLASS = "net.minecraft.world.storage.WorldInfo";
	private static final String WORLD_INFO_OBF_CLASS = "axe";
	private static final String WORLD_INFO_WORLD_TIME_FIELD = "worldTime";
	private static final String WORLD_INFO_WORLD_TIME_OBF_FIELD = "field_76094_f";
	private static final String WORLD_INFO_GET_WORLD_TIME_METHOD = "getWorldTime";
	private static final String WORLD_INFO_GET_WORLD_TIME_OBF_METHOD = "g";
	private static final String WORLD_INFO_SET_WORLD_TIME_METHOD = "setWorldTime";
	private static final String WORLD_INFO_SET_WORLD_TIME_OBF_METHOD = "c";

	private Logger logger = LogManager
			.getLogger("HardModeTweaks.ClassTransformer");

	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		if (name.equals(WORLD_SERVER_CLASS)
				|| name.equals(WORLD_SERVER_OBF_CLASS)) {
			logger.info("Class WorldServer({}/{}) is loading, patching it",
					name, transformedName);
			return patchWorldServer(name, basicClass,
					name.equals(WORLD_SERVER_OBF_CLASS));
		}

		if (name.equals(WORLD_CLIENT_CLASS)
				|| name.equals(WORLD_CLIENT_OBF_CLASS)) {
			logger.info("Class WorldClient({}/{}) is loading, patching it",
					name, transformedName);
			return patchWorldClient(name, basicClass,
					name.equals(WORLD_CLIENT_OBF_CLASS));
		}

		if (name.equals(WORLD_INFO_CLASS) || name.equals(WORLD_INFO_OBF_CLASS)) {
			logger.info("Class WorldInfo({}/{}) is loading, patching it", name,
					transformedName);
			return patchWorldInfo(name, basicClass,
					name.equals(WORLD_INFO_OBF_CLASS));
		}

		return basicClass;
	}

	//
	// ******************** WorldServer ********************
	//
	private byte[] patchWorldServer(String name, byte[] basicClass,
			boolean obfuscated) {
		ClassNode classNode = readClass(basicClass);

		logger.info("Looking for tick method of WorldServer");
		MethodNode tickMethod = getMethod(classNode,
				obfuscated ? WORLD_SERVER_TICK_OBF_METHOD
						: WORLD_SERVER_TICK_METHOD, "()V");
		if (tickMethod == null)
			error("Could not find the method");
		logger.info("Patching tick({}) method of WorldServer", tickMethod.name);
		patchWorldServerTick(tickMethod);
		logger.info("Finished patching tick method of WorldServer");

		return writeClass(classNode);
	}

	private void patchWorldServerTick(MethodNode method) {
		Iterator<AbstractInsnNode> iter = method.instructions.iterator();

		// Replace
		// > getWorldTime()
		// To
		// > getRealWorldTime()
		while (iter.hasNext()) {
			AbstractInsnNode currentNode = iter.next();

			if (currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode methodInsnNode = (MethodInsnNode) currentNode;
				if (methodInsnNode.name.equals("getWorldTime")) {
					methodInsnNode.name = "getRealWorldTime";
				} else if (methodInsnNode.name.equals("setWorldTime")) {
					methodInsnNode.name = "setRealWorldTime";
				}
			}
		}
	}

	//
	// ******************** WorldClient ********************
	//
	private byte[] patchWorldClient(String name, byte[] basicClass,
			boolean obfuscated) {
		ClassNode classNode = readClass(basicClass);

		logger.info("Looking for tick method of WorldClient");
		MethodNode tickMethod = getMethod(classNode,
				obfuscated ? WORLD_CLIENT_TICK_OBF_METHOD
						: WORLD_CLIENT_TICK_METHOD, "()V");
		if (tickMethod == null)
			error("Could not find the method");
		logger.info("Patching tick({}) method of WorldClient", tickMethod.name);
		patchWorldClientTick(tickMethod, obfuscated);
		logger.info("Finished patching tick method of WorldClient");

		return writeClass(classNode);
	}

	private void patchWorldClientTick(MethodNode method, boolean obfuscated) {
		Iterator<AbstractInsnNode> iter = method.instructions.iterator();

		// Replace
		// > this.setWorldTime(this.getWorldTime() + 1L);
		// To
		// > this.provider.worldObj.getWorldInfo().setWorldTime(
		// >         this.provider.worldObj.getWorldInfo().getRealWorldTime() + 1L);
		while (iter.hasNext()) {
			AbstractInsnNode currentNode = iter.next();

			if (currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode methodInsnNode = (MethodInsnNode) currentNode;
				if (methodInsnNode.name.equals("getWorldTime")) {
					methodInsnNode.name = "getRealWorldTime";
					methodInsnNode.owner = "net/minecraft/world/storage/WorldInfo";

					method.instructions.insertBefore(currentNode.getPrevious(),
							new FieldInsnNode(Opcodes.GETFIELD,
									"net/minecraft/world/World", "provider",
									"Lnet/minecraft/world/WorldProvider;"));
					method.instructions.insertBefore(currentNode.getPrevious(),
							new FieldInsnNode(Opcodes.GETFIELD,
									"net/minecraft/world/WorldProvider",
									"worldObj", "Lnet/minecraft/world/World;"));
					method.instructions
							.insertBefore(
									currentNode.getPrevious(),
									new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
											"net/minecraft/world/World",
											"getWorldInfo",
											"()Lnet/minecraft/world/storage/WorldInfo;"));

					method.instructions.insertBefore(currentNode,
							new FieldInsnNode(Opcodes.GETFIELD,
									"net/minecraft/world/World", "provider",
									"Lnet/minecraft/world/WorldProvider;"));
					method.instructions.insertBefore(currentNode,
							new FieldInsnNode(Opcodes.GETFIELD,
									"net/minecraft/world/WorldProvider",
									"worldObj", "Lnet/minecraft/world/World;"));
					method.instructions
							.insertBefore(
									currentNode,
									new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
											"net/minecraft/world/World",
											"getWorldInfo",
											"()Lnet/minecraft/world/storage/WorldInfo;"));
				} else if (methodInsnNode.name.equals("setWorldTime")) {
					methodInsnNode.name = "setRealWorldTime";
					methodInsnNode.owner = "net/minecraft/world/storage/WorldInfo";

				}
			}
		}
	}

	//
	// ******************** WorldInfo ********************
	//
	private byte[] patchWorldInfo(String name, byte[] basicClass,
			boolean obfuscated) {
		ClassNode classNode = readClass(basicClass);

		logger.info("Renaming getWorldTime method of WorldInfo to getRealWorldTime");
		MethodNode getWorldTimeMethod = getMethod(classNode,
				obfuscated ? WORLD_INFO_GET_WORLD_TIME_OBF_METHOD
						: WORLD_INFO_GET_WORLD_TIME_METHOD, "()J");
		if (getWorldTimeMethod == null)
			error("Could not find the method");
		String originalGetWorldTimeName = getWorldTimeMethod.name;
		getWorldTimeMethod.name = "getRealWorldTime";
		logger.info("Finished renaming getWorldTime method of WorldInfo");

		logger.info("Renaming setWorldTime method of WorldInfo to setRealWorldTime");
		MethodNode setWorldTimeMethod = getMethod(classNode,
				obfuscated ? WORLD_INFO_SET_WORLD_TIME_OBF_METHOD
						: WORLD_INFO_SET_WORLD_TIME_METHOD, "(J)V");
		if (setWorldTimeMethod == null)
			error("Could not find the method");
		String originalSetWorldTimeName = setWorldTimeMethod.name;
		setWorldTimeMethod.name = "setRealWorldTime";
		logger.info("Finished renaming setWorldTime method of WorldInfo");

		logger.info(
				"Creating new implementation of getWorldTime({}) method of WorldInfo",
				originalGetWorldTimeName);
		classNode.methods.add(createNewGetWorldTimeMethod(
				originalGetWorldTimeName, obfuscated));
		logger.info("Finished adding getWorldTime method to WorldInfo");

		logger.info(
				"Creating new implementation of setWorldTime({}) method of WorldInfo",
				originalSetWorldTimeName);
		classNode.methods.add(createNewSetWorldTimeMethod(
				originalSetWorldTimeName, obfuscated));
		logger.info("Finished adding setWorldTime method to WorldInfo");

		return writeClass(classNode);
	}

	private MethodNode createNewGetWorldTimeMethod(String methodName,
			boolean obfuscated) {
		// > long getWorldTime() {
		// >     return (long)Math.floor(worldTime * ModHardModeTweaks.dayLengthMultiplier);
		// > }
		MethodNode getWorldTimeMethod = new MethodNode(Opcodes.ASM4,
				Opcodes.ACC_PUBLIC, methodName, "()J", null, null);
		getWorldTimeMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
		getWorldTimeMethod.instructions.add(new FieldInsnNode(Opcodes.GETFIELD,
				"net/minecraft/world/storage/WorldInfo",
				obfuscated ? WORLD_INFO_WORLD_TIME_OBF_FIELD
						: WORLD_INFO_WORLD_TIME_FIELD, "J"));
		getWorldTimeMethod.instructions.add(new InsnNode(Opcodes.L2D));
		getWorldTimeMethod.instructions.add(new FieldInsnNode(
				Opcodes.GETSTATIC,
				"com/hea3ven/hardmodetweaks/ModHardModeTweaks",
				"dayLengthMultiplier", "D"));
		getWorldTimeMethod.instructions.add(new InsnNode(Opcodes.DMUL));
		getWorldTimeMethod.instructions.add(new MethodInsnNode(
				Opcodes.INVOKESTATIC, "java/lang/Math", "floor", "(D)D"));
		getWorldTimeMethod.instructions.add(new InsnNode(Opcodes.D2L));
		getWorldTimeMethod.instructions.add(new InsnNode(Opcodes.LRETURN));
		return getWorldTimeMethod;
	}

	private MethodNode createNewSetWorldTimeMethod(String methodName,
			boolean obfuscated) {
		// > void setWorldTime(long time) {
		// >     worldTime = (long)Math.floor(time / ModHardModeTweaks.dayLengthMultiplier);
		// > }
		MethodNode setWorldTimeMethod = new MethodNode(Opcodes.ASM4,
				Opcodes.ACC_PUBLIC, methodName, "(J)V", null, null);
		setWorldTimeMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
		setWorldTimeMethod.instructions.add(new VarInsnNode(Opcodes.LLOAD, 1));
		setWorldTimeMethod.instructions.add(new InsnNode(Opcodes.L2D));
		setWorldTimeMethod.instructions.add(new FieldInsnNode(
				Opcodes.GETSTATIC,
				"com/hea3ven/hardmodetweaks/ModHardModeTweaks",
				"dayLengthMultiplier", "D"));
		setWorldTimeMethod.instructions.add(new InsnNode(Opcodes.DDIV));
		setWorldTimeMethod.instructions.add(new MethodInsnNode(
				Opcodes.INVOKESTATIC, "java/lang/Math", "floor", "(D)D"));
		setWorldTimeMethod.instructions.add(new InsnNode(Opcodes.D2L));
		setWorldTimeMethod.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD,
				"net/minecraft/world/storage/WorldInfo",
				obfuscated ? WORLD_INFO_WORLD_TIME_OBF_FIELD
						: WORLD_INFO_WORLD_TIME_FIELD, "J"));
		setWorldTimeMethod.instructions.add(new InsnNode(Opcodes.RETURN));
		return setWorldTimeMethod;
	}

	//
	// ******************** Utilities ********************
	//
	private ClassNode readClass(byte[] basicClass) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		return classNode;
	}

	private byte[] writeClass(ClassNode classNode) {
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	private MethodNode getMethod(ClassNode classNode, String methodName,
			String methodDesc) {
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while (methods.hasNext()) {
			MethodNode m = methods.next();
			if ((m.name.equals(methodName) && m.desc.equals(methodDesc)))
				return m;
		}
		return null;
	}

	private void error(String msg) {
		logger.error(msg);
		throw new RuntimeException(String.format("Failed to patch class: %s",
				msg));
	}

}
