package com.paisheng.bi.util;


import com.paisheng.bi.bean.BiMethodDeclaration;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.*;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.StringReader;
import java.util.Map;

public class JavaParserUtils {
    public static void test(String code, final Map<String, BiMethodDeclaration> map) {
        try {
            CompilationUnit compilationUnit = JavaParser.parse(new StringReader(code));
            VoidVisitorAdapter<Object> adapter = new VoidVisitorAdapter<Object>() {

                @Override
                public void visit(MethodDeclaration n, Object arg) {
                    super.visit(n, arg);
                    if (n.getAnnotations() != null) {
                        for (AnnotationExpr annotationExpr : n.getAnnotations()) {
                            if (annotationExpr.getName().toString().matches(BiMethodDeclaration.NAME_REGEX)) {
                                BiMethodDeclaration biMethodDeclaration = new BiMethodDeclaration();
                                biMethodDeclaration.setMethodDeclaration(n);
                                map.put(getName(map, n.getName(), 0), biMethodDeclaration);
                                break;
                            }
                        }
                    }
                }
            };
            adapter.visit(compilationUnit, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String getName(Map<String, BiMethodDeclaration> map, String name, int type) {
        if (map.containsKey(name + type)) {
            return getName(map, name, type + 1);
        } else {
            return name + type;
        }
    }
}
