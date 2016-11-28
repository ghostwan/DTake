package com.ghostwan.dtake.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import com.ghostwan.dtake.calendardayview.Event;
import com.ghostwan.dtake.R;
import com.ghostwan.dtake.calendardayview.CalendarDayView;
import com.ghostwan.dtake.calendardayview.data.IEvent;
import com.ghostwan.dtake.entity.Take;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.util.*;

@EFragment(R.layout.fragment_day)
public class DayFragment extends Fragment {

    private static final String TAG = "DayFragment";

    @FragmentArg
    Date date;

    @ViewById
    CalendarDayView dayView;



    @AfterViews
    void initViews(){
        String title = getString(R.string.day_stats, DateFormat.getDateInstance().format(date));
        getActivity().setTitle(title);

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        List<Take> takes = Take.find(Take.class, Take.getWhereTakeDate(null)+" = "+Take.getWhereTakeDate(date.getTime()));
        List<IEvent> events = new ArrayList<>();
        calendar.setTime(date);
        for (Take take : takes) {
            Calendar calendar2 = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar2.setTime(take.getTakeDate());
            Log.d(TAG, "take : "+calendar2.get(Calendar.HOUR_OF_DAY) + "h"+calendar2.get(Calendar.MINUTE));
            events.add(new Event(calendar2, take.getOk()));
        }
        dayView.setEvents(events);

    }

}
