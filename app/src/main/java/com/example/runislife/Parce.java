package com.example.runislife;

import android.os.Parcel;
import android.os.Parcelable;


public class Parce implements Parcelable {
   public double xDist;
   public double ySpeed;

public Parce(double xDist,double ySpeed) {
    this.xDist= xDist;
    this.ySpeed=ySpeed;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(new double[] {this.xDist,this.ySpeed
        });
    }
    public Parce(Parcel in){
        double[] data = new double[2];

        in.readDoubleArray(data);
        this.xDist=data[0];
        this.ySpeed=data[1];

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Parce createFromParcel(Parcel in) {
            return new Parce(in);
        }

        public Parce[] newArray(int size) {
            return new Parce[size];
        }
    };

}
