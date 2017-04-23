package com.drplump.droid.academy.hist;

import android.os.AsyncTask;
import android.util.Log;

import com.drplump.droid.academy.cache.Cache;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class History {

    private static History instance;

    private final Cache cache;
    private final List<HistoryItem> itemList;

    private History(File cacheDir) {
        this.cache = Cache.getCachedHistory(cacheDir);
        itemList = new ArrayList<>();
    }

    public static History newInstance() {
        return instance;
    }

    public static void init(File cacheDir) {
        if (instance == null) {
            instance = new History(cacheDir);
        }
    }

    public List<HistoryItem> list() {
        if(itemList.isEmpty()) read();
        return itemList;
    }

    public void add(HistoryItem item) {
        if(!itemList.contains(item)) {
            itemList.add(item);
            new WriteHistoryTask().execute();
        }
    }

    private synchronized boolean read() {
        if (!cache.verify()) return false;
        Document doc;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(new FileInputStream(cache.getPath()));
        } catch (Exception ex) {
            Log.e(getClass().getName(), ex.getMessage(), ex);
            return false;
        }
        Element root = doc.getDocumentElement();
        root.normalize();
        NodeList items = root.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            String direct = "";
            boolean favourites = false;
            String source = "";
            String translated = "";
            Node tmp;
            tmp = item.getAttributes().getNamedItem("direct");
            if (tmp != null) direct = tmp.getNodeValue();
            tmp = item.getAttributes().getNamedItem("favourites");
            if (tmp != null) favourites = Boolean.parseBoolean(tmp.getNodeValue());
            tmp = null;
            NodeList childItems = item.getChildNodes();
            for (int j = 0; j < childItems.getLength(); j++) {
                Node child = childItems.item(j);
                if(child.getNodeName().equals("source")) source = child.getTextContent();
                if(child.getNodeName().equals("translated")) translated = child.getTextContent();
            }
            if (!source.isEmpty() && !translated.isEmpty()) {
                HistoryItem h = new HistoryItem(source, translated, direct);
                if(favourites) h.favouriteIt();
                itemList.add(h);
            }
        }
        return true;
    }

    private synchronized boolean write() {
        if (itemList.isEmpty() || !cache.delete()) return false;

        Document doc;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (Exception ex) {
            Log.e(getClass().getName(), ex.getMessage(), ex);
            return false;
        }

        Element root = doc.createElement("hist");
        for (HistoryItem item : itemList) {
            Element itemNode = doc.createElement("item");
            itemNode.setAttribute("direct", item.direct);
            itemNode.setAttribute("favourites", String.valueOf(item.isFavourites()));
            Element sourceNode = doc.createElement("source");
            sourceNode.setTextContent(item.source);
            itemNode.appendChild(sourceNode);
            Element translatedNode = doc.createElement("translated");
            translatedNode.setTextContent(item.translated);
            itemNode.appendChild(translatedNode);

            root.appendChild(itemNode);
        }
        doc.appendChild(root);

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(cache.getPath()));
        } catch (Exception ex) {
            Log.e(getClass().getName(), ex.getMessage(), ex);
            return false;
        }

        return true;
    }

    private class WriteHistoryTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return write();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(!aBoolean) Log.e(History.class.getName(), "Error on store history");
        }
    }



}
