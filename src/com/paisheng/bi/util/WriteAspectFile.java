package com.paisheng.bi.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.paisheng.bi.bean.CheckPointBean;
import com.paisheng.bi.constant;

import java.util.List;

public class WriteAspectFile {
    private Project project;
    private Editor editor;
    private PsiClass psiClass;//需要写入的Aspect类
    private PsiElementFactory psiElementFactory;
    private PsiClass psiClassPoint;//需要添加注解的类
    private PsiMethod psiMethodPoint;//需要添加注解的类的方法
    private String annotationName;//注解类的名称
    private List<CheckPointBean> list;//参数；

    public WriteAspectFile(Project project, Editor editor, PsiClass psiClass, PsiClass psiClassPoint, PsiMethod psiMethodPoint, String annotationName, List<CheckPointBean> list) {
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
        initPublicClass();
        for (CheckPointBean item : list) {
            if (item.getPointType() == 1) {
                sensors();
            } else if (item.getPointType() == 2) {
                um();
            } else if (item.getPointType() == 3) {
                local();
            }
        }
        end();
    }

    private void end() {
        JavaCodeStyleManager javaCodeStyleManager = JavaCodeStyleManager.getInstance(project);
        javaCodeStyleManager.optimizeImports(psiClass.getContainingFile());
        javaCodeStyleManager.shortenClassReferences(psiClass);
    }

    private void initPublicClass() {
    }

    private void sensors() {
        PsiClass psiClassSensors = psiClass.findInnerClassByName("Sensors", true);
        if (psiClassSensors == null) {
            psiClassSensors = psiElementFactory.createClassFromText(constant.SENSORS_ASPECT, null).getInnerClasses()[0];
            psiClass.add(psiClassSensors);
        }
    }

    private void um() {
        PsiClass psiClassUm = psiClass.findInnerClassByName("Um", true);
        if (psiClassUm == null) {
            psiClassUm = psiElementFactory.createClassFromText(constant.UM_ASPECT, null).getInnerClasses()[0];
            psiClass.add(psiClassUm);
        }
    }

    private void local() {
        PsiClass psiClassLocal = psiClass.findInnerClassByName("Local", true);
        if (psiClassLocal == null) {
            psiClassLocal = psiElementFactory.createClassFromText(constant.LOCAL_ASPECT, null).getInnerClasses()[0];
            psiClass.add(psiClassLocal);
        }
    }
}
