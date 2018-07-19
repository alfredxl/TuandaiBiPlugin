package com.paisheng.bi.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.paisheng.bi.bean.CheckPointBean;

import java.io.IOException;
import java.util.List;

public class WriteToFile {
    public static void write(final AnActionEvent e, final PsiClass psiClass, final PsiMethod selectMethod, final String className, final List<CheckPointBean> list) {
        final Project project = e.getProject();
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            public void run() {
                // 创建文件系统
                PsiClass[] virtualFiles = createFile(e);
                // 写文件
                if (virtualFiles != null) {
                    toWrite(project, virtualFiles, psiClass, selectMethod, className, list);
                }
            }
        });
    }

    private static void toWrite(final Project project, final PsiClass[] psiClassesList, final PsiClass psiClassPoint, final PsiMethod psiMethodPoint, final String annotationName, final List<CheckPointBean> list) {
        writeAspect(project, psiClassesList[0], psiClassPoint, psiMethodPoint, annotationName, list, psiClassesList[1].getName());
        writeNote(project, psiClassesList[1], psiClassPoint, psiMethodPoint, annotationName, list);
        writePoint(project, psiClassesList[1], psiClassPoint, psiMethodPoint, annotationName, list);
    }

    private static void writeAspect(Project project, PsiClass psiClass, PsiClass psiClassPoint,
                                    PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list, String noteName) {
        new WriteAspectFile(project, psiClass, psiClassPoint, psiMethodPoint, annotationName, list, noteName).run();
    }

    private static void writeNote(Project project, PsiClass psiClass, PsiClass psiClassPoint, PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list) {
        new WriteNoteFile(project, psiClass, psiClassPoint, psiMethodPoint, annotationName, list).run();
    }

    private static void writePoint(Project project, PsiClass psiClass, PsiClass psiClassPoint, PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list) {
        new WritePointFile(project, psiClass, psiClassPoint, psiMethodPoint, annotationName, list).run();
    }

    private static PsiClass[] createFile(AnActionEvent e) {
        Project project = e.getProject();
        Module mModule = e.getData(LangDataKeys.MODULE);
        if (project != null && mModule != null) {
            String ModuleName = mModule.getName();
            String src = ModuleName + "/src/bi/java/bi";
            try {
                // 获取virtualFileDirectory
                VirtualFile virtualFileDirectory = ProjectHelper.createFolderIfNotExist(project, src);
                // 获取biPsiDirectory
                PsiDirectory biPsiDirectory = PsiManager.getInstance(project).findDirectory(virtualFileDirectory);
                if (biPsiDirectory != null) {
                    // 查找该目录下是否有Aspect和Note文件
                    PsiFile[] psiFiles = getAspectAndNote(biPsiDirectory);
                    // 获取要定义的类名
                    String[] classNames = getFormatName(ModuleName.replaceAll("[Bb]iz_", ""));
                    // 定义PsiClass数组
                    PsiClass[] PsiClassArray = new PsiClass[2];
                    // 查找PsiFile
                    PsiFile psiFile1 = psiFiles[0];
                    if (psiFile1 instanceof PsiJavaFile) {
                        // 赋值
                        PsiClassArray[0] = ((PsiJavaFile) psiFile1).getClasses()[0];
                    } else {
                        // 创建
                        PsiClassArray[0] = JavaDirectoryService.getInstance().createClass(biPsiDirectory, classNames[0].replace(".java", ""));
//                        VirtualFile virtualFile = virtualFileDirectory.findOrCreateChildData(project, classNames[0]);
//                        ProjectHelper.setFileContent(project, virtualFile, String.format(constant.ASPECT, classNames[0].replace(".java", "")));
//                        PsiClassArray[0] = ((PsiJavaFile) PsiManager.getInstance(project).findFile(virtualFile)).getClasses()[0];
                    }
                    //  查找PsiFile
                    PsiFile psiFile2 = psiFiles[1];
                    if (psiFile2 instanceof PsiJavaFile) {
                        // 赋值
                        PsiClassArray[1] = ((PsiJavaFile) psiFile2).getClasses()[0];
                    } else {
                        // 创建
                        PsiClassArray[1] = JavaDirectoryService.getInstance().createAnnotationType(biPsiDirectory, classNames[1].replace(".java", ""));
//                        VirtualFile virtualFile = virtualFileDirectory.findOrCreateChildData(project, classNames[1]);
//                        ProjectHelper.setFileContent(project, virtualFile, String.format(constant.NOTE, classNames[1].replace(".java", "")));
//                        PsiClassArray[1] = ((PsiJavaFile) PsiManager.getInstance(project).findFile(virtualFile)).getClasses()[0];
                    }
                    // 加入编辑器
                    return PsiClassArray;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    private static PsiFile[] getAspectAndNote(PsiDirectory biPsiDirectory) {
        PsiFile[] values = new PsiFile[2];
        for (PsiFile item : biPsiDirectory.getFiles()) {
            String name = item.getName();
            if (name.startsWith("Bi") && name.endsWith("Aspect.java")) {
                values[0] = item;
                continue;
            }
            if (name.startsWith("Bi") && name.endsWith("Note.java")) {
                values[1] = item;
            }
        }
        return values;
    }

    private static String[] getFormatName(String className) {
        String temp = formatNamed(className);
        return new String[]{"Bi" + temp + "Aspect.java", "Bi" + temp + "Note.java"};
    }

    private static String formatNamed(String name) {
        char[] ch = name.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
