package com.ghostwan.dtake.fragment;

import android.support.v4.app.Fragment;
import com.ghostwan.dtake.R;
import com.ghostwan.dtake.Util;
import com.ghostwan.dtake.entity.Take;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_stats)
public class StatsFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        Util.get().displayText(getView(), "Displaying graph");
        int count = (int) Take.count(Take.class, "date(TAKE_DATE/1000, 'unixepoch') = date('now','localtime')", null);
    }
}
