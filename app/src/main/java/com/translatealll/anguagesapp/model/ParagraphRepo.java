package com.translatealll.anguagesapp.model;

public class ParagraphRepo {
    int flag;
    String name;



    public ParagraphRepo(String name, int flag) {
        this.name = name;
        this.flag = flag;
    }

    public ParagraphRepo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
