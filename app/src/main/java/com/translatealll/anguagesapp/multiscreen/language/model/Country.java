package com.translatealll.anguagesapp.multiscreen.language.model;

import kotlin.jvm.internal.Intrinsics;

public final class Country {
    private String name;
    private String suggestname;


    public Country(String name, String suggestname) {
        this.name = name;
        this.suggestname = suggestname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuggestname() {
        return suggestname;
    }

    public void setSuggestname(String suggestname) {
        this.suggestname = suggestname;
    }

    public final String component1() {
        return this.suggestname;
    }

    public final String component2() {
        return this.name;
    }



    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Country) {
            Country country = (Country) obj;
            return this.suggestname == country.suggestname && Intrinsics.areEqual(this.name, country.name);
        }
        return false;
    }

    public int hashCode() {
        return (((Integer.hashCode(Integer.parseInt(this.suggestname)) * 31) + this.name.hashCode()) * 31);
    }


}