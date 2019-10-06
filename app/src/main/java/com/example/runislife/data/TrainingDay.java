package com.example.runislife.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = TrainingPlan.class, parentColumns = "id_training", childColumns = "id_training", onDelete = CASCADE),
        indices=@Index(value={"day_number", "id_training"}, unique = true))
public class TrainingDay {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_day")
    private int id_day;

    @ColumnInfo(name = "day_number")
    private int day_number;
    @ColumnInfo(name = "id_training", index = true)
    private int id_training;
    private Date date;

    @Ignore
    public TrainingDay(int day_number, int id_training) {
        this.day_number = day_number;
        this.id_training = id_training;
    }

    public TrainingDay(int id_day, int id_training, Date date) {
        this.id_day = id_day;
        this.id_training = id_training;
        this.date = date;
    }

    @Ignore
    public TrainingDay(int id_day, int day_number, int id_training) {
        this.id_day = id_day;
        this.day_number = day_number;
        this.id_training = id_training;
    }

    public int getId_day() {
        return id_day;
    }

    public void setId_day(int id_day) {
        this.id_day = id_day;
    }

    public int getId_training() {
        return id_training;
    }

    public void setId_training(int id_training) {
        this.id_training = id_training;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDay_number() {
        return day_number;
    }

    public void setDay_number(int day_number) {
        this.day_number = day_number;
    }
}
