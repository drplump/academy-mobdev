package com.drplump.droid.test02.yapi;


public class Lang {
    public final String code;
    public final String descriptions;
    public final boolean preferred;

    public Lang(String code, String descriptions, boolean preferred) {
        this.code = code;
        this.descriptions = descriptions;
        this.preferred = preferred;
    }

    @Override
    public String toString() {
        return this.descriptions;
    }
}
