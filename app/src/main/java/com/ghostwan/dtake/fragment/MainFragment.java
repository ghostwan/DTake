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
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

    @ViewById
    TextView countTake;

    @ViewById
    TextView lastTake;

    @AfterViews
    void initViews(){
        getActivity().setTitle(R.string.main);
        int count = Take.countTakeToday();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        long lastTakeThing = sharedPreferences.getLong(SettingsActivity.LAST_TAKE_PREF, -1);

        if(lastTakeThing != -1) {
            long lastTakeLong = sharedPreferences.getLong(SettingsActivity.LAST_TAKE_PREF, -1);;
            long timeDifference = (System.currentTimeMillis() - lastTakeLong);
            String lastTakenString = Util.getDurationBreakdown(timeDifference);
//            lastTake.setText(getString(R.string.last_pill_taken, "pills", lastTakenString));
            lastTake.setText(Util.getThing(getContext(), R.string.last_pill_taken, lastTakenString));
        }
        else
            lastTake.setText(Util.getThing(getContext(), R.string.no_taken));

        String countTakenString = getResources().getQuantityString(R.plurals.number_pill_taken, count, count,
                Util.getThingPreference(getContext(), true, true));
        countTake.setText(countTakenString);


    }

}
