package com.ghostwan.dtake.calendardayview.decoration;

import android.graphics.Rect;
import com.ghostwan.dtake.calendardayview.DayView;
import com.ghostwan.dtake.calendardayview.EventView;
import com.ghostwan.dtake.calendardayview.data.IEvent;

/**
 * Created by FRAMGIA\pham.van.khac on 22/07/2016.
 */
public interface CdvDecoration {

    EventView getEventView(IEvent event, Rect eventBound, int hourHeight, int seperateHeight);

    DayView getDayView(int hour);
}
