package com.ghostwan.dtake.fragment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.widget.TextView;
import com.ghostwan.dtake.R;
import com.ghostwan.dtake.SettingsActivity;
import com.ghostwan.dtake.Util;
import com.ghostwan.dtake.entity.Take;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

    @ViewById
    TextView countTake;

    @ViewById
    TextView lastTake;

    private SharedPreferences sharedPreferences;

    @AfterViews
    void initViews(){
        int count = Take.countTakeToday();
        String countTakenString = getResources().getQuantityString(R.plurals.number_pill_taken, count, count);
        countTake.setText(countTakenString);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        long lastTakeLong = sharedPreferences.getLong(SettingsActivity.LAST_TAKE_PREF, -1);;
        long timeDifference = (System.currentTimeMillis() - lastTakeLong);
        String lastTakenString = Util.getDurationBreakdown(timeDifference);
        lastTake.setText(getString(R.string.last_pill_taken, lastTakenString));
    }

}