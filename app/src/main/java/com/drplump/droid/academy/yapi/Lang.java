package com.drplump.droid.academy.yapi;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Lang {

    public static final Set<String> SUPER_PREFERRED = new HashSet<String>(Arrays.asList(
            new String[] {"en", "ru", "uk"}
    ));

    public final String code;
    public final String descriptions;
    public final boolean preferred;

    public Lang(String code, String descriptions, boolean preferred) {
        this.code = code;
        this.descriptions = descriptions;
        this.preferred = preferred;
    }

    public static Lang parseFrom(String direction) {
        String[] dirs = direction.split("-");
        ELang l = ELang.fromString(dirs[0]);
        if (l != null) {
            return new Lang(l.toString(), l.name(), false);
        }
        return null;
    }

    public static Lang parseTo(String direction) {
        String[] dirs = direction.split("-");
        ELang l = ELang.fromString(dirs[1]);
        if (l != null) {
            return new Lang(l.toString(), l.name(), false);
        }
        return null;
    }

    @Override
    public String toString() {
        return this.descriptions;
    }
}
