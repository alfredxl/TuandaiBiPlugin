package com.paisheng.bi.bean;

import com.github.javaparser.ast.body.MethodDeclaration;
public class BiMethodDeclaration {

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
