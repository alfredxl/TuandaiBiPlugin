package com.paisheng.bi.inspection;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PaiSInspectionToolProvider implements ToolWindowFactory {


    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

    }

    public void init(ToolWindow window) {
//        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//        JPanel jPanel = new JPanel();
//        jPanel.add(new JLabel("Test"));
//        Content content = contentFactory.createContent(jPanel, "", false);
//        window.getContentManager().addContent(content);
    }

    public boolean shouldBeAvailable(@NotNull Project project) {
        return false;
    }

    public boolean isDoNotActivateOnStart() {
        return true;
    }
}
