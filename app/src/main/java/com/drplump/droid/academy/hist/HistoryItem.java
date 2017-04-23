package com.drplump.droid.academy.hist;


import android.util.Log;

public class HistoryItem {
    public final String source;
    public final String translated;
    public final String direct;
    private boolean favourites;

    public HistoryItem(String source, String translation, String direct) {
        this.source = source;
        this.translated = translation;
        this.direct = direct;
        this.favourites = false;
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

    public void changeStatus() {
        this.favourites = !favourites;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HistoryItem)) {
            return false;
        }
        HistoryItem tmp = (HistoryItem) obj;
        if (tmp.direct != null && !tmp.direct.equals(this.direct)) return false;
        if ((tmp.source != null && !tmp.source.equals(this.source))
                && (tmp.translated != null && !tmp.translated.equals(this.translated))) return false;
        //if ((tmp.isFavourites() && !this.isFavourites()) || (!tmp.isFavourites() && this.isFavourites())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + direct.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + translated.hashCode();
        //result = 31 * result + (favourites ? 0 : 1);
        return result;
    }
}
