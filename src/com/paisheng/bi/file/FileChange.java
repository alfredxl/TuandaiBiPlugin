package com.paisheng.bi.file;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.AbstractVcs;

public class FileChange implements ProjectComponent {

    private DocumentListener documentListener = new DocumentListener() {
        private String contentOld;

        public void beforeDocumentChange(DocumentEvent event) {
            contentOld = event.getDocument().getText();
        }

        public void documentChanged(DocumentEvent event) {
//            Messages.showInfoMessage(contentOld + "\r\n" + event.getDocument().getText(), "提示");
        }
    };


    public void projectOpened() {
        EditorFactory.getInstance().getEventMulticaster().addDocumentListener(documentListener);
    }

    public void projectClosed() {
        EditorFactory.getInstance().getEventMulticaster().removeDocumentListener(documentListener);
    }
}
