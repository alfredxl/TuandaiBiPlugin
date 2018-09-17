package com.paisheng.bi.commit;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.ui.NonFocusableCheckBox;
import com.intellij.util.PairConsumer;
import com.paisheng.bi.bean.BiClassMethod;
import com.paisheng.bi.bean.BiMethodDeclaration;
import com.paisheng.bi.util.JavaParserUtils;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.AnnotationExpr;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

class PaiSCodeAnalysisHandler extends CheckinHandler {
    private final JCheckBox checkBox = new NonFocusableCheckBox("Tuandai Bi Code Check");
    private CheckinProjectPanel checkinProjectPanel;
    private CommitContext commitContext;
    private List<BiClassMethod> allBiList = new ArrayList<BiClassMethod>();
    private List<BiClassMethod> problemBiList = new ArrayList<BiClassMethod>();


    PaiSCodeAnalysisHandler(CheckinProjectPanel checkinProjectPanel, CommitContext commitContext) {
        this.checkinProjectPanel = checkinProjectPanel;
        this.commitContext = commitContext;
    }

    @Override
    public RefreshableOnComponent getBeforeCheckinConfigurationPanel() {
        return new RefreshableOnComponent() {

            public void refresh() {
            }

            public void saveState() {
            }

            public void restoreState() {
                checkBox.setSelected(true);
            }

            public JComponent getComponent() {
                return checkBox;
            }
        };
    }

    @Override
    public ReturnResult beforeCheckin(@Nullable CommitExecutor executor, PairConsumer<Object, Object> additionalDataConsumer) {
        if (checkBox.isSelected()) {
            allBiList.clear();
            problemBiList.clear();
            for (Change item : checkinProjectPanel.getSelectedChanges()) {
                // 只对旧有代码有的做检测
                ContentRevision revisionBefore = item.getBeforeRevision();
                if (revisionBefore != null && revisionBefore.getFile().getName().endsWith(".java")) {
                    BiClassMethod biClassMethod = new BiClassMethod();
                    ContentRevision revisionAfter = item.getAfterRevision();
                    biClassMethod.setHasDelete(revisionAfter == null);
                    try {
                        if (revisionAfter != null) {
                            biClassMethod.setVirtualFilePath(revisionAfter.getFile().getPath());
                            biClassMethod.setAfterContent(revisionAfter.getContent());
                        }
                        biClassMethod.setBeforeContent(revisionBefore.getContent());
                    } catch (VcsException e) {
                        e.printStackTrace();
                    }
                    JavaParserUtils.test(biClassMethod.getBeforeContent(), biClassMethod.getBeforeMap());
                    if (biClassMethod.getBeforeMap().size() > 0) {
                        allBiList.add(biClassMethod);
                        if (biClassMethod.getAfterContent() != null) {
                            JavaParserUtils.test(biClassMethod.getAfterContent(), biClassMethod.getAfterMap());
                        }
                    }
                }
            }
            collectProblem();
            if (problemBiList.size() > 0) {
                StringBuilder stringBuffer = new StringBuilder();
                stringBuffer.append("有修改的Bi注解方法:").append("\r\n");
                for (BiClassMethod biItem : problemBiList) {
                    for (Map.Entry<String, BiMethodDeclaration> entry : biItem.getBeforeMap().entrySet()) {
                        BiMethodDeclaration mBiItem = entry.getValue();
                        if (mBiItem.isChange()) {
                            stringBuffer.append(mBiItem.getMethodDeclaration().getName()).append("\r\n");
                        }
                    }
                }
                Messages.showInfoMessage(stringBuffer.toString(), "提示");
                return CheckinHandler.ReturnResult.CANCEL;
            } else {
                return CheckinHandler.ReturnResult.COMMIT;
            }
        } else {
            return super.beforeCheckin(executor, additionalDataConsumer);
        }
    }

    private void collectProblem() {
        for (BiClassMethod biClassMethod : allBiList) {
            // 文件被删除。
            if (biClassMethod.isHasDelete()) {
                for (Map.Entry<String, BiMethodDeclaration> entry : biClassMethod.getBeforeMap().entrySet()) {
                    // 设置旧有每个Bi方法都被改变
                    entry.getValue().setChange(true);
                }
                // 旧有类文件Bi方法被改变
                problemBiList.add(biClassMethod);
            } else {
                // 旧文件Bi方法集合
                Map<String, BiMethodDeclaration> beforeMap = biClassMethod.getBeforeMap();
                // 新文件Bi方法集合
                Map<String, BiMethodDeclaration> afterMap = biClassMethod.getAfterMap();
                // 初始旧有类的Bi方法是否有改变
                boolean biClassMethodIsChange = false;
                // 遍历旧有方法
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
                    problemBiList.add(biClassMethod);
                }
            }
        }
    }

    private boolean checkMethodHasChange(MethodDeclaration biMethodBefore, MethodDeclaration biMethodAfter) {
        // 检测注解
        List<AnnotationExpr> annotationsBefore = biMethodBefore.getAnnotations();
        List<AnnotationExpr> annotationsAfter = biMethodAfter.getAnnotations();
        // 定义有相同的方法注解
        boolean isSampleMethod = true;
        if (annotationsBefore != null) {
            for (AnnotationExpr beforeItem : annotationsBefore) {
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
            // 注解相同，检测参数
            if (isSampleMethod) {
                List<Parameter> parametersBefore = biMethodBefore.getParameters();
                List<Parameter> parametersAfter = biMethodAfter.getParameters();
                if (parametersBefore != null) {
                    if (parametersAfter != null) {
                        if (parametersBefore.size() == parametersAfter.size()) {
                            // 参数数量相同
                            for (int i = 0; i < parametersBefore.size(); i++) {
                                if (!parametersBefore.get(i).getType().toString().equals(
                                        parametersAfter.get(i).getType().toString())) {
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
