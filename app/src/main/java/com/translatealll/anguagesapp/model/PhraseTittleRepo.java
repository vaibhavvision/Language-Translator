package com.translatealll.anguagesapp.model;


public class PhraseTittleRepo {
    String title;
    int title_icon;

    public PhraseTittleRepo(String title, int title_icon) {
        this.title = title;
        this.title_icon = title_icon;
    }

    public String getTitle() {
        return title;
    }

    public int getTitle_icon() {
        return title_icon;
    }
}
