package com.drplump.droid.academy.yapi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

public class TranslateAPI extends API {

    public final static String ERROR_MESSAGE = "translation service error";

    private final String API_URL = "https://translate.yandex.net/api/v1.5/tr";
    private final String TOKEN = "trnsl.1.1.20170413T121734Z.0479393b2459016b.4c2abd096a68643caa708220f4ea1b4eb1a17397";

    @Override
    String getServicePrefix(String service) {
        return API_URL + service + "?" + P_KEY + TOKEN;
    }

    public String translate(String text, String from, String to) throws Exception {
        String SERVICE = "/translate";

        URL url = new URL(getServicePrefix(SERVICE) + P_LANG + from + "-" + to + P_TEXT + URLEncoder.encode(text, "UTF-8"));
        Document doc = getResponse(url);
        if (doc == null) throw new Exception();
        Element root = doc.getDocumentElement();
        String code = root.getAttribute("code");
        if (code.equals("200")) {
            return root.getFirstChild().getTextContent();
        } else if (code.isEmpty()) {
            throw new Exception("Empty result code exception");
        } else {
            throw new Exception("API ERROR: method=" + SERVICE + ", code=" + code + ", message=" + root.getAttribute("message"));
        }
    }

    public String detect(String text, String hint) throws Exception {
        String SERVICE = "/detect";

        String stringUrl = getServicePrefix(SERVICE);
        if (hint != null) stringUrl += P_HINT + hint;
        stringUrl += P_TEXT + URLEncoder.encode(text, "UTF-8");

        Document doc = getResponse(new URL(stringUrl));
        if (doc == null) throw new Exception();
        Element root = doc.getDocumentElement();
        String code = root.getAttribute("code");
        if (code.equals("200")) {
            return root.getAttribute("lang");
        } else if (code.isEmpty()) {
            throw new Exception("Empty result code exception");
        } else {
            throw new Exception("API ERROR: method=" + SERVICE + ", code=" + code + ", message=" + root.getAttribute("message"));
        }
    }



}
