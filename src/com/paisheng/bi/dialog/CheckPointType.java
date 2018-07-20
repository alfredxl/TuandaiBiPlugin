package com.paisheng.bi.dialog;

import com.intellij.lang.jvm.JvmParameter;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBUI;
import com.paisheng.bi.bean.CheckPointBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CheckPointType {

    public static void main(String[] args) {
        show(null, null, null);
    }

    public static void show(PsiClass psiClass, PsiMethod psiMethod, final CheckPointListener listener) {
        final JFrame jFrame = new JFrame("请选择要统计的方式");
        JPanel jPanelMain = new JPanel(new VerticalLayout(10));
        jPanelMain.setBorder(JBUI.Borders.empty(10));
        jFrame.setContentPane(jPanelMain);

        JPanel jPanelClassBox = new JPanel(new VerticalLayout(5));
        jPanelClassBox.setBorder(BorderFactory.createEtchedBorder());
        JPanel jPanelClassName = new JPanel(new FlowLayout(5));
        jPanelClassName.add(new JLabel("注解类名称"));
        final JTextField textFieldClassNme = new JTextField(getClassName(psiClass, psiMethod), 30);
        jPanelClassName.add(textFieldClassNme);
        jPanelClassBox.add(jPanelClassName);

        JPanel jPanelClassDescription = new JPanel(new FlowLayout(5));
        jPanelClassDescription.add(new JLabel("注解类描述"));
        final JTextField textFieldClassDescription = new JTextField(30);
        jPanelClassDescription.add(textFieldClassDescription);
        jPanelClassBox.add(jPanelClassDescription);
        jPanelMain.add(jPanelClassBox);

        JPanel jPanelPointType = new JPanel(new HorizontalLayout(5));
        JPanel jPanelSensors = new JPanel(new VerticalLayout(5));
        jPanelSensors.setBorder(BorderFactory.createEtchedBorder());
        JPanel jPanelUm = new JPanel(new VerticalLayout(5));
        jPanelUm.setBorder(BorderFactory.createEtchedBorder());
        JPanel jPanelLocal = new JPanel(new VerticalLayout(5));
        jPanelLocal.setBorder(BorderFactory.createEtchedBorder());
        jPanelPointType.add(jPanelSensors);
        jPanelPointType.add(jPanelUm);
        jPanelPointType.add(jPanelLocal);
        jPanelMain.add(jPanelPointType);

        List<String> parameters = getMethodParameter(psiMethod);

        final JCheckBox jCheckBoxSensors = new JCheckBox("神测", true);
        jPanelSensors.add(jCheckBoxSensors);
        // 选择框集合
        final List<JCheckBox> ListCheckBoxesSensors = new ArrayList<JCheckBox>();
        JPanel jPanelParameterRootSensors = new JPanel(new VerticalLayout(5));
        jPanelParameterRootSensors.setBorder(BorderFactory.createTitledBorder("参数"));
        jPanelSensors.add(jPanelParameterRootSensors);
        // 是私有类
        boolean isHasPrivate = psiClass.getModifierList() != null && psiClass.getModifierList().hasModifierProperty(PsiKeyword.PRIVATE);
        // 是内部类
        boolean isHasNotName = psiClass.getName() == null || psiClass.getName().length() == 0;
        // 不能有this参数选项
        boolean isNotThis = isHasPrivate || isHasNotName;
        if (isNotThis) {
            ListCheckBoxesSensors.add(new JCheckBox("", false));
        } else {
            jPanelParameterRootSensors.add(new JCheckBox(psiClass.getName() + " this", true));
            ListCheckBoxesSensors.add((JCheckBox) jPanelParameterRootSensors.getComponent(0));
        }

        final JCheckBox jCheckBoxUm = new JCheckBox("友盟");
        jPanelUm.add(jCheckBoxUm);
        final List<JCheckBox> ListCheckBoxesUm = new ArrayList<JCheckBox>();
        JPanel jPanelParameterRootUm = new JPanel(new VerticalLayout(5));
        jPanelParameterRootUm.setBorder(BorderFactory.createTitledBorder("参数"));
        jPanelUm.add(jPanelParameterRootUm);
        if (isNotThis) {
            ListCheckBoxesUm.add(new JCheckBox("", false));
        } else {
            jPanelParameterRootUm.add(new JCheckBox(psiClass.getName() + " this", true));
            ListCheckBoxesUm.add((JCheckBox) jPanelParameterRootUm.getComponent(0));
        }

        final JCheckBox jCheckBoxLocal = new JCheckBox("本地");
        jPanelLocal.add(jCheckBoxLocal);
        final List<JCheckBox> ListCheckBoxesLocal = new ArrayList<JCheckBox>();
        JPanel jPanelParameterRootLocal = new JPanel(new VerticalLayout(5));
        jPanelParameterRootLocal.setBorder(BorderFactory.createTitledBorder("参数"));
        jPanelLocal.add(jPanelParameterRootLocal);
        if (isNotThis) {
            ListCheckBoxesLocal.add(new JCheckBox("", false));
        } else {
            jPanelParameterRootLocal.add(new JCheckBox(psiClass.getName() + " this", true));
            ListCheckBoxesLocal.add((JCheckBox) jPanelParameterRootLocal.getComponent(0));
        }

        for (int i = 0; i < parameters.size(); i++) {
            String parameter = parameters.get(i);
            JPanel jPanelParameterSensors = new JPanel(new HorizontalLayout(0));
            jPanelParameterSensors.add(new JCheckBox("NotNull", true));
            jPanelParameterSensors.add(new JCheckBox(parameter, true));
            jPanelParameterRootSensors.add(jPanelParameterSensors);
            ListCheckBoxesSensors.add((JCheckBox) jPanelParameterSensors.getComponent(0));
            ListCheckBoxesSensors.add((JCheckBox) jPanelParameterSensors.getComponent(1));

            JPanel jPanelParameterUm = new JPanel(new HorizontalLayout(0));
            jPanelParameterUm.add(new JCheckBox("NotNull", true));
            jPanelParameterUm.add(new JCheckBox(parameter, true));
            jPanelParameterRootUm.add(jPanelParameterUm);
            ListCheckBoxesUm.add((JCheckBox) jPanelParameterUm.getComponent(0));
            ListCheckBoxesUm.add((JCheckBox) jPanelParameterUm.getComponent(1));

            JPanel jPanelParameterLocal = new JPanel(new HorizontalLayout(0));
            jPanelParameterLocal.add(new JCheckBox("NotNull", true));
            jPanelParameterLocal.add(new JCheckBox(parameter, true));
            jPanelParameterRootLocal.add(jPanelParameterLocal);
            ListCheckBoxesLocal.add((JCheckBox) jPanelParameterLocal.getComponent(0));
            ListCheckBoxesLocal.add((JCheckBox) jPanelParameterLocal.getComponent(1));
        }

        JPanel bottomJPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        final JLabel jLabelTips = new JLabel();
        bottomJPanel.add(jLabelTips);
        jLabelTips.setForeground(JBColor.RED);
        jPanelMain.add(bottomJPanel);
        JButton jButton = new JButton("确定");
        bottomJPanel.add(jButton);
        jPanelMain.add(bottomJPanel);
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String description = textFieldClassDescription.getText();
                if (description == null || description.length() == 0) {
                    jLabelTips.setText("请输入注解类的注释描述");
                    return;
                }
                if (!jCheckBoxSensors.isSelected() && !jCheckBoxUm.isSelected() && !jCheckBoxLocal.isSelected()) {
                    jLabelTips.setText("请至少选择一种Bi统计方式");
                } else {
                    jLabelTips.setText("");
                    List<CheckPointBean> list = new ArrayList<CheckPointBean>();
                    if (jCheckBoxSensors.isSelected()) {
                        list.add(initData(1, ListCheckBoxesSensors));
                    }
                    if (jCheckBoxUm.isSelected()) {
                        list.add(initData(2, ListCheckBoxesUm));
                    }
                    if (jCheckBoxLocal.isSelected()) {
                        list.add(initData(3, ListCheckBoxesLocal));
                    }
                    if (listener != null) {
                        listener.checked(textFieldClassNme.getText(), list, description);
                    }
                    jFrame.dispose();
                }
            }
        });

        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jFrame.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jFrame.getHeight()) / 2;
        jFrame.setLocation(w, h);
    }

    private static String getClassName(PsiClass psiClass, PsiMethod psiMethod) {
        String name = psiClass.getName();
        if (name == null) {
            name = "";
        }
        return name + formatNamed(psiMethod.getName());
    }

    private static String formatNamed(String name) {
        char[] ch = name.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    private static List<String> getMethodParameter(PsiMethod psiMethod) {
        List<String> parameter = new ArrayList<String>();
        JvmParameter[] parameters = psiMethod.getParameters();
        for (JvmParameter item : parameters) {
            parameter.add(formatParameter(item));
        }
        return parameter;
    }

    private static String formatParameter(JvmParameter jvmParameter) {
        String value = jvmParameter.getType().toString();
        return value.split(":")[1] + " " + jvmParameter.getName();
    }

    private static CheckPointBean initData(int pointType, List<JCheckBox> list) {
        CheckPointBean checkPointBean = new CheckPointBean();
        checkPointBean.setPointType(pointType);
        List<Integer> data = new ArrayList<Integer>();
        checkPointBean.setParameterList(data);
        for (int i = -1; i < list.size(); i += 2) {
            boolean isNotNull;
            if (i == -1) {
                isNotNull = true;
            } else {
                isNotNull = list.get(i).isSelected();
            }
            boolean isGet = list.get(i + 1).isSelected();
            int parameterType = 0;
            if (isGet) {
                parameterType = 1;
                if (!isNotNull) {
                    parameterType = 2;
                }
            }
            data.add(parameterType);
        }
        return checkPointBean;
    }

    public interface CheckPointListener {
        void checked(String className, List<CheckPointBean> list, String description);
    }
}
