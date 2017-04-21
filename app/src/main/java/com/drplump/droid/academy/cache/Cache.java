package com.drplump.droid.academy.cache;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Cache {

    static String LANG_CACHE = "/langs";
    static String DICT_CACHE = "/dict";
    static String HIST_CACHE = "/history";

    private final File cached;
    //private CacheIndex index;

    private Cache(File cacheDir, String cacheName) {
        this.cached = new File(cacheDir.getAbsolutePath() + cacheName);
        if(!cached.getParentFile().exists()) cached.getParentFile().mkdirs();
        /*
        try {
            this.index = new CacheIndex(cached.getParentFile().getAbsolutePath());
        } catch (Exception ex) {
            this.index = null;// fuck
        }
        */
    }

    public String getPath() {
        return cached.getAbsolutePath();
    }

    public boolean delete() {
        return cached.delete();
    }

    public boolean verify() {
        if (!cached.exists()) return false;
        return cached.length() != 0 || !cached.delete();
    }

    public static Cache getCachedLang(File cachedDir, String lang) {
        return new Cache(cachedDir, LANG_CACHE + "/" + lang + ".xml");
    }

    public static Cache getCachedDict(File cachedDir, String direct, String text) {
        return new Cache(cachedDir, DICT_CACHE + "/" + direct + "/" + text + ".xml");
    }

    public static Cache getCachedHistory(File cachedDir) {
        return new Cache(cachedDir, HIST_CACHE + "/history.xml");
    }

    private class CacheIndex {

        private final String FILE_NAME = "cache.index.xml";
        private final File file;
        private Document doc;

        CacheIndex(String rootPath, Boolean withIndex) throws Exception {
            this.file = new File(rootPath + FILE_NAME);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            if (file.exists()) {
                this.doc = docBuilder.parse(new FileInputStream(file));
                doc.getDocumentElement().normalize();
            } else {
                this.doc = docBuilder.newDocument();
                doc.appendChild(doc.createElement("props"));
            }
        }

        String getProperty(String key) {
            String value = "";
            NodeList items = doc.getDocumentElement().getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
                Node k = item.getAttributes().getNamedItem("key");
                if (k != null && k.getNodeValue().equals(key)) {
                    Node t = item.getAttributes().getNamedItem("value");
                    if (t != null) {
                        value = t.getNodeValue();
                        break;
                    }
                }
            }
            return value;
        }

        void setProperty(String key, String value) {
            NodeList items = doc.getDocumentElement().getElementsByTagName("item");
            Node item = null;
            for (int i = 0; i < items.getLength(); i++) {
                item = items.item(i);
                Node k = item.getAttributes().getNamedItem("key");
                if (k != null && k.getNodeValue().equals(key)) break;
            }

            Attr a = doc.createAttribute("value");
            a.setValue(value);

            if (item != null) {
                Node t = item.getAttributes().getNamedItem(a.getName());
                t.getAttributes().setNamedItem(a);
            } else {
                item = doc.createElement("item");
                item.getAttributes().setNamedItem(a);
                doc.getDocumentElement().appendChild(item);
            }
        }

        void store() throws Exception {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(file));
        }
    }
}
