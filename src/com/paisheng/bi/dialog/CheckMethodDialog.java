package com.paisheng.bi.dialog;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CheckMethodDialog {

    public static void show(String[] methods, final CheckListener listener) {
        final JFrame jFrame = new JFrame("请选择要切入Bi的方法名称");
        JPanel jPanelMain = new JPanel(new VerticalLayout(10));
        jPanelMain.setBorder(JBUI.Borders.empty(10));
        jFrame.setContentPane(jPanelMain);
        final ButtonGroup buttonGroup = new ButtonGroup();
        JPanel radioJPanel = new JPanel(new VerticalLayout(10));
        JBScrollPane jbScrollPane = new JBScrollPane();
        jbScrollPane.setPreferredSize(new Dimension(500, 200));
        jbScrollPane.setViewportView(radioJPanel);
        jPanelMain.add(jbScrollPane);
        final List<JRadioButton> listButton = new ArrayList<JRadioButton>();
        for (String a : methods) {
            JRadioButton jRadioButton = new JRadioButton(a);
            buttonGroup.add(jRadioButton);
            radioJPanel.add(jRadioButton);
            listButton.add(jRadioButton);
        }
        JPanel bottomJPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        final JLabel jLabelTips = new JLabel();
        bottomJPanel.add(jLabelTips);
        jLabelTips.setForeground(JBColor.RED);
        jPanelMain.add(bottomJPanel);
        JButton jButton = new JButton("确定");
        bottomJPanel.add(jButton);
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selection = getChecked(listButton);
                if (selection < 0) {
                    jLabelTips.setText("请选择要切入Bi的方法名称");
                } else {
                    jLabelTips.setText("");
                    JRadioButton itemSelect = listButton.get(selection);
                    jFrame.dispose();
                    if (listener != null) {
                        listener.checked(selection, itemSelect.getText());
                    }
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

    private static int getChecked(List<JRadioButton> list) {
        int selection = -1;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JRadioButton jRadioButton = list.get(i);
                if (jRadioButton.isSelected()) {
                    selection = i;
                    break;
                }
            }
        }
        return selection;
    }

    public interface CheckListener {
        void checked(int position, String value);
    }
}
