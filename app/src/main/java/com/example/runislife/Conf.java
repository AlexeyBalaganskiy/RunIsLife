package com.example.runislife;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class Conf extends AppCompatActivity {
    static Context context;

    public static String LENGTH_UNIT = "International unit";
    public static String MAP_TYPE = "Google Map";
    public static String SPEED_TYPE = "Speed";
    public static int MIN_DISTANCE = 0;
    public static int INTERVAL_LOCATION = 10;
    public static int INTERVAL_LOCATION_FAST = 1;
    public static int LOCATION_ACCURACY = 100;
    public static int SPEED_AVG = 10;
    public static boolean GPS_ONLY = true;
    public static double ACCELERATE_FACTOR = 1f;
    public static final int INVALID_ALT = -999;

    static public void init(Context context) {
        if (Conf.context == null) {
            Conf.context = context;
        }
        LENGTH_UNIT = context.getString(R.string.international);

        read();
    }
    static public void read() {
        String filename = String.format(getRootDir() + "/conf.json");
        try {
            FileInputStream fin = new FileInputStream(filename);
            JsonReader reader = new JsonReader(new InputStreamReader(fin, "UTF-8"));

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("LENGTH_UNIT")) {
                    LENGTH_UNIT = reader.nextString();
                } else if (name.equals("MAP_TYPE")) {
                    MAP_TYPE = reader.nextString();
                } else if (name.equals("SPEED_TYPE")) {
                    SPEED_TYPE = reader.nextString();
                } else if (name.equals("MIN_DISTANCE")) {
                    MIN_DISTANCE = reader.nextInt();
                } else if (name.equals("INTERVAL_LOCATION")) {
                    INTERVAL_LOCATION = reader.nextInt();
                } else if (name.equals("INTERVAL_LOCATION_FAST")) {
                    INTERVAL_LOCATION_FAST = reader.nextInt();
                } else if (name.equals("LOCATION_ACCURACY")) {
                    LOCATION_ACCURACY = reader.nextInt();
                } else if (name.equals("SPEED_AVG")) {
                    SPEED_AVG = reader.nextInt();
                } else if (name.equals("GPS_ONLY")) {
                    GPS_ONLY = reader.nextBoolean();
                } else if (name.equals("GPS_ONLY")) {
                    ACCELERATE_FACTOR = reader.nextDouble();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

            reader.close();
            fin.close();
        }catch (Exception e) {
        }
    }

    public static String getDistanceUnit() { return "Км";  }
    public static String getSpeedUnit() { return "Км/ч";  }
    public static double getDistance(double distance) {return distance/1000f;}
    public static double getAltitude(double alt) { return alt; }
    public static double getSpeed(double speed, double avgSpeed) {
        if (speed < 0.000001) return 0;
        if (LENGTH_UNIT.equals(context.getResources().getString(R.string.international))) {
            if (SPEED_TYPE.equals(context.getResources().getString(R.string.pace))) {
                if (speed<avgSpeed/2)speed = avgSpeed/2;
                return 1000/(60*speed);
            }else{
                return (speed/1000)*3600;
            }
        }else{
            if (SPEED_TYPE.equals(context.getResources().getString(R.string.pace))) {
                if (speed<avgSpeed/2)speed = avgSpeed/2;
                return 1609.34f/(60*speed);
            }else{
                return (speed/1609.34f)*3600;
            }
        }
    }

    public static String getSpeedString(double speed) {
        double sp = getSpeed(speed,0);
        if (SPEED_TYPE.equals(context.getResources().getString(R.string.pace))) {
            return String.format("%02d:%02d", (int)Math.floor(sp), Math.round(60 * (sp - Math.floor(sp))));
        } else {
            return String.format("%.02f", sp);
        }
    }
    public static String getRootDir() {
        File dir = Environment.getExternalStorageDirectory();
        String rootDir = null;
        if (dir == null) {
            dir = context.getDir(".", 0);
            rootDir = dir.getAbsolutePath();
        } else {
            rootDir = dir.getAbsolutePath() +"/"+ context.getResources().getString(R.string.app_name);
        }
        dir = new File(rootDir);
        if (dir.mkdir()) {
            new File(rootDir+"/records").mkdir();
        }
        return rootDir;
    }
}
