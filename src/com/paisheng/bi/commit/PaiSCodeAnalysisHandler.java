package com.paisheng.bi.commit;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.NonFocusableCheckBox;
import com.intellij.util.PairConsumer;
import com.paisheng.bi.bean.BiClassMethod;
import com.paisheng.bi.inspection.InspectionTools;
import com.paisheng.bi.util.ContrastBiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

class PaiSCodeAnalysisHandler extends CheckinHandler {
    private final JCheckBox checkBox = new NonFocusableCheckBox("Tuandai Bi Code Check");
    private CheckinProjectPanel checkinProjectPanel;
    private List<BiClassMethod> problemBiList = new ArrayList<BiClassMethod>();
    private static ToolWindow window;


    PaiSCodeAnalysisHandler(CheckinProjectPanel checkinProjectPanel) {
        this.checkinProjectPanel = checkinProjectPanel;
    }

    @Override
    public RefreshableOnComponent getBeforeCheckinConfigurationPanel() {
        return new RefreshableOnComponent() {

            public void refresh() {
            }

            public void saveState() {
            }

            public void restoreState() {
                checkBox.setSelected(true);
            }

            public JComponent getComponent() {
                return checkBox;
            }
        };
    }

    @Override
    public ReturnResult beforeCheckin(@Nullable CommitExecutor executor, PairConsumer<Object, Object> additionalDataConsumer) {
        if (checkBox.isSelected()) {
            toDeal();
            if (problemBiList.size() > 0) {
                return showDialog();
            } else {
                return CheckinHandler.ReturnResult.COMMIT;
            }
        } else {
            return super.beforeCheckin(executor, additionalDataConsumer);
        }
    }

    private void toDeal() {
        ProgressManager.getInstance().run(new Task.Modal(checkinProjectPanel.getProject(), "检测Bi注解方法改动...", true) {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                problemBiList.clear();
                problemBiList.addAll(ContrastBiMethod.collectMethod(checkinProjectPanel.getSelectedChanges()));
            }
        });
    }

    private ReturnResult showDialog() {
        if (Messages.showOkCancelDialog(checkinProjectPanel.getProject(), "发现Bi注解方法被改动！",
                "Bi方法改动检测", "查看改动", "继续提交", null) == Messages.OK) {
            if (window == null) {
                ToolWindowManager.getInstance(checkinProjectPanel.getProject()).unregisterToolWindow("PaiSTool");
                window = ToolWindowManager.getInstance(checkinProjectPanel.getProject())
                        .registerToolWindow("PaiSTool", true, ToolWindowAnchor.BOTTOM);
                window.setIcon(IconLoader.getIcon("/image/bi_icon.png"));
            }
            window.getContentManager().removeAllContents(true);
            InspectionTools.addTab(window);
            window.show(null);
            return CheckinHandler.ReturnResult.CLOSE_WINDOW;
        } else {
            return CheckinHandler.ReturnResult.COMMIT;
        }
    }
}
