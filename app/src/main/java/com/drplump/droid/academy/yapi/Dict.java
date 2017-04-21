package com.drplump.droid.academy.yapi;


import java.util.ArrayList;
import java.util.List;

public class Dict {

    private String text;
    private String pos;
    private String ts;
    private final List<Trans> tr;

    public Dict() {
        this.tr = new ArrayList<>();
    }

    public Dict(String text, String pos, String ts) {
        this.text = text;
        this.pos = pos;
        this.ts = ts;
        this.tr = new ArrayList<>();
    }

    public Trans newTr() {
        return new Trans();
    }

    public String getText() {
        return text;
    }

    public String getPos() {
        return pos;
    }

    public String getTs() {
        return ts;
    }

    public List<Trans> getTr() {
        return tr;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void addTrans(Trans tr) {
        this.tr.add(tr);
    }



    public class Trans {
        private String text;
        private String pos;
        private String gen;
        private final List<String> mean;

        public Trans() {
            this.mean = new ArrayList<>();
        }

        public Trans(String text, String pos, String gen) {
            this.text = text;
            this.pos = pos;
            this.gen = gen;
            this.mean = new ArrayList<>();
        }

        public String getText() {
            return text;
        }

        public String getPos() {
            return pos;
        }

        public String getGen() {
            return gen;
        }

        public List<String> getMean() {
            return mean;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public void setGen(String gen) {
            this.gen = gen;
        }

        public void addMean(String mean) {
            this.mean.add(mean);
        }
    }
}
