package com.paisheng.bi.bean;


import java.util.*;

public class BiClassMethod {
    private boolean hasDelete;
    private String virtualFilePath;
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

    public String getVirtualFilePath() {
        return virtualFilePath;
    }

    public void setVirtualFilePath(String virtualFilePath) {
        this.virtualFilePath = virtualFilePath;
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
