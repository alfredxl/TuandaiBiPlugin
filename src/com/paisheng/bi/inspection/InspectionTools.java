package com.paisheng.bi.inspection;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.paisheng.bi.bean.BiClassMethod;
import com.paisheng.bi.ui.BiListLabel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InspectionTools {
    public static void addTab(final Project project, ToolWindow window, final List<BiClassMethod> problemBiList) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        final JBList<String[]> jbList = new JBList<>(getDefaultListModel(problemBiList));
        Content content = contentFactory.createContent(jbList, "TuandaiBiChange", false);
        window.getContentManager().addContent(content);
        jbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jbList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listItemClick(e, problemBiList, jbList, project);
            }
        });
        jbList.setCellRenderer(new BiListLabel());
    }

    private static ListModel<String[]> getDefaultListModel(List<BiClassMethod> problemBiList) {
        DefaultListModel<String[]> myListModel = new DefaultListModel<>();
        for (BiClassMethod biClassMethod : problemBiList) {
            String path = biClassMethod.isHasDelete() ? biClassMethod.getBeforeVirtualFilePath() : biClassMethod.getAfterVirtualFilePath();
            int index = path.lastIndexOf("/");
            String name = path.substring(index + 1, path.length() - 5);
            String parentPath = path.substring(0, index);
            myListModel.addElement(new String[]{name, parentPath, biClassMethod.isHasDelete() ? "0" : "1"});
        }
        return myListModel;
    }

    private static void listItemClick(MouseEvent e, List<BiClassMethod> problemBiList, JBList<String[]> jbList, Project project) {
        if (e.getClickCount() == 2) {
            BiClassMethod biClassMethod = problemBiList.get(jbList.getSelectedIndex());
            String title = biClassMethod.isHasDelete() ? biClassMethod.getBeforeVirtualFilePath() : biClassMethod.getAfterVirtualFilePath();
            List<DiffContent> contents = new ArrayList<>();
            List<String> itemTitles = new ArrayList<>();
            contents.add(DiffContentFactory.getInstance().create(biClassMethod.getBeforeContent() == null
                    ? "" : biClassMethod.getBeforeContent(), JavaFileType.INSTANCE));
            itemTitles.add("history");
            contents.add(DiffContentFactory.getInstance().create(biClassMethod.getAfterContent() == null
                    ? "" : biClassMethod.getAfterContent(), JavaFileType.INSTANCE));
            itemTitles.add("you version");
            SimpleDiffRequest simpleDiffRequest = new SimpleDiffRequest(title, contents, itemTitles);
            DiffManager.getInstance().showDiff(project, simpleDiffRequest);
            if (biClassMethod.getAfterVirtualFilePath() != null) {
                File afterFile = new File(biClassMethod.getAfterVirtualFilePath());
                if (afterFile.exists()) {
                    VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(afterFile);
                    if (virtualFile != null) {
                        FileEditorManager.getInstance(project).openFile(virtualFile, true, true);
                    }
                }
            }
        }
    }
}
