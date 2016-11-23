package com.ghostwan.dtake;

import com.facebook.stetho.Stetho;
import com.orm.SugarApp;

/**
 * Created by erwan on 21/11/2016.
 */

public class DTakeApp extends SugarApp {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
