package com.drplump.droid.academy.yapi;

public enum ELang {
    AZERBAIJAN("az"),
    MALTESE("mt"),
    ALBANIAN("sq"),
    MACEDONIAN("mk"),
    AMHARIC("am"),
    MAORI("mi"),
    ENGLISH("en"),
    MARATHI("mr"),
    ARAB("ar"),
    MARI("mhr"),
    ARMENIAN("hy"),
    MONGOLIAN("mn"),
    AFRIKAANS("af"),
    GERMAN("de"),
    BASQUE("eu"),
    NEPALI("ne"),
    BASHKIR("ba"),
    NORWEGIAN("no"),
    BELARUSIAN("be"),
    PUNJABI("pa"),
    BENGAL("bn"),
    PAPIAMENTO("pap"),
    BULGARIAN("bg"),
    PERSIAN("fa"),
    BOSNIAN("bs"),
    POLISH("pl"),
    WELSH("cy"),
    PORTUGUESE("pt"),
    HUNGARIAN("hu"),
    ROMANIAN("ro"),
    VIETNAMESE("vi"),
    RUSSIAN("ru"),
    HAITIAN("ht"),
    CEBUANO("ceb"),
    GALICIAN("gl"),
    SERBIAN("sr"),
    DUTCH("nl"),
    SINHALA("si"),
    WESTERNMARI("mrj"),
    SLOVAK("sk"),
    GREEK("el"),
    SLOVENIAN("sl"),
    GEORGIAN("ka"),
    SWAHILI("sw"),
    GUJARATI("gu"),
    SUNDANESE("su"),
    DANISH("da"),
    TAJIK("tg"),
    HEBREW("he"),
    THAI("th"),
    YIDDISH("yi"),
    TAGALOG("tl"),
    INDONESIAN("id"),
    TAMIL("ta"),
    IRISH("ga"),
    TATAR("tt"),
    ITALIAN("it"),
    TELUGU("te"),
    ICELANDIC("is"),
    TURKISH("tr"),
    SPANISH("es"),
    UDMURT("udm"),
    KAZAKH("kk"),
    UZBEK("uz"),
    KANNADA("kn"),
    UKRAINIAN("uk"),
    CATALAN("ca"),
    URDU("ur"),
    KYRGYZ("ky"),
    FINNISH("fi"),
    CHINESE("zh"),
    FRENCH("fr"),
    KOREAN("ko"),
    HINDI("hi"),
    BRAID("xh"),
    CROATIAN("hr"),
    LATIN("la"),
    CZECH("cs"),
    LATVIAN("lv"),
    SWEDISH("sv"),
    LITHUANIAN("lt"),
    SCOTTISH("gd"),
    LUXEMBOURG("lb"),
    ESTONIAN("et"),
    MALAGASY("mg"),
    ESPERANTO("eo"),
    MALAY("ms"),
    JAVANESE("jv"),
    MALAYALAM("ml"),
    JAPANESE("ja");

    private final String lang;

    ELang(final String pLang) {
        lang = pLang;
    }

    public static ELang fromString(final String pLanguage) {
        for (ELang l : values()) {
            if (l.toString().equals(pLanguage)) {
                return l;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return lang;
    }
}
