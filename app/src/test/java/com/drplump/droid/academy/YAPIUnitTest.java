package com.drplump.droid.academy;

import com.drplump.droid.academy.cache.CacheDir;
import com.drplump.droid.academy.yapi.ELang;
import com.drplump.droid.academy.yapi.Lang;
import com.drplump.droid.academy.yapi.TranslateAPI;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class YAPIUnitTest {

    @Before
    public void setUp() throws Exception {
        CacheDir.init(System.getenv("tmp"));
    }

    @Test
    public void translate_singleWord() throws Exception {
        final String TEXT = "hello world";
        TranslateAPI api = new TranslateAPI();
        String result = api.translate(TEXT, ELang.ENGLISH.toString(), ELang.RUSSIAN.toString());
        assertEquals(result.toLowerCase(), "привет мир");
    }

    @Test
    public void translate_reverseWord() throws Exception {
        final String TEXT = "hello world";
        TranslateAPI api = new TranslateAPI();
        String result = api.translate(TEXT, ELang.ENGLISH.toString(), ELang.RUSSIAN.toString());
        String reserseResult = api.translate(result, ELang.RUSSIAN.toString(), ELang.ENGLISH.toString());
        assertEquals(reserseResult.toLowerCase(), TEXT.toLowerCase());
    }

    @Test
    public void translate_detectWord() throws Exception {
        final String TEXT = "привет мир";
        TranslateAPI api = new TranslateAPI();

        ELang lang = ELang.fromString(api.detect(TEXT, null));

        if (lang == null) throw new NullPointerException();

        String result = api.translate(TEXT, lang.toString(), ELang.ENGLISH.toString());
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