package com.ghostwan.dtake.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import com.ghostwan.dtake.R;
import com.ghostwan.dtake.Util;
import com.nononsenseapps.filepicker.FilePickerActivity;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import static com.orm.util.ContextUtil.getPackageName;

@EFragment(R.layout.fragment_manage)
public class ManageFragment extends Fragment {

    private static final String TAG = "ManageFragment";
    private static final int RESTORE_CODE = 1;
    private static final int SAVE_CODE = 2;
    private String currentDBPath = "/data/data/" + getPackageName() + "/databases/dtake.db";


    @Click
    protected void saveDatabase(View view) {
        Util.get().displayText(view, "Saving database...");
        Intent i = new Intent(getContext(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, SAVE_CODE);
    }

    @Click
    protected void restoreDatabase(View view){
        Util.get().displayText(view, "Restoring database...");
        Intent i = new Intent(getContext(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, RESTORE_CODE);
    }

    @OnActivityResult(RESTORE_CODE)
    void onResultRestore(Intent data){
        if(data != null) {
            Uri uri = data.getData();
            Util.get().displayText(getView(), "Uri : "+uri.getPath());
            final String restorePath = uri.getPath();
            Util.get().copyFile(getView(), restorePath, currentDBPath, new Util.CopyCallback() {
                @Override
                public void onSuccess() {
                    Util.get().displayText(getView(), "Database restored : " + restorePath);
                }

                @Override
                public void onError() {
                    Util.get().displayText(getView(), "Error database file not found!");
                }
            });
        }
    }

    @OnActivityResult(SAVE_CODE)
    void onResultSave(Intent data){
        if(data != null) {
            Uri uri = data.getData();
            Util.get().displayText(getView(), "Uri : "+uri.getPath());
            String dbName= "dtake_backup"+Util.get().getStringDate()+".db";
            final String backupPath = uri.getPath()+"/"+dbName;
            Util.get().copyFile(getView(), currentDBPath, backupPath, new Util.CopyCallback() {
                @Override
                public void onSuccess() {
                    Util.get().displayText(getView(), "Database saved : " + backupPath);
                }

                @Override
                public void onError() {
                    Util.get().displayText(getView(), "Error database file not found!");
                }
            });
        }
    }



}
