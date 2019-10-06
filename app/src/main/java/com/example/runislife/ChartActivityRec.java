package com.example.runislife;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


public class ChartActivityRec extends AppCompatActivity {
    double currentDist = 0;
    double totalDist = 0;
    long totalTime = 0;
    ArrayList<Double> xDist = new ArrayList<>();
    ArrayList<Double> yPace = new ArrayList<>();
    ArrayList<Double> dist = new ArrayList<>();
    ArrayList<Double> sped = new ArrayList<>();
    ArrayList<GpsRec> locations = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chart_rec);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimary)));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>График скорости </font>"));

            double pre_speed = 0;
            double alt = 0;
            double speed = 0;
            double yMaxPace = 0;
            int yMaxAlt = 0;

            ArrayList<Double> avgAlt = new ArrayList<>();
            ArrayList<Double> avgSpeed = new ArrayList<>();
            if (locations.size()>0)
                totalTime = (locations.get(locations.size()-1).getDate().getTime()-locations.get(0).getDate().getTime())/1000;
            for(GpsRec r:locations) totalDist += r.distance;

            double avgSpeedAll = totalDist/totalTime;

            for (GpsRec rec: locations) {
                currentDist += rec.distance;
                dist.add(currentDist);
                sped.add(rec.speed);
                avgSpeed.add(rec.speed);
                if (avgSpeed.size()>Conf.SPEED_AVG)avgSpeed.remove(0);
                speed = 0;
                for(double s: avgSpeed)speed += s;
                speed /= avgSpeed.size();
                if (Conf.ACCELERATE_FACTOR>0 && pre_speed>0 && speed>pre_speed*(1+Conf.ACCELERATE_FACTOR))speed=pre_speed*(1+Conf.ACCELERATE_FACTOR);
                if (Conf.ACCELERATE_FACTOR>0 && pre_speed>0 && speed<pre_speed*(1/(1+Conf.ACCELERATE_FACTOR)))speed=pre_speed*(1/(1+Conf.ACCELERATE_FACTOR));
                pre_speed = speed;

                if (rec.getAlt() != Conf.INVALID_ALT) {
                    avgAlt.add(rec.getAlt());
                    if (avgAlt.size()>Conf.SPEED_AVG)avgAlt.remove(0);
                    alt = 0;
                    for (double t: avgAlt) alt += t;
                    alt /= avgAlt.size();
                }
                if (locations.indexOf(rec)>= Conf.SPEED_AVG) {
                    xDist.add((Conf.getDistance(currentDist)));

                    double y_pace = Conf.getSpeed( speed, avgSpeedAll);
                    if (y_pace>yMaxPace) yMaxPace=y_pace;
                    yPace.add(y_pace);
                }
                }

        int n = dist.size();
        GraphView graph;
        LineGraphSeries<DataPoint> series;
        graph =  findViewById(R.id.graph_rec);
        for (int i=0;i<n;i++) {
            series = new LineGraphSeries<>(data());
            graph.addSeries(series);
        }
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(dist.get(n-1));
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

    public DataPoint[] data(){
        int n=dist.size();
        DataPoint[] values = new DataPoint[n];
        for(int i=0;i<n;i++){

            DataPoint v = new DataPoint(dist.get(i),sped.get(i));
            values[i] = v;
        }
        return values;
    }
    }

