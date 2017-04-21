package com.drplump.droid.academy.hist;


public class HistoryItem {
    public final String source;
    public final String translated;
    public final String direct;
    private boolean favourites;

    public HistoryItem(String source, String translation, String direct) {
        this.source = source;
        this.translated = translation;
        this.direct = direct;
    }

    public void favouriteIt() {
        this.favourites = true;
    }

    public void ignoreIt() {
        this.favourites = false;
    }

    public boolean isFavourites() {
        return this.favourites;
    }
}
