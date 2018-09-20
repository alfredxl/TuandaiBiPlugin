package com.paisheng.bi.ui;

import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.ui.NonFocusableCheckBox;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.panels.HorizontalLayout;

import javax.swing.*;

public class BiRefreshableOnComponent implements RefreshableOnComponent {
    private JCheckBox checkBox;
    private JBPanel jPanel;

    public BiRefreshableOnComponent() {
        checkBox = new NonFocusableCheckBox("Tuandai Bi Code Check");
        jPanel = new JBPanel(new HorizontalLayout(0));
        jPanel.add(checkBox);
    }

    @Override
    public JComponent getComponent() {
        return jPanel;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void saveState() {

    }

    @Override
    public void restoreState() {
        checkBox.setSelected(true);
    }

    public boolean getState() {
        return checkBox != null && checkBox.isSelected();
    }
}
