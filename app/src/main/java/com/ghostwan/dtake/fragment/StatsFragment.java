package com.ghostwan.dtake.fragment;

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
import com.ghostwan.dtake.Util;
import com.ghostwan.dtake.entity.Take;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orm.SugarDb;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_stats)
public class StatsFragment extends Fragment {

    private static final String TAG = "StatsFragment";

    private HorizontalBarChart chart;

    @AfterViews
    protected void initViews() {
        getActivity().setTitle(R.string.stats);
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

        List<BarEntry> okEntryList = new ArrayList<>();
        List<BarEntry> koEntryList = new ArrayList<>();
        List<BarEntry> entryListCurrent = new ArrayList<>();

        Cursor cursor = database.rawQuery("select count(*), take_date " +
                " from take " +
                " group by " + Take.getWhereTakeDate(null) +
                " order by take_date", null);

        Log.d(TAG, "Data count: " + cursor.getCount());
        int i = 0;
        Long[] dates = new Long[cursor.getCount()];

        while (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            long datetime = cursor.getLong(1);

            if (Util.isDateSameDay(new Date(datetime), new Date())){
                entryListCurrent.add(new BarEntry(i, count, datetime));
            }
            else {
                Cursor cursor2 = database.rawQuery("select * " +
                        " from take " +
                        " where " + Take.getWhereTakeDate(null) + " = "+Take.getWhereTakeDate(datetime)+ " and is_ok = 0"
                        , null);
                if(cursor2.getCount() > 0)
                    koEntryList.add(new BarEntry(i, count, datetime));
                else
                    okEntryList.add(new BarEntry(i, count, datetime));
            }
            dates[i] = datetime;
            i++;
        }
        cursor.close();
        BarDataSet okDataSet = new BarDataSet(okEntryList, getString(R.string.good_take)); // add entries to dataset
        okDataSet.setValueTextSize(11);
        okDataSet.setColor(getResources().getColor(R.color.okColor));

        BarDataSet koDataSet = new BarDataSet(koEntryList, getString(R.string.bad_take)); // add entries to dataset
        koDataSet.setValueTextSize(11);
        koDataSet.setColor(getResources().getColor(R.color.koColor));

        BarDataSet dataSetCurrent = new BarDataSet(entryListCurrent, getString(R.string.current)); // add entries to dataset
        dataSetCurrent.setValueTextSize(12);


        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DayFormatter(chart, dates));
        xAxis.setDrawGridLines(false);
        chart.setData(new BarData(okDataSet,koDataSet, dataSetCurrent));
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Date date = new Date((long) e.getData());
                Log.d(TAG, "Selected : "+date);
                DayFragment dayFragment = DayFragment_.builder().date(date).build();
                StatsFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.content_main2, dayFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        chart.invalidate(); // refresh
        chart.setVisibleXRangeMaximum(7);
        chart.centerViewToY(i-1, YAxis.AxisDependency.LEFT);
        chart.invalidate();
    }


}
