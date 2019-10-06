package com.example.runislife;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import java.util.ArrayList;


public class ChartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_chart);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimary)));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>График скорости </font>"));
        ArrayList<Parce> parces = this.getIntent().getExtras().getParcelableArrayList("Parce");
        GraphView graph;
        int n=parces.size();
        LineGraphSeries<DataPoint> series;
        graph = findViewById(R.id.graph);
        for(int i=0;i<n;i++) {
            series = new LineGraphSeries<>(data());
            graph.addSeries(series);
        }
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(parces.get(n-1).xDist);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(30);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getGridLabelRenderer().setGridColor(Color.GRAY);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Расстояние");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Скорость");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);

    }
    public DataPoint[] data(){ArrayList<Parce> parces = this.getIntent().getExtras().getParcelableArrayList("Parce");
        int n=parces.size();
        DataPoint[] values = new DataPoint[n];
        for(int i=0;i<n;i++){
            DataPoint v = new DataPoint(parces.get(i).xDist,parces.get(i).ySpeed);
            values[i] = v;
        }
        return values;
    }
    }
