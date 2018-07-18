package com.paisheng.bi.bean;

import java.util.List;

public class CheckPointBean {
    private int pointType;// 1 神测， 2 友盟， 3 本地
    private List<Integer> parameterList; // 0 不取值  1 选中（NotNull） 2 选中(Nullable)

    public CheckPointBean() {

    }

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    public List<Integer> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Integer> parameterList) {
        this.parameterList = parameterList;
    }

    public String toString() {
        return "pointType : " + pointType + "; parameterList : " + parameterList.toString();
    }
}
