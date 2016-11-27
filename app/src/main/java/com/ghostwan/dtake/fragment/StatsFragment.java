package com.ghostwan.dtake.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import com.ghostwan.dtake.DTakeService;
import com.ghostwan.dtake.DayFormatter;
import com.ghostwan.dtake.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.orm.SugarDb;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_stats)
public class StatsFragment extends Fragment {

    private static final String TAG = "StatsFragment";

    private HorizontalBarChart chart;

    @AfterViews
    protected void initViews() {
        updateChart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chart = (HorizontalBarChart)view.findViewById(R.id.chart);
    }

    @Receiver(actions = DTakeService.ACTION_COUNT)
    protected void updateChart(){
        SQLiteDatabase database = SugarDb.getInstance().getDB();

        List<BarEntry> entryList = new ArrayList<>();
        List<BarEntry> entryListCurrent = new ArrayList<>();

        Cursor cursor = database.rawQuery("select count(*), take_date " +
                "from take " +
                "group by date(take_date/1000, 'unixepoch', 'localtime') " +
                "order by take_date", null);

        Log.d(TAG, "Data count: " + cursor.getCount());
        int i = 0;
        Long[] dates = new Long[cursor.getCount()];
        while (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            long datetime = cursor.getLong(1);
            if(i+1 != cursor.getCount()) {
                entryList.add(new BarEntry(i, count));
            }
            else {
                entryListCurrent.add(new BarEntry(i, count));
            }
            dates[i] = datetime;
            i++;
        }
        cursor.close();
        BarDataSet dataSet = new BarDataSet(entryList, "Pills"); // add entries to dataset
        dataSet.setValueTextSize(11);

        BarDataSet dataSetCurrent = new BarDataSet(entryListCurrent, "Current"); // add entries to dataset
        dataSetCurrent.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSetCurrent.setValueTextSize(12);


        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DayFormatter(chart, dates));
        chart.setData(new BarData(dataSet, dataSetCurrent));
        chart.invalidate(); // refresh
    }


}
