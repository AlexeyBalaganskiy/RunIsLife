package com.example.runislife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimelineFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Context context = null;
    SupportMapFragment mapFragment;
    TextView textDist = null;
    TextView textSpeed = null;
    TextView textTime = null;
    Spinner buttonSpinner;

    public final static String EXTRA_MESSAGE = "MESSAGE";
    GoogleMap map;
    Record record;
    ArrayList<Date> recordsDate = null;
    ArrayList<GpsRec> locations = null;

    public TimelineFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View rootView = inflater.inflate(R.layout.record_button, container, false);
        record = new Record(context);

        final ImageButton button_del =  rootView.findViewById(R.id.button_delete);
        final ImageButton button_chart = rootView.findViewById(R.id.button_chart);
        buttonSpinner = rootView.findViewById(R.id.button_spinner);
        textDist =  rootView.findViewById(R.id.button_distance);
        textSpeed = rootView.findViewById(R.id.button_speed);
        textTime = rootView.findViewById(R.id.button_time);
        buttonSpinner.setOnItemSelectedListener(this);

        recordsDate = Record.getRecords(context);
        SimpleDateFormat sdf = new SimpleDateFormat ("dd.MM.yyyy HH:mm") ;
        ArrayList<String> datestr = new ArrayList<>();
        datestr.add(context.getResources().getString(R.string.select_prompt));
        for (Date date: recordsDate) {
            datestr.add(sdf.format(date));
        }
        ArrayAdapter<String> adapter =new  ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, datestr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buttonSpinner.setAdapter(adapter);

        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = buttonSpinner.getSelectedItemPosition();
                if (pos > 0) {
                    Record.delete(context, recordsDate.get(pos - 1));
                    map.clear();
                    textTime.setText("00:00:00");
                    textSpeed.setText("0,00");
                    textDist.setText("0,00");
                    initSpinner();
                } else {
                    Toast.makeText(context, context.getString(R.string.select_no), Toast.LENGTH_SHORT).show();
                }

            }
        });
        button_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locations != null) {
                    final Intent intent_chart = new Intent(context, ChartActivityRec.class);
                    intent_chart.putExtra(EXTRA_MESSAGE, locations);
                    startActivity(intent_chart);
                }else{
                    Toast.makeText(context, context.getString(R.string.select_no), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    void initSpinner() {
        recordsDate = Record.getRecords(context);
        SimpleDateFormat sdf = new SimpleDateFormat () ;
        ArrayList<String> datestr = new ArrayList<>();
        datestr.add(context.getString(R.string.select_prompt));
        for (Date date: recordsDate) {
            datestr.add(sdf.format(date));
        }
        ArrayAdapter<String> adapter =new  ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, datestr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buttonSpinner.setAdapter(adapter);
        buttonSpinner.setSelection(0);
        buttonSpinner.invalidate();
        locations = null;
    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (id>0) {
            record.read(recordsDate.get((int)id-1));
            record.usedTime /=1000;
            locations = record.gpsRecs;
            final String timeStr = String.format("%d:%02d:%02d", record.usedTime / 3600, record.usedTime % 3600 / 60, record.usedTime % 3600 % 60);
            mapFragment =(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapp);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    final PolylineOptions polylines = new PolylineOptions();
                    polylines.color(Color.BLUE).width(10);
                    map.clear();
                    for (GpsRec rec: locations) {
                        CircleOptions circleOptions = new CircleOptions()
                                .center(new LatLng(rec.getLat(),rec.getLng())).radius(3)
                                .fillColor(Color.YELLOW).strokeColor(Color.DKGRAY)
                                .strokeWidth(5);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(circleOptions.getCenter(), 16));
                        map.addCircle(circleOptions);
                        polylines.add(new LatLng(rec.getLat(),rec.getLng()));
                    }
                    map.addPolyline(polylines);

                    textTime.setText(timeStr);
                    textSpeed.setText(String.format("%s %s", Conf.getSpeedString(record.distance/record.usedTime), Conf.getSpeedUnit()));
                    textDist.setText(String.format("%.2f %s", Conf.getDistance(record.distance), Conf.getDistanceUnit()));
                }
            });

        } else {
            locations = null;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        locations = null;
    }
}
