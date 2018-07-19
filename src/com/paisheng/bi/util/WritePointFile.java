package com.paisheng.bi.util;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.paisheng.bi.bean.CheckPointBean;

import java.util.List;

public class WritePointFile {
    private Project project;
    private PsiClass psiClassNote;//的Note类
    private PsiElementFactory psiElementFactory;
    private PsiClass psiClassPoint;//需要添加注解的类
    private PsiMethod psiMethodPoint;//需要添加注解的类的方法
    private String annotationName;//注解类的名称
    private List<CheckPointBean> list;//参数；

    public WritePointFile(Project project, PsiClass psiClassNote, PsiClass psiClassPoint, PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list) {
        this.project = project;
        this.psiClassNote = psiClassNote;
        psiElementFactory = JavaPsiFacade.getElementFactory(project);
        this.psiClassPoint = psiClassPoint;
        this.psiMethodPoint = psiMethodPoint;
        this.annotationName = annotationName;
        this.list = list;
    }

    public void run() {
        try {
            for (CheckPointBean item : list) {
                if (item.getPointType() == 1) {
                    sensors(item);
                } else if (item.getPointType() == 2) {
                    um(item);
                } else if (item.getPointType() == 3) {
                    local(item);
                }
            }
            openFiles(project, psiClassPoint);
        } catch (Exception e) {
            Messages.showInfoMessage(e.toString(), "错误");
        }
    }

    private void openFiles(Project project, PsiClass... psiClasses) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        for (PsiClass psiClass :
                psiClasses) {
            fileEditorManager.openFile(psiClass.getContainingFile().getVirtualFile(), true, true);
        }
    }

    private void sensors(CheckPointBean item) {
        PsiModifierList psiModifierList = psiMethodPoint.getModifierList();
        psiModifierList.addBefore(psiElementFactory.createAnnotationFromText("@" + psiClassNote.getName() + ".Sensors." + annotationName, psiMethodPoint), psiModifierList.getFirstChild());
    }


    private void um(CheckPointBean item) {
        PsiModifierList psiModifierList = psiMethodPoint.getModifierList();
        psiModifierList.addBefore(psiElementFactory.createAnnotationFromText("@" + psiClassNote.getName() + ".Um." + annotationName, psiMethodPoint), psiModifierList.getFirstChild());
    }

    private void local(CheckPointBean item) {
        PsiModifierList psiModifierList = psiMethodPoint.getModifierList();
        psiModifierList.addBefore(psiElementFactory.createAnnotationFromText("@" + psiClassNote.getName() + ".Local." + annotationName, psiMethodPoint), psiModifierList.getFirstChild());
    }
}
