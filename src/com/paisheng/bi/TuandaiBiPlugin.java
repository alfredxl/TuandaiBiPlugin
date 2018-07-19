package com.paisheng.bi;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import com.paisheng.bi.bean.CheckPointBean;
import com.paisheng.bi.dialog.CheckMethodDialog;
import com.paisheng.bi.dialog.CheckPointType;
import com.paisheng.bi.util.PsiMethodUtil;
import com.paisheng.bi.util.WriteToFile;

import java.util.List;


public class TuandaiBiPlugin extends AnAction {
    private boolean isWriting = false;

    @Override
    public void actionPerformed(final AnActionEvent e) {
        isWriting = false;
        Project project = e.getProject();
        if (project != null) {
            Editor editor = e.getData(PlatformDataKeys.EDITOR);
            if (editor != null) {
                String mSelectedText = editor.getSelectionModel().getSelectedText();
                if (mSelectedText == null || mSelectedText.length() == 0) {
                    Messages.showInfoMessage("请选中需要切入Bi的方法名！", "提示");
                } else {
                    PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                    final PsiClass psiClass = EditUtils.getPsiClass(editor, psiFile);
                    final PsiMethod[] psiMethods = psiClass.findMethodsByName(mSelectedText, false);
                    if (psiMethods.length == 0) {
                        Messages.showInfoMessage("Bi切入点必须为方法，请选中方法名！", "提示");
                    } else if (psiMethods.length > 1) {
                        String[] values = new String[psiMethods.length];
                        for (int i = 0; i < psiMethods.length; i++) {
                            values[i] = PsiMethodUtil.getFriendMethodName(psiMethods[i]);
                        }
                        CheckMethodDialog.show(values, new CheckMethodDialog.CheckListener() {
                            public void checked(int position, String value) {
                                final PsiMethod selectMethod = psiMethods[position];
                                CheckPointType.show(psiClass, selectMethod, new CheckPointType.CheckPointListener() {
                                    public void checked(String className, List<CheckPointBean> list) {
                                        if (!isWriting) {
                                            isWriting = true;
                                            WriteToFile.write(e, psiClass, selectMethod, className, list);
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        CheckPointType.show(psiClass, psiMethods[0], new CheckPointType.CheckPointListener() {
                            public void checked(String className, List<CheckPointBean> list) {
                                if (!isWriting) {
                                    isWriting = true;
                                    WriteToFile.write(e, psiClass, psiMethods[0], className, list);
                                }
                            }
                        });
                    }
                }
            }
        }
    }
//    private PsiDirectory getSrcDirectory(PsiDirectory psiDirectory) {
//        if (!psiDirectory.getName().equals("src")) {
//            return getSrcDirectory(psiDirectory.getParentDirectory());
//        } else {
//            return psiDirectory;
//        }
//    }
//
//    public List<String> getSourceRootPathList(Project project, AnActionEvent event) {
//        List<String> sourceRoots = Lists.newArrayList();
//        String projectPath = StringUtils.normalizePath(project.getBasePath());
//        for (VirtualFile virtualFile : getModuleRootManager(event).getSourceRoots(false)) {
//            sourceRoots.add(StringUtils.normalizePath(virtualFile.getPath()).replace(projectPath, ""));
//        }
//        return sourceRoots;
//    }
//
//    private ModuleRootManager getModuleRootManager(AnActionEvent event) {
//        return ModuleRootManager.getInstance(event.getData(LangDataKeys.MODULE));
//    }
}
