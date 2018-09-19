package com.paisheng.bi.bean;


import java.util.*;

public class BiClassMethod {
    private boolean hasDelete;
    private String beforeVirtualFilePath;
    private String afterVirtualFilePath;
    private String beforeContent;
    private String afterContent;
    private Map<String, BiMethodDeclaration> beforeMap = new HashMap<String, BiMethodDeclaration>();
    private Map<String, BiMethodDeclaration> afterMap = new HashMap<String, BiMethodDeclaration>();

    public boolean isHasDelete() {
        return hasDelete;
    }

    public void setHasDelete(boolean hasDelete) {
        this.hasDelete = hasDelete;
    }

    public String getBeforeVirtualFilePath() {
        return beforeVirtualFilePath;
    }

    public void setBeforeVirtualFilePath(String beforeVirtualFilePath) {
        this.beforeVirtualFilePath = beforeVirtualFilePath;
    }

    public String getAfterVirtualFilePath() {
        return afterVirtualFilePath;
    }

    public void setAfterVirtualFilePath(String afterVirtualFilePath) {
        this.afterVirtualFilePath = afterVirtualFilePath;
    }

    public String getBeforeContent() {
        return beforeContent;
    }

    public void setBeforeContent(String beforeContent) {
        this.beforeContent = beforeContent;
    }

    public String getAfterContent() {
        return afterContent;
    }

    public void setAfterContent(String afterContent) {
        this.afterContent = afterContent;
    }

    public Map<String, BiMethodDeclaration> getBeforeMap() {
        return beforeMap;
    }

    public Map<String, BiMethodDeclaration> getAfterMap() {
        return afterMap;
    }
}
