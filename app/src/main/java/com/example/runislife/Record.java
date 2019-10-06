package com.example.runislife;

import android.content.Context;
import android.content.Intent;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

public class Record  {
    public String user = "default";
    public Date startTime;
    public long usedTime;
    public double distance;
    public ArrayList<GpsRec> gpsRecs;
    public Context context;
    public static final String MSG_RECORD_CHANGED="Record_CHANGED";
    Record(Context context) {this.context = context;}
    static public ArrayList<Date> getRecords(Context context) {
        File f = new File(Conf.getRootDir()+"/records");
        File file[] = f.listFiles();
        ArrayList<Date> ret = new ArrayList<>();
        if (file != null){
            for (int i = 0; i < file.length; i++) {
                String filename = file[i].getName();
                Long date = Long.parseLong(filename.replace("record-","").replace(".json",""));
                ret.add(new Date(date));
            }
        }
        return ret;
    }

    public void  save() {
        String filename = String.format(Conf.getRootDir()+"/records" + "/record-%d.json", System.currentTimeMillis());
        try {
            FileOutputStream fout = new FileOutputStream(filename);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fout, "UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();

            writer.name("user").value(user);
            writer.name("usedTime").value(usedTime);
            writer.name("startTime").value(startTime.getTime());
            writer.name("distance").value(distance);

            writer.name("rec");
            writer.beginArray();
            for (GpsRec rec : gpsRecs) {
                writer.beginObject();
                writer.name("lat").value(rec.lat);
                writer.name("lng").value(rec.lng);
                writer.name("alt").value(rec.alt);
                writer.name("distance").value(rec.distance);
                writer.name("speed").value(rec.speed);
                writer.name("date").value(rec.getDate().getTime());
                writer.endObject();
            }
            writer.endArray();

            writer.endObject();
            writer.close();
            fout.close();
            Toast.makeText(context, "Данные сохранены", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction(MSG_RECORD_CHANGED);
            context.sendBroadcast(intent);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read(Date date) {
        String filename = String.format(Conf.getRootDir()+"/records"+"/record-%d.json", date.getTime());
        try {
            FileInputStream fin = new FileInputStream(filename);
            JsonReader reader = new JsonReader(new InputStreamReader(fin, "UTF-8"));

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("user")) {
                    user = reader.nextString();
                } else if (name.equals("usedTime")) {
                    usedTime = reader.nextLong();
                } else if (name.equals("startTime")) {
                    startTime = new Date(reader.nextLong());
                } else if (name.equals("distance")) {
                    distance = reader.nextDouble();
                }else if (name.equals("rec")) {
                    gpsRecs = new ArrayList<GpsRec>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        GpsRec rec = new GpsRec();
                        reader.beginObject();
                        while(reader.hasNext()){
                            String itemName = reader.nextName();
                            if (itemName.equals("lat")){
                                rec.lat = reader.nextDouble();
                            } else if (itemName.equals("lng")) {
                                rec.lng = reader.nextDouble();
                            } else if (itemName.equals("alt")) {
                                rec.alt = reader.nextDouble();
                            } else if (itemName.equals("distance")) {
                                rec.distance = reader.nextDouble();
                            } else if (itemName.equals("speed")) {
                                rec.speed = reader.nextDouble();
                            } else if (itemName.equals("date")) {
                                rec.date = new Date(reader.nextLong());
                            } else {
                                reader.skipValue();
                            }
                        }
                        gpsRecs.add(rec);
                        reader.endObject();

                    }
                    reader.endArray();
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

    static void delete(Context context, Date date) {
        String filename = String.format(Conf.getRootDir()+"/records" + "/record-%d.json", date.getTime());
        File f = new File(filename);
        f.delete();
        Intent intent = new Intent();
        intent.setAction(MSG_RECORD_CHANGED);
        context.sendBroadcast(intent);
    }
}
