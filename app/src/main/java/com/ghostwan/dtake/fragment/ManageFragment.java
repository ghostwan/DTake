package com.ghostwan.dtake.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import com.ghostwan.dtake.R;
import com.ghostwan.dtake.Util;
import com.ghostwan.dtake.Util_;
import com.nononsenseapps.filepicker.FilePickerActivity;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import static com.orm.util.ContextUtil.getPackageName;

@EFragment(R.layout.fragment_manage)
public class ManageFragment extends Fragment {

    private static final String TAG = "ManageFragment";
    private static final int FILE_CODE = 1;


    @Click
    protected void saveDatabase(View view) {
        Util_.get().displayText(view, "Saving database...");
        saveDatabase();
    }

    @Click
    protected void restoreDatabase(View view){
        Util.get().displayText(view, "Restoring database...");
        Intent i = new Intent(getContext(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(i, FILE_CODE);

//        restoreDatabase();
    }

    @OnActivityResult(FILE_CODE)
    void onResult(int resultCode, Intent data){
        if(data != null) {
            Uri uri = data.getData();
            Util.get().displayText(getView(), "Uri : "+uri.getPath());
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Background
    protected void saveDatabase() {
        try {
            if (isExternalStorageWritable()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/dtake.db";
                String backupDBPath = "dtake_backup.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(getContext().getExternalFilesDir(null), backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Util.get().displayText(getView(), "Database saved : " + backupDBPath);
                } else {
                    Util.get().displayText(getView(), "Error database file not found!");
                    Log.e(TAG, "Error path : " + currentDBPath + " doesn't exist!");
                }
            }
            else {
                Util.get().displayText(getView(), "Can't write on SDCard!");
            }
        } catch (Exception e) {
            Log.i(TAG, "error : ", e);
        }
    }

    @Background
    protected void restoreDatabase() {
        try {
            if (isExternalStorageWritable()) {
                String targetDBPath = "/data/data/" + getPackageName() + "/databases/dtake.db";
                String backupDBPath = "dtake_backup.db";
                File targetDB = new File(targetDBPath);
                File backupDB = new File(getContext().getExternalFilesDir(null), backupDBPath);

                if (backupDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(targetDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Util.get().displayText(getView(), "Database restored from : " + backupDBPath);
                } else {
                    Util.get().displayText(getView(), "Error database file not found!");
                    Log.e(TAG, "Error path : " + targetDBPath + " doesn't exist!");
                }
            }
            else {
                Util.get().displayText(getView(), "Can't write on SDCard!");
            }
        } catch (Exception e) {
            Log.i(TAG, "error : ", e);
        }
    }



}
