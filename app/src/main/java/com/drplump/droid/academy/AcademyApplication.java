package com.drplump.droid.academy;

import android.app.Application;

import com.drplump.droid.academy.hist.History;

public class AcademyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        History.init(getFilesDir());
    }
}
