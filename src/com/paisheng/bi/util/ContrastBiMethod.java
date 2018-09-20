package com.paisheng.bi.util;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.paisheng.bi.bean.BiClassMethod;
import com.paisheng.bi.bean.BiMethodDeclaration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ContrastBiMethod {

    public static final String FILE_EXTENSION = ".java";

    /*** 收集有改动的类 ***/
    public static List<BiClassMethod> collectMethod(Collection<Change> changes) {
        List<BiClassMethod> problemBiList = new ArrayList<>();
        for (Change item : changes) {
            // 只对旧有代码有的做检测
            ContentRevision revisionBefore = item.getBeforeRevision();
            if (revisionBefore != null && revisionBefore.getFile().getName().endsWith(FILE_EXTENSION)) {
                BiClassMethod biClassMethod = new BiClassMethod();
                ContentRevision revisionAfter = item.getAfterRevision();
                biClassMethod.setHasDelete(revisionAfter == null);
                try {
                    if (revisionAfter != null) {
                        biClassMethod.setAfterVirtualFilePath(revisionAfter.getFile().getPath());
                        biClassMethod.setAfterContent(revisionAfter.getContent());
                    }
                    biClassMethod.setBeforeVirtualFilePath(revisionBefore.getFile().getPath());
                    biClassMethod.setBeforeContent(revisionBefore.getContent());
                } catch (VcsException e) {
                    e.printStackTrace();
                }
                JavaParserUtils.parse(biClassMethod.getBeforeContent(), biClassMethod.getBeforeMap());
                // 旧有代码有Bi注解方法
                if (biClassMethod.getBeforeMap().size() > 0) {
                    if (biClassMethod.getAfterContent() != null) {
                        // 新代码进行Bi方法统计
                        JavaParserUtils.parse(biClassMethod.getAfterContent(), biClassMethod.getAfterMap());
                    }
                    // 对比
                    BiClassMethod contrasCM = ContrastBiMethod.contras(biClassMethod);
                    if (contrasCM != null) {
                        problemBiList.add(contrasCM);
                    }
                }
            }
        }
        return problemBiList;
    }


    /*** 检测Bi方法是否有改动，有改动则返回处理好的BiClassMethod, 否则返回null ***/
    private static BiClassMethod contras(BiClassMethod biClassMethod) {
        // 文件被删除。
        if (biClassMethod.isHasDelete()) {
            for (Map.Entry<String, BiMethodDeclaration> entry : biClassMethod.getBeforeMap().entrySet()) {
                // 设置旧有每个Bi方法都被改变
                entry.getValue().setChange(true);
            }
            // 旧有类文件Bi方法被改变
            return biClassMethod;
        } else {
            // 旧文件Bi方法集合
            Map<String, BiMethodDeclaration> beforeMap = biClassMethod.getBeforeMap();
            // 新文件Bi方法集合
            Map<String, BiMethodDeclaration> afterMap = biClassMethod.getAfterMap();
            // 初始旧有类的Bi方法是否有改变
            boolean biClassMethodIsChange = false;
            // 遍历旧有方法(并处理改变标识)
            for (Map.Entry<String, BiMethodDeclaration> entryBefore : beforeMap.entrySet()) {
                // 定义单个方法是否有被改变
                boolean singleMethodIsChange;
                // 旧的Bi方法
                BiMethodDeclaration biMethodBefore = entryBefore.getValue();
                // 新的Bi方法含有旧的Bi方法
                if (afterMap.containsKey(entryBefore.getKey())) {
                    // 新的Bi方法
                    BiMethodDeclaration biMethodAfter = afterMap.get(entryBefore.getKey());
                    // 检测方法是否被改变
                    singleMethodIsChange = checkMethodHasChange(biMethodBefore.getMethodDeclaration(), biMethodAfter.getMethodDeclaration());
                    biMethodAfter.setChange(singleMethodIsChange);
                } else {
                    // 方法被删除
                    singleMethodIsChange = true;
                }
                if (singleMethodIsChange) {
                    // Bi方法被改变
                    biMethodBefore.setChange(true);
                    // 整体类有Bi方法被改变
                    biClassMethodIsChange = true;
                }
            }
            if (biClassMethodIsChange) {
                // 旧有类文件Bi方法被改变
                return biClassMethod;
            } else {
                return null;
            }
        }
    }

    private static boolean checkMethodHasChange(MethodDeclaration biMethodBefore, MethodDeclaration biMethodAfter) {
        // 检测注解
        List<AnnotationExpr> annotationsBefore = biMethodBefore.getAnnotations();
        List<AnnotationExpr> annotationsAfter = biMethodAfter.getAnnotations();
        // 定义有相同的方法注解
        boolean isSampleMethod = true;
        if (annotationsBefore != null) {
            for (AnnotationExpr beforeItem : annotationsBefore) {
                // 只对Bi注解进行检测
                if (beforeItem.getName().asString().matches(BiMethodDeclaration.NAME_REGEX)) {
                    // 定义单个注解
                    boolean singleHasSampleAnnotation = false;
                    if (annotationsAfter != null) {
                        for (AnnotationExpr afterItem : annotationsAfter) {
                            if (beforeItem.getName().equals(afterItem.getName())) {
                                singleHasSampleAnnotation = true;
                                break;
                            }
                        }
                    }
                    if (!singleHasSampleAnnotation) {
                        isSampleMethod = false;
                        break;
                    }
                }
            }
            // 注解相同，检测参数
            if (isSampleMethod) {
                List<Parameter> parametersBefore = biMethodBefore.getParameters();
                List<Parameter> parametersAfter = biMethodAfter.getParameters();
                if (parametersBefore != null) {
                    if (parametersAfter != null) {
                        if (parametersBefore.size() == parametersAfter.size()) {
                            // 参数数量相同
                            for (int i = 0; i < parametersBefore.size(); i++) {
                                if (!parametersBefore.get(i).getType().asString().equals(
                                        parametersAfter.get(i).getType().asString())) {
                                    // 参数类型不同
                                    isSampleMethod = false;
                                    break;
                                }
                            }
                        } else {
                            // 修改前后方法参数数量不一致
                            isSampleMethod = false;
                        }
                    } else {
                        // 修改后的方法没有参数
                        isSampleMethod = false;
                    }
                }
            }
        }
        return !isSampleMethod;
    }
}
