package com.ghostwan.dtake.calendardayview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.ghostwan.dtake.R;
import com.ghostwan.dtake.calendardayview.data.IEvent;
import com.ghostwan.dtake.calendardayview.data.ITimeDuration;
import com.ghostwan.dtake.calendardayview.decoration.CdvDecoration;
import com.ghostwan.dtake.calendardayview.decoration.CdvDecorationDefault;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 */
public class CalendarDayView extends FrameLayout {

    private int mDayHeight = 300;

    private int mEventMarginLeft = 0;

    private int mHourWidth = 120;

    private int mTimeHeight = 120;

    private int mSeparateHourHeight = 0;

    private int mStartHour = 0;

    private int mEndHour = 24;

    private LinearLayout mLayoutDayView;

    private FrameLayout mLayoutEvent;

    private CdvDecoration mDecoration;

    private List<IEvent> mEvents;

    public CalendarDayView(Context context) {
        super(context);
        init(null);
    }

    public CalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CalendarDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_day_calendar, this, true);

        mLayoutDayView = (LinearLayout) findViewById(R.id.dayview_container);
        mLayoutEvent = (FrameLayout) findViewById(R.id.event_container);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarDayView);
            try {
                mEventMarginLeft =
                        a.getDimensionPixelSize(R.styleable.CalendarDayView_eventMarginLeft,
                                mEventMarginLeft);
                mDayHeight =
                        a.getDimensionPixelSize(R.styleable.CalendarDayView_dayHeight, mDayHeight);
                mStartHour = a.getInt(R.styleable.CalendarDayView_startHour, mStartHour);
                mEndHour = a.getInt(R.styleable.CalendarDayView_endHour, mEndHour);
            } finally {
                a.recycle();
            }
        }

        mEvents = new ArrayList<>();
        mDecoration = new CdvDecorationDefault(getContext());

        refresh();
    }

    public void refresh() {
        drawDayViews();

        drawEvents();
    }

    private void drawDayViews() {
        mLayoutDayView.removeAllViews();
        DayView dayView = null;
        for (int i = mStartHour; i <= mEndHour; i++) {
            dayView = getDecoration().getDayView(i);
            mLayoutDayView.addView(dayView);
        }
        mHourWidth = (int) dayView.getHourTextWidth();
        mTimeHeight = (int) dayView.getHourTextHeight();
        mSeparateHourHeight = (int) dayView.getSeparateHeight();
    }

    private void drawEvents() {
        mLayoutEvent.removeAllViews();

        for (IEvent event : mEvents) {
            Rect rect = getTimeBound(event);

            // add event view
            EventView eventView =
                    getDecoration().getEventView(event, rect, mTimeHeight, mSeparateHourHeight);
            if (eventView != null) {
                mLayoutEvent.addView(eventView, eventView.getLayoutParams());
            }
        }
    }

    private Rect getTimeBound(ITimeDuration event) {
        Rect rect = new Rect();
        rect.top = getPositionOfTime(event.getStartTime()) + mTimeHeight / 2 + mSeparateHourHeight;
        rect.bottom = getPositionOfTime(event.getEndTime()) + mTimeHeight / 2 + mSeparateHourHeight;
        rect.left = mHourWidth + mEventMarginLeft;
        rect.right = getWidth();
        return rect;
    }

    private int getPositionOfTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY) - mStartHour;
        int minute = calendar.get(Calendar.MINUTE);
        return hour * mDayHeight + minute * mDayHeight / 60;
    }

    public void setEvents(List<IEvent> events) {
        this.mEvents = events;
        refresh();
    }

    public void setLimitTime(int startHour, int endHour) {
        if (startHour >= endHour) {
            throw new IllegalArgumentException("start hour must before end hour");
        }
        mStartHour = startHour;
        mEndHour = endHour;
        refresh();
    }

    /**
     * @param decorator decoration for draw event, popup, time
     */
    public void setDecorator(@NonNull CdvDecoration decorator) {
        this.mDecoration = decorator;
        refresh();
    }

    public CdvDecoration getDecoration() {
        return mDecoration;
    }
}
