package com.drplump.droid.academy.yapi;

import com.drplump.droid.academy.cache.Cache;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DictionaryAPI extends API {

    private final String API_URL = "https://dictionary.yandex.net/api/v1/dicservice";
    private final String TOKEN = "trnsl.1.1.20170413T121734Z.0479393b2459016b.4c2abd096a68643caa708220f4ea1b4eb1a17397";

    public DictionaryAPI(File cacheDir) {
        super(cacheDir);
    }

    @Override
    String getServicePrefix(String service) {
        return API_URL + service + "?" + P_KEY + TOKEN;
    }


    public List<Dict> lookup(String text, String direct, String localeCode) throws Exception {
        String SERVICE = "/lookup";

        List<Dict> list = new ArrayList<>();

        Cache cache = Cache.getCachedDict(cacheDir, direct, text);

        getContent(new URL(getServicePrefix(SERVICE) + P_LANG + direct + P_TEXT + URLEncoder.encode(text, "UTF-8") + P_UI + localeCode), cache);

        Document doc = getResponse(new File(cache.getPath()));

        if (doc == null) throw new Exception();
        Element root = doc.getDocumentElement();
        if(root.getNodeName().equals("Error"))
            throw new Exception("API ERROR: method=" + SERVICE + ", code=" + root.getAttribute("code") + ", message=" + root.getAttribute("message"));

        NodeList nodeList = root.getElementsByTagName("def");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node defNode = nodeList.item(i);
            Dict d = new Dict();
            Node t;
            t = defNode.getAttributes().getNamedItem("pos");
            if (t != null) d.setPos(t.getNodeValue());
            t = defNode.getAttributes().getNamedItem("ts");
            if (t != null) d.setTs(t.getNodeValue());
            NodeList childNodes = defNode.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node childNode = childNodes.item(j);
                if (childNode.getNodeName().equals("text")) {
                    d.setText(childNode.getTextContent());
                } else if (childNode.getNodeName().equals("tr")) {
                    Dict.Trans tr = d.newTr();
                    t = defNode.getAttributes().getNamedItem("pos");
                    if (t != null) tr.setPos(t.getNodeValue());
                    NodeList trNodes = childNode.getChildNodes();
                    for (int k = 0; k < trNodes.getLength(); k++) {
                        Node trNode = trNodes.item(k);
                        if (trNode.getNodeName().equals("text")) {
                            tr.setText(trNode.getTextContent());
                        } else if (trNode.getNodeName().equals("mean")) {
                            String mean = trNode.getFirstChild().getTextContent();
                            tr.addMean(mean);
                        }
                    }
                    d.addTrans(tr);
                }
            }
        }
        return list;
    }
}
