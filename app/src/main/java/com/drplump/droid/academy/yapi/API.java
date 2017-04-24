package com.drplump.droid.academy.yapi;


import com.drplump.droid.academy.cache.Cache;
import com.drplump.droid.academy.cache.CacheDir;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public abstract class API {

    public final static String DEFAULT_ERROR_MESSAGE = "api service error";

    final String P_KEY = "&key=";
    final String P_LANG = "&lang=";
    final String P_TEXT = "&text=";
    final String P_HINT = "&hint=";
    final String P_UI = "&ui=";

    private void offCheckSSL() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    Document getResponse(URL url) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc;
        try {
            doc = docBuilder.parse(url.openStream());
        } catch (Exception ex) {
            offCheckSSL();
            doc = docBuilder.parse(url.openStream());
        }
        doc.getDocumentElement().normalize();
        return doc;
    }

    Document getResponse(final File cached) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new FileInputStream(cached));
        doc.getDocumentElement().normalize();
        return doc;
    }

    void getContent(URL url, Cache cache) throws Exception {
        if (cache.verify()) return;
        ReadableByteChannel rbc;
        try {
            rbc = Channels.newChannel(url.openStream());
        } catch (IOException ex) {
            offCheckSSL();
            rbc = Channels.newChannel(url.openStream());
        }
        try {
            FileOutputStream fos = new FileOutputStream(cache.getPath());
            try {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } finally {
                fos.close();
            }
        } finally {
            rbc.close();
        }
    }

    public List<Lang> getLangs(String locale_code) throws Exception {

        String SERVICE = "/getLangs";

        List<Lang> list = new ArrayList<>();
        Cache cache = Cache.getCachedLang(locale_code);

        getContent(new URL(getServicePrefix(SERVICE) + P_UI + locale_code), cache);

        Document doc = getResponse(new File(cache.getPath()));
        Element root = doc.getDocumentElement();

        if(root.getNodeName().equals("Error")) {
            cache.delete();
            throw new Exception("API ERROR: method=" + SERVICE + ", code=" + root.getAttribute("code") + ", message=" + root.getAttribute("message"));
        }

        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression exp = xpath.compile("/Langs/langs/Item");
        NodeList nodeList = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String code = node.getAttributes().getNamedItem("key").getNodeValue();
            String description = node.getAttributes().getNamedItem("value").getNodeValue();
            list.add(new Lang(code, description, (code.equals(locale_code) || Lang.SUPER_PREFERRED.contains(code))));
        }
        if(list.isEmpty()) cache.delete();
        return list;
    }

    abstract String getServicePrefix(String service);
}
