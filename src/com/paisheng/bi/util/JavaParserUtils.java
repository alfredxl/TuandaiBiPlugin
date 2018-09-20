package com.paisheng.bi.util;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.paisheng.bi.bean.BiMethodDeclaration;

import java.util.Map;

public class JavaParserUtils {
    public static void parse(String code, final Map<String, BiMethodDeclaration> map) {
        CompilationUnit compilationUnit = JavaParser.parse(code);
        VoidVisitorAdapter<Object> adapter = new VoidVisitorAdapter<Object>() {

            @Override
            public void visit(MethodDeclaration n, Object arg) {
                super.visit(n, arg);
                if (n.getAnnotations() != null) {
                    for (AnnotationExpr annotationExpr : n.getAnnotations()) {
                        if (annotationExpr.getName().asString().matches(PropertiesComponentUtil.getBiConfigData())) {
                            BiMethodDeclaration biMethodDeclaration = new BiMethodDeclaration();
                            biMethodDeclaration.setMethodDeclaration(n);
                            map.put(getName(map, n.getName().asString(), 0), biMethodDeclaration);
                            break;
                        }
                    }
                }
            }
        };
        adapter.visit(compilationUnit, null);
    }

    private static String getName(Map<String, BiMethodDeclaration> map, String name, int type) {
        if (map.containsKey(name + type)) {
            return getName(map, name, type + 1);
        } else {
            return name + type;
        }
    }
}
