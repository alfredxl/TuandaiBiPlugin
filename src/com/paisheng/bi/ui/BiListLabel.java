package com.paisheng.bi.ui;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;

public class BiListLabel extends JBLabel implements ListCellRenderer<String[]> {
    private JBColor back = new JBColor(new Color(75, 110, 175), new Color(75, 110, 175));
    private JBColor backNoFocus = new JBColor(new Color(13, 41, 62), new Color(13, 41, 62));
    private String fontBlue = "#6897BB";
    private String fontWhite = "#999999";
    private String fontWhiteFocus = "#BAADB4";
    private String fontGray = "#6C6C6C";
    private String text = "<HTML><font color='%s'>%s</font>&nbsp;<font color='%s'>%s</font></html>";

    public BiListLabel() {
        setIcon(IconLoader.getIcon("/image/ic_font.png"));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String[]> list, String[] value, int index, boolean isSelected, boolean cellHasFocus) {
        boolean isDelete = "0".equals(value[2]);
        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {
            // check if this cell is selected
        } else if (isSelected) {
            setOpaque(true);
            if (cellHasFocus) {
                setText(String.format(text, fontWhiteFocus, value[0], fontWhiteFocus, value[1]));
                setBackground(back);
            } else {
                setText(String.format(text, isDelete ? fontGray : fontBlue, value[0], fontWhite, value[1]));
                setBackground(backNoFocus);
            }
        } else {
            setOpaque(false);
            setText(String.format(text, isDelete ? fontGray : fontBlue, value[0], fontWhite, value[1]));
        }
        return this;
    }
}
