package com.paisheng.bi.bean;

import japa.parser.ast.body.MethodDeclaration;

public class BiMethodDeclaration {
    public static final String NAME_REGEX = "^Bi[a-zA-Z]+Note.(Local|Um|Sensors).[a-zA-Z0-9]+$";
    private boolean isChange;
    private MethodDeclaration methodDeclaration;

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }

    public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }
}
