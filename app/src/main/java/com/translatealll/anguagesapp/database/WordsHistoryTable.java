package com.translatealll.anguagesapp.database;

import java.io.Serializable;


public class WordsHistoryTable implements Serializable {
    String language1;
    String language2;
    int lngsId;
    String texttotranslate;
    String translatedtext;

    public String getLanguage1() {
        return this.language1;
    }

    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    public String getLanguage2() {
        return this.language2;
    }

    public void setLanguage2(String language2) {
        this.language2 = language2;
    }

    public String getTexttotranslate() {
        return this.texttotranslate;
    }

    public void setTexttotranslate(String texttotranslate) {
        this.texttotranslate = texttotranslate;
    }

    public String getTranslatedtext() {
        return this.translatedtext;
    }

    public void setTranslatedtext(String translatedtext) {
        this.translatedtext = translatedtext;
    }
}
