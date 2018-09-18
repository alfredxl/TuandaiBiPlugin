package com.paisheng.bi.inspection;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;

public class InspectionTools {
    public static void addTab(ToolWindow window) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel("Test"));
        Content content = contentFactory.createContent(jPanel, "TuandaiBiChange", false);
        window.getContentManager().addContent(content);
    }
}
