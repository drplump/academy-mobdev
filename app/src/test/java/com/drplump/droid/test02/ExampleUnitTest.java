package com.drplump.droid.test02;

import com.drplump.droid.test02.yapi.ELang;
import com.drplump.droid.test02.yapi.Lang;
import com.drplump.droid.test02.yapi.TranslateAPI;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void translate_singleWord() throws Exception {
        final String TEXT = "hello world";
        TranslateAPI api = new TranslateAPI();
        String result = api.translate(TEXT, ELang.ENGLISH, ELang.RUSSIAN);
        assertEquals(result.toLowerCase(), "привет мир");
    }

    @Test
    public void translate_reverseWord() throws Exception {
        final String TEXT = "hello world";
        TranslateAPI api = new TranslateAPI();
        String result = api.translate(TEXT, ELang.ENGLISH, ELang.RUSSIAN);
        String reserseResult = api.translate(result, ELang.RUSSIAN, ELang.ENGLISH);
        assertEquals(reserseResult.toLowerCase(), TEXT.toLowerCase());
    }

    @Test
    public void translate_detectWord() throws Exception {
        final String TEXT = "привет мир";
        TranslateAPI api = new TranslateAPI();

        ELang lang = api.detect(TEXT, null);

        if (lang == null) throw new NullPointerException();

        String result = api.translate(TEXT, lang, ELang.ENGLISH);
        assertEquals(result.toLowerCase(), "hello world");
    }

    @Test
    public void translate_getLangs() throws Exception {
        final String TEXT = "привет мир";
        TranslateAPI api = new TranslateAPI();

        List<Lang> list = api.getLangs("en");
        assert(list.size() > 0);
    }
}