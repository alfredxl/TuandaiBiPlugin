package com.paisheng.bi.util;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.paisheng.bi.bean.CheckPointBean;
import com.paisheng.bi.constant;

import java.util.List;

public class WriteNoteFile {
    private Project project;
    private PsiClass psiClass;//需要写入的Note类
    private PsiElementFactory psiElementFactory;
    private PsiClass psiClassPoint;//需要添加注解的类
    private PsiMethod psiMethodPoint;//需要添加注解的类的方法
    private String annotationName;//注解类的名称
    private List<CheckPointBean> list;//参数；
    private String classDescription; //注解类的描述

    public WriteNoteFile(Project project, PsiClass psiClass, PsiClass psiClassPoint, PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list,
                         String classDescription) {
        this.project = project;
        this.psiClass = psiClass;
        psiElementFactory = JavaPsiFacade.getElementFactory(project);
        this.psiClassPoint = psiClassPoint;
        this.psiMethodPoint = psiMethodPoint;
        this.annotationName = annotationName;
        this.list = list;
        this.classDescription = classDescription;
    }

    public void run() {
        start();
        for (CheckPointBean item : list) {
            if (item.getPointType() == 1) {
                sensors(item);
            } else if (item.getPointType() == 2) {
                um(item);
            } else if (item.getPointType() == 3) {
                local(item);
            }
        }
        openFiles(project, psiClass);
    }

    private void openFiles(Project project, PsiClass... psiClasses) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        for (PsiClass psiClass :
                psiClasses) {
            fileEditorManager.openFile(psiClass.getContainingFile().getVirtualFile(), true, true);
        }
    }

    private void start() {
        PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList != null) {
            if (!modifierList.hasModifierProperty(PsiKeyword.PUBLIC)) {
                modifierList.add(psiElementFactory.createKeyword(PsiKeyword.PUBLIC));
                modifierList.addBefore(psiElementFactory.createAnnotationFromText("@Retention(RetentionPolicy.CLASS)", null), modifierList.getFirstChild());
                modifierList.addBefore(psiElementFactory.createAnnotationFromText("@Target({ElementType.METHOD})", null), modifierList.getFirstChild());
            }
        }
    }

    private void sensors(CheckPointBean item) {
        PsiClass psiClassSensors = psiClass.findInnerClassByName("Sensors", true);
        boolean isWillBeAdd = false;
        if (psiClassSensors == null) {
            String str = String.format(constant.ASPECT_ANOTE, "神测注解类", "Sensors");
            psiClassSensors = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            isWillBeAdd = true;
        }

        toAddSubA(psiClassSensors, isWillBeAdd);
    }


    private void um(CheckPointBean item) {
        PsiClass psiClassUm = psiClass.findInnerClassByName("Um", true);
        boolean isWillBeAdd = false;
        if (psiClassUm == null) {
            String str = String.format(constant.ASPECT_ANOTE, "友盟注解类", "Um");
            psiClassUm = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            isWillBeAdd = true;
        }

        toAddSubA(psiClassUm, isWillBeAdd);
    }

    private void local(CheckPointBean item) {
        PsiClass psiClassLocal = psiClass.findInnerClassByName("Local", true);
        boolean isWillBeAdd = false;
        if (psiClassLocal == null) {
            String str = String.format(constant.ASPECT_ANOTE, "本地注解类", "Local");
            psiClassLocal = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            isWillBeAdd = true;
        }

        toAddSubA(psiClassLocal, isWillBeAdd);
    }

    private void toAddSubA(PsiClass psiClassParent, boolean isWillBeAdd) {
        PsiClass psiClassSub = psiClassParent.findInnerClassByName(annotationName, true);
        if (psiClassSub == null) {
            String str = String.format(constant.ASPECT_ANOTE, classDescription, annotationName);
            psiClassSub = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            psiClassParent.add(psiClassSub);
        } else {
            throw new RuntimeException(psiClassParent.getName() + "类中，注解类" + annotationName + "已存在");
        }
        if (isWillBeAdd) {
            psiClass.add(psiClassParent);
        }
    }
}
