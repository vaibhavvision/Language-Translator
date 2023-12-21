package com.translatealll.anguagesapp.model;

public class Paragraph {
    int flag;
    String name;



    public Paragraph(String name,int flag) {
        this.name = name;
        this.flag = flag;
    }

    public Paragraph(String name) {
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
