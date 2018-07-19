package com.paisheng.bi.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.paisheng.bi.bean.CheckPointBean;
import com.paisheng.bi.constant;

import java.util.List;

public class WritePointFile {
    private Project project;
    private Editor editor;
    private PsiClass psiClassNote;//的Note类
    private PsiElementFactory psiElementFactory;
    private PsiClass psiClassPoint;//需要添加注解的类
    private PsiMethod psiMethodPoint;//需要添加注解的类的方法
    private String annotationName;//注解类的名称
    private List<CheckPointBean> list;//参数；

    public WritePointFile(Project project, Editor editor, PsiClass psiClassNote, PsiClass psiClassPoint, PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list) {
        this.project = project;
        this.editor = editor;
        this.psiClassNote = psiClassNote;
        psiElementFactory = JavaPsiFacade.getElementFactory(project);
        this.psiClassPoint = psiClassPoint;
        this.psiMethodPoint = psiMethodPoint;
        this.annotationName = annotationName;
        this.list = list;
    }

    public void run() {
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

    private void sensors(CheckPointBean item) {
        psiMethodPoint.getModifierList().add(psiElementFactory.createAnnotationFromText("@" + psiClassNote.getName() + "$Sensors$" + annotationName, psiMethodPoint));
    }


    private void um(CheckPointBean item) {
        psiMethodPoint.getModifierList().add(psiElementFactory.createAnnotationFromText("@" + psiClassNote.getName() + "$Um$" + annotationName, psiMethodPoint));
    }

    private void local(CheckPointBean item) {
        psiMethodPoint.getModifierList().add(psiElementFactory.createAnnotationFromText("@" + psiClassNote.getName() + "$Local$" + annotationName, psiMethodPoint));
    }
}
