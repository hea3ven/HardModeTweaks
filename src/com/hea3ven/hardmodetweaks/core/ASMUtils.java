package com.hea3ven.hardmodetweaks.core;

import java.util.Iterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMUtils {

    public static MethodNode getMethod(ClassNode classNode, String methodName, String methodDesc) {
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext()) {
            MethodNode m = methods.next();
            if ((m.name.equals(methodName) && m.desc.equals(methodDesc)))
                return m;
        }
        return null;
    }

}
