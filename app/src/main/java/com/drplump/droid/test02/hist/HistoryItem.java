package com.drplump.droid.test02.hist;


public class HistoryItem {
    public final String source;
    public final String translation;
    public final String direct;
    private boolean favourite;

    public HistoryItem(String source, String translation, String direct) {
        this.source = source;
        this.translation = translation;
        this.direct = direct;
    }

    public void favouriteIt() {
        this.favourite = true;
    }

    public void ignoreIt() {
        this.favourite = false;
    }

    public boolean isFavourite() {
        return this.favourite;
    }
}
