package com.ghostwan.dtake.calendardayview;

import com.ghostwan.dtake.R;
import com.ghostwan.dtake.calendardayview.data.IEvent;

import java.util.Calendar;

/**
 * Created by erwan on 28/11/2016.
 */

public class Event implements IEvent {

    private Calendar mStartTime;
    private boolean isOk;

    public Event(Calendar mStartTime, boolean isOk) {
        this.mStartTime = mStartTime;
        this.isOk = isOk;
    }


    public Calendar getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        this.mStartTime = startTime;
    }

    public Calendar getEndTime() {
        Calendar endTime = (Calendar) mStartTime.clone();
        endTime.add(Calendar.MINUTE, 15);
        return endTime;
    }

    @Override
    public String getName() {
        return isOk ? "OK" : "KO";
    }

    @Override
    public int getColor() {
        return isOk ? R.color.okColor : R.color.koColor;
    }

}
