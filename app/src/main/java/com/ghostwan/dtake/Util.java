package com.ghostwan.dtake;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by erwan on 22/11/2016.
 */

@EBean
public class Util {

    private static final String TAG = "Util";
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

    @Background
    public void copyFile(View view, String inputPath, String ouputPath, CopyCallback copyCallback) {
        try {
            if (isExternalStorageWritable()) {
                File inputFile = new File(inputPath);
                File outputFile = new File(ouputPath);

                if (inputFile.exists()) {
                    FileChannel src = new FileInputStream(inputFile).getChannel();
                    FileChannel dst = new FileOutputStream(outputFile).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    copyCallback.onSuccess();
                } else {
                    copyCallback.onError();
                }
            }
            else {
                Util.get().displayText(view, "Can't write on SDCard!");
            }
        } catch (Exception e) {
            Log.i(TAG, "error : ", e);
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public String getStringDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        return  dateFormat.format(new Date());
    }

    public interface CopyCallback {

        void onSuccess();

        void onError();
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0 ) {
            sb.append(days);
            sb.append(" Days ");
        }
        if (hours > 0 ) {
            sb.append(hours);
            sb.append(" Hours ");
        }
        if (minutes > 0 ) {
            sb.append(minutes);
            sb.append(" Minutes ");
        }
        if (seconds > 0 ) {
            sb.append(seconds);
            sb.append(" Seconds ");
        }

        return(sb.toString());
    }
}
