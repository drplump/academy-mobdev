package com.drplump.droid.test02.hist;

import com.drplump.droid.test02.cache.Cache;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class History {

    private final Cache cache;

    public History(File cacheDir) {
        this.cache = Cache.getCachedHistory(cacheDir);
    }

    private void read() {
        if (!cache.verify()) return;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new FileInputStream(cache.getPath()));
        Element root = doc.getDocumentElement();
        root.normalize();
        NodeList items = root.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);

        }

    }

}
