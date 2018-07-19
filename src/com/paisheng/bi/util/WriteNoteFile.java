package com.paisheng.bi.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.paisheng.bi.bean.CheckPointBean;
import com.paisheng.bi.constant;

import java.util.List;

public class WriteNoteFile {
    private Project project;
    private Editor editor;
    private PsiClass psiClass;//需要写入的Note类
    private PsiElementFactory psiElementFactory;
    private PsiClass psiClassPoint;//需要添加注解的类
    private PsiMethod psiMethodPoint;//需要添加注解的类的方法
    private String annotationName;//注解类的名称
    private List<CheckPointBean> list;//参数；

    public WriteNoteFile(Project project, Editor editor, PsiClass psiClass, PsiClass psiClassPoint, PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list) {
        this.project = project;
        this.editor = editor;
        this.psiClass = psiClass;
        psiElementFactory = JavaPsiFacade.getElementFactory(project);
        this.psiClassPoint = psiClassPoint;
        this.psiMethodPoint = psiMethodPoint;
        this.annotationName = annotationName;
        this.list = list;
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
    }

    private void start() {
        PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList != null) {
            if (!modifierList.hasModifierProperty(PsiKeyword.PUBLIC)) {
                modifierList.add(psiElementFactory.createKeyword(PsiKeyword.PUBLIC));
//                modifierList.add(psiElementFactory.createAnnotationFromText("@Target({ElementType.METHOD})\n@Retention(RetentionPolicy.CLASS)\n ", null));
            }
        }
    }

    private void sensors(CheckPointBean item) {
        PsiClass psiClassSensors = psiClass.findInnerClassByName("Sensors", true);
        if (psiClassSensors == null) {
            String str = String.format(constant.ASPECT_ANOTE, "Sensors");
            psiClassSensors = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            psiClass.add(psiClassSensors);
        }

        toAddSubA(psiClassSensors);
    }


    private void um(CheckPointBean item) {
        PsiClass psiClassUm = psiClass.findInnerClassByName("Um", true);
        if (psiClassUm == null) {
            String str = String.format(constant.ASPECT_ANOTE, "Um");
            psiClassUm = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            psiClass.add(psiClassUm);
        }

        toAddSubA(psiClassUm);
    }

    private void local(CheckPointBean item) {
        PsiClass psiClassLocal = psiClass.findInnerClassByName("Local", true);
        if (psiClassLocal == null) {
            String str = String.format(constant.ASPECT_ANOTE, "Local");
            psiClassLocal = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            psiClass.add(psiClassLocal);
        }

        toAddSubA(psiClassLocal);
    }

    private void toAddSubA(PsiClass psiClassSensors) {
        PsiClass psiClassSub = psiClassSensors.findInnerClassByName(annotationName, true);
        if (psiClassSub == null) {
            String str = String.format(constant.ASPECT_ANOTE, annotationName);
            psiClassSub = psiElementFactory.createClassFromText(str, null).getInnerClasses()[0];
            psiClassSensors.add(psiClassSub);
        }
    }
}
