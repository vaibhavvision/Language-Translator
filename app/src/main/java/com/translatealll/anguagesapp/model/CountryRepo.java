package com.translatealll.anguagesapp.model;

import kotlin.jvm.internal.Intrinsics;

public final class CountryRepo {
    private String name;
    private String suggestname;


    public CountryRepo(String name, String suggestname) {
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
        if (obj instanceof CountryRepo) {
            CountryRepo country = (CountryRepo) obj;
            return suggestname == country.suggestname && Intrinsics.areEqual(name, country.name);
        }
        return false;
    }

    public int hashCode() {
        return (((Integer.hashCode(Integer.parseInt(suggestname)) * 31) + name.hashCode()) * 31);
    }


}