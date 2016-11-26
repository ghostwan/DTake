package com.ghostwan.dtake;

import android.icu.text.DateFormat;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Date;


/**
 * Created by erwan on 27/11/2016.
 */

public class DayFormatter implements IAxisValueFormatter {

    private final Long[] dates;
    private BarLineChartBase<?> chart;

    public DayFormatter(BarLineChartBase<?> chart, Long[] nanoTime) {
        this.dates = nanoTime;
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return DateFormat.getDateInstance().format(new Date(dates[(int) value]));
    }



}
