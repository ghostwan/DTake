package com.ghostwan.dtake;

import android.support.design.widget.Snackbar;
import android.view.View;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

/**
 * Created by erwan on 22/11/2016.
 */

@EBean
public class Util {

    private static Util util = null;

    @UiThread
    public void displayText(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public static Util get(){
        if (util == null)
            util = new Util();
        return util;
    }
}
