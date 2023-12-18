package com.translatealll.anguagesapp.database;


import java.io.Serializable;

public class ChatTable implements Serializable {
    String Lang1;
    String Lang2;
    String chatname;
    int tableid;
    String texttotranslate;
    String translatedtext;
    String user;

    public ChatTable(String Lang1, String Lang2, String texttotranslate, String translatedtext, String user, String chatname) {
        this.Lang1 = Lang1;
        this.Lang2 = Lang2;
        this.texttotranslate = texttotranslate;
        this.translatedtext = translatedtext;
        this.user = user;
        this.chatname = chatname;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLang1() {
        return Lang1;
    }

    public void setLang1(String lang1) {
        this.Lang1 = lang1;
    }

    public String getLang2() {
        return Lang2;
    }

    public void setLang2(String lang2) {
        this.Lang2 = lang2;
    }

    public String getTexttotranslate() {
        return texttotranslate;
    }

    public void setTexttotranslate(String texttotranslate) {
        this.texttotranslate = texttotranslate;
    }

    public String getTranslatedtext() {
        return translatedtext;
    }

    public void setTranslatedtext(String translatedtext) {
        this.translatedtext = translatedtext;
    }

    public String getChatname() {
        return chatname;
    }

    public void setChatname(String chatname) {
        this.chatname = chatname;
    }
}
