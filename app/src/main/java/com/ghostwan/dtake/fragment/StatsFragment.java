package com.ghostwan.dtake.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.ghostwan.dtake.DayFormatter;
import com.ghostwan.dtake.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.orm.SugarDb;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_stats)
public class StatsFragment extends Fragment {

    private static final String TAG = "StatsFragment";
    @ViewById
    HorizontalBarChart chart;

    @AfterViews
    protected void initViews(){

        SQLiteDatabase database = SugarDb.getInstance().getDB();

        List<BarEntry> entryList = new ArrayList<>();

        Cursor cursor = database.rawQuery("select count(*), take_date " +
                "from take " +
                "group by date(take_date/1000, 'unixepoch', 'localtime') " +
                "order by take_date", null);

        Log.d(TAG, "Data count: "+cursor.getCount());
        int i = 0;
        Long[] dates = new Long[cursor.getCount()];
        while (cursor.moveToNext()){
            int count = cursor.getInt(0);
            long datetime = cursor.getLong(1);
            entryList.add(new BarEntry(i, count));
            dates[i] = datetime;
            i++;
        }
        cursor.close();
        BarDataSet dataSet = new BarDataSet(entryList, "Pills"); // add entries to dataset
        BarData lineData = new BarData(dataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DayFormatter(chart, dates));
        chart.setVisibleXRangeMaximum(4);
        chart.setData(lineData);
        chart.invalidate(); // refresh



    }
    @Override
    public void onStart() {
        super.onStart();

    }
}
