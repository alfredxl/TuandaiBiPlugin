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
import com.intellij.util.PairConsumer;
import com.paisheng.bi.bean.BiClassMethod;
import com.paisheng.bi.inspection.InspectionTools;
import com.paisheng.bi.ui.BiRefreshableOnComponent;
import com.paisheng.bi.util.ContrastBiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

class PaiSCodeAnalysisHandler extends CheckinHandler {
    private static final String ID = "PaiSTool";
    private BiRefreshableOnComponent refreshableOnComponent;
    private CheckinProjectPanel checkinProjectPanel;
    private List<BiClassMethod> problemBiList = new ArrayList<>();


    PaiSCodeAnalysisHandler(CheckinProjectPanel checkinProjectPanel) {
        this.checkinProjectPanel = checkinProjectPanel;
    }

    @Override
    public RefreshableOnComponent getBeforeCheckinConfigurationPanel() {
        if (refreshableOnComponent == null) {
            refreshableOnComponent = new BiRefreshableOnComponent();
        }
        return refreshableOnComponent;
    }

    @Override
    public ReturnResult beforeCheckin(@Nullable CommitExecutor executor, PairConsumer<Object, Object> additionalDataConsumer) {
        startInitToolWindows();
        if (refreshableOnComponent != null && refreshableOnComponent.getState()) {
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
        ProgressManager.getInstance().run(new Task.Modal(checkinProjectPanel.getProject(), "Check Bi Annotation Method Changes...", true) {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                problemBiList.clear();
                problemBiList.addAll(ContrastBiMethod.collectMethod(checkinProjectPanel.getSelectedChanges()));
            }
        });
    }

    private ReturnResult showDialog() {
        if (Messages.showOkCancelDialog(checkinProjectPanel.getProject(), "\r\nFound the Bi annotation method was changed!\r\n",
                "Bi Method Change Detection", "View changes", "Commit Anyway", null) == Messages.OK) {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(checkinProjectPanel.getProject());
            ToolWindow window = toolWindowManager.getToolWindow(ID);
            if (window == null) {
                window = toolWindowManager.registerToolWindow("PaiSTool", true, ToolWindowAnchor.BOTTOM);
                window.setIcon(IconLoader.getIcon("/image/ic_menu.png"));
            }
            ToolWindow finalWindow = window;
            window.show(() -> InspectionTools.addTab(checkinProjectPanel.getProject(), finalWindow, problemBiList));
            return CheckinHandler.ReturnResult.CLOSE_WINDOW;
        } else {
            return CheckinHandler.ReturnResult.COMMIT;
        }
    }

    private void startInitToolWindows() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(checkinProjectPanel.getProject());
        ToolWindow window = toolWindowManager.getToolWindow(ID);
        if (window != null && window.getContentManager().getContentCount() > 0) {
            window.getContentManager().removeAllContents(true);
        }
    }
}
