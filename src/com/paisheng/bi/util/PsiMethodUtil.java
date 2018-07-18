package com.paisheng.bi.util;

import com.intellij.lang.jvm.JvmParameter;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;

public class PsiMethodUtil {
    public static String getFriendMethodName(PsiMethod psiMethod) {
        StringBuilder name = new StringBuilder();
        name.append(formatReturnType(psiMethod.getReturnType()));
        name.append(" ");
        name.append(psiMethod.getName());
        name.append("(");
        JvmParameter[] parameters = psiMethod.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            JvmParameter itemParameter = parameters[i];
            if (i != 0) {
                name.append(", ");
            }
            name.append(formatParameter(itemParameter));
        }
        name.append(")");
        return name.toString();
    }

    private static String formatReturnType(PsiType psiType) {
        String className = psiType.getCanonicalText();
        int lastIndex = className.lastIndexOf(".");
        if (lastIndex != -1) {
            className = className.substring(lastIndex + 1);
        }
        return className;
    }

    private static String formatParameter(JvmParameter jvmParameter) {
        String value = jvmParameter.getType().toString();
        return value.split(":")[1] + " " + jvmParameter.getName();
    }
}
