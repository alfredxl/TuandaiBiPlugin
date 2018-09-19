package com.paisheng.bi.ui;

import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.ui.NonFocusableCheckBox;

import javax.swing.*;

public class BiRefreshableOnComponent implements RefreshableOnComponent {
    private JCheckBox checkBox;

    public BiRefreshableOnComponent() {
        checkBox = new NonFocusableCheckBox("Tuandai Bi Code Check");
    }

    @Override
    public JComponent getComponent() {
        return checkBox;
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
