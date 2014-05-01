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

	private static final ObfuscatedClass WORLD_SERVER_CLASS = new ObfuscatedClass(
			"net.minecraft.world.WorldServer", "mj");
	private static final ObfuscatedMethod WORLD_SERVER_TICK = new ObfuscatedMethod(
			WORLD_SERVER_CLASS, "tick", "b");
	private static final ObfuscatedClass WORLD_CLIENT = new ObfuscatedClass(
			"net.minecraft.client.multiplayer.WorldClient", "biz");
	private static final ObfuscatedMethod WORLD_CLIENT_TICK = new ObfuscatedMethod(
			WORLD_CLIENT, "tick", "b");
	private static final ObfuscatedMethod WORLD_CLIENT_GET_WORLD_TIME = new ObfuscatedMethod(
			WORLD_CLIENT, "getWorldTime", "I");
	private static final ObfuscatedMethod WORLD_CLIENT_SET_WORLD_TIME = new ObfuscatedMethod(
			WORLD_CLIENT, "setWorldTime", "b");
	private static final ObfuscatedClass WORLD_INFO = new ObfuscatedClass(
			"net.minecraft.world.storage.WorldInfo", "axe");
	private static final ObfuscatedMethod WORLD_INFO_GET_WORLD_TIME = new ObfuscatedMethod(
			WORLD_INFO, "getWorldTime", "g");
	private static final ObfuscatedMethod WORLD_INFO_SET_WORLD_TIME = new ObfuscatedMethod(
			WORLD_INFO, "setWorldTime", "c");
	private static final ObfuscatedField WORLD_INFO_WORLD_TIME = new ObfuscatedField(
			WORLD_INFO, "worldTime", "h");
	private static final ObfuscatedClass WORLD = new ObfuscatedClass(
			"net.minecraft.world.World", "afn");
	private static final ObfuscatedMethod WORLD_GET_WORLD_INFO_MTHD = new ObfuscatedMethod(
			WORLD, "getWorldInfo", "M");
	private static final ObfuscatedField WORLD_PROVIDER = new ObfuscatedField(
			WORLD, "provider", "t");
	private static final ObfuscatedClass WORLD_PROVIDER_CLASS = new ObfuscatedClass(
			"net.minecraft.world.WorldProvider", "apa");
	private static final ObfuscatedField WORLD_PROVIDER_WORLD_OBJ_FLD = new ObfuscatedField(
			WORLD_PROVIDER_CLASS, "worldObj", "b");

	private Logger logger = LogManager
			.getLogger("HardModeTweaks.ClassTransformer");

	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		if (WORLD_SERVER_CLASS.matchesName(name)) {
			logger.info("Class WorldServer({}/{}) is loading, patching it",
					name, transformedName);
			return patchWorldServer(name, basicClass,
					WORLD_SERVER_CLASS.isObfuscated(name));
		}

		if (WORLD_CLIENT.matchesName(name)) {
			logger.info("Class WorldClient({}/{}) is loading, patching it",
					name, transformedName);
			return patchWorldClient(name, basicClass,
					WORLD_CLIENT.isObfuscated(name));
		}

		if (WORLD_INFO.matchesName(name)) {
			logger.info("Class WorldInfo({}/{}) is loading, patching it", name,
					transformedName);
			return patchWorldInfo(name, basicClass,
					WORLD_INFO.isObfuscated(name));
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
				WORLD_SERVER_TICK.get(obfuscated), "()V");
		if (tickMethod == null)
			error("Could not find the method");
		logger.info("Patching tick({}) method of WorldServer", tickMethod.name);
		patchWorldServerTick(tickMethod, obfuscated);
		logger.info("Finished patching tick method of WorldServer");

		return writeClass(classNode);
	}

	private void patchWorldServerTick(MethodNode method, boolean obfuscated) {
		Iterator<AbstractInsnNode> iter = method.instructions.iterator();

		// Replace
		// > getWorldTime()
		// To
		// > getRealWorldTime()
		while (iter.hasNext()) {
			AbstractInsnNode currentNode = iter.next();

			if (currentNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode methodInsnNode = (MethodInsnNode) currentNode;
				if (WORLD_INFO_GET_WORLD_TIME.matchesNode(methodInsnNode,
						"()J", obfuscated)) {
					methodInsnNode.name = "getRealWorldTime";
				} else if (WORLD_INFO_SET_WORLD_TIME.matchesNode(
						methodInsnNode, "(J)V", obfuscated)) {
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
				WORLD_CLIENT_TICK.get(obfuscated), "()V");
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
				if (WORLD_CLIENT_GET_WORLD_TIME.matchesNode(methodInsnNode,
						"()J", obfuscated)) {
					methodInsnNode.name = "getRealWorldTime";
					methodInsnNode.owner = WORLD_INFO.getPath(obfuscated);

					method.instructions.insertBefore(
							currentNode.getPrevious(),
							new FieldInsnNode(Opcodes.GETFIELD, WORLD
									.getPath(obfuscated), WORLD_PROVIDER
									.get(obfuscated), "L"
									+ WORLD_PROVIDER_CLASS.getPath(obfuscated)
									+ ";"));
					method.instructions.insertBefore(
							currentNode.getPrevious(),
							new FieldInsnNode(Opcodes.GETFIELD,
									WORLD_PROVIDER_CLASS.getPath(obfuscated),
									WORLD_PROVIDER_WORLD_OBJ_FLD
											.get(obfuscated), "L"
											+ WORLD.getPath(obfuscated) + ";"));
					method.instructions.insertBefore(
							currentNode.getPrevious(),
							new MethodInsnNode(Opcodes.INVOKEVIRTUAL, WORLD
									.getPath(obfuscated),
									WORLD_GET_WORLD_INFO_MTHD.get(obfuscated),
									"()L" + WORLD_INFO.getPath(obfuscated)
											+ ";"));

					method.instructions.insertBefore(
							currentNode,
							new FieldInsnNode(Opcodes.GETFIELD, WORLD
									.getPath(obfuscated), WORLD_PROVIDER
									.get(obfuscated), "L"
									+ WORLD_PROVIDER_CLASS.getPath(obfuscated)
									+ ";"));
					method.instructions.insertBefore(
							currentNode,
							new FieldInsnNode(Opcodes.GETFIELD,
									WORLD_PROVIDER_CLASS.getPath(obfuscated),
									WORLD_PROVIDER_WORLD_OBJ_FLD
											.get(obfuscated), "L"
											+ WORLD.getPath(obfuscated) + ";"));
					method.instructions.insertBefore(
							currentNode,
							new MethodInsnNode(Opcodes.INVOKEVIRTUAL, WORLD
									.getPath(obfuscated),
									WORLD_GET_WORLD_INFO_MTHD.get(obfuscated),
									"()L" + WORLD_INFO.getPath(obfuscated)
											+ ";"));
				} else if (WORLD_CLIENT_SET_WORLD_TIME.matchesNode(
						methodInsnNode, "(J)V", obfuscated)) {
					methodInsnNode.name = "setRealWorldTime";
					methodInsnNode.owner = WORLD_INFO.getPath(obfuscated);

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
				WORLD_INFO_GET_WORLD_TIME.get(obfuscated), "()J");
		if (getWorldTimeMethod == null)
			error("Could not find the method");
		String originalGetWorldTimeName = getWorldTimeMethod.name;
		getWorldTimeMethod.name = "getRealWorldTime";
		logger.info("Finished renaming getWorldTime method of WorldInfo");

		logger.info("Renaming setWorldTime method of WorldInfo to setRealWorldTime");
		MethodNode setWorldTimeMethod = getMethod(classNode,
				WORLD_INFO_SET_WORLD_TIME.get(obfuscated), "(J)V");
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
				WORLD_INFO.getPath(obfuscated), WORLD_INFO_WORLD_TIME
						.get(obfuscated), "J"));
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
				WORLD_INFO.getPath(obfuscated), WORLD_INFO_WORLD_TIME
						.get(obfuscated), "J"));
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
