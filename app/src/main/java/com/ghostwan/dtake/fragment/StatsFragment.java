package com.ghostwan.dtake.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.ghostwan.dtake.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.GraphViewXML;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orm.SugarDb;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@EFragment(R.layout.fragment_stats)
public class StatsFragment extends Fragment {

    private static final String TAG = "StatsFragment";
    @ViewById
    GraphView graphViewXML;

    @AfterViews
    protected void initViews(){

        SQLiteDatabase database = SugarDb.getInstance().getDB();

        Date currentDate = null;
        int currentCount = 0;


        Cursor data = database.rawQuery("select count(*), take_date from take group by date(take_date/1000, 'unixepoch') order by take_date", null);

        graphViewXML.getViewport().setXAxisBoundsManual(true);
        DataPoint[] points = new DataPoint[data.getCount()];
        Log.d(TAG, "Data count: "+data.getCount());
        int i = 0;
        while (data.moveToNext()){
            if(currentDate == null) {
                currentDate = new Date(data.getLong(1));
                graphViewXML.getViewport().setMinX(currentDate.getTime());
            }
            currentDate = new Date(data.getLong(1));
            currentCount = data.getInt(0);

            points[i] = new DataPoint(currentDate, currentCount);
            i++;
        }
        data.close();
        graphViewXML.getViewport().setMaxX(currentDate.getTime());

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);
        series.setSpacing(20);

        graphViewXML.getViewport().setScrollable(true);
        graphViewXML.getViewport().setScalable(true);
        graphViewXML.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphViewXML.getGridLabelRenderer().setHumanRounding(false);
        graphViewXML.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("dd-MM")));
        graphViewXML.addSeries(series);

//        data.close();
//        series.setSpacing(10);
//        graphViewXML.addSeries(series);
//        graphViewXML.getViewport().setMaxX(currentDate.getTime());
//        graphViewXML.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("dd-MM")));
//        graphViewXML.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
//        graphViewXML.getGridLabelRenderer().setHumanRounding(false);
//        graphViewXML.getViewport().setScrollable(true);
//



    }
    @Override
    public void onStart() {
        super.onStart();

    }
}
