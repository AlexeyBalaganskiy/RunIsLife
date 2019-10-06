package com.example.runislife.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = TrainingDay.class, parentColumns = "id_day", childColumns = "id_day", onDelete = CASCADE),
        @ForeignKey(entity = Exercise.class, parentColumns = "id_exercise", childColumns = "id_exercise")},
        indices = {@Index(value = {"id_day", "id_exercise"})})
public class TrainingDayExercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "id_day")
    private int id_day;
    @ColumnInfo(name = "id_exercise")
    private int id_exercise;
    private int progress=0;
    private int status=0;

    public TrainingDayExercise(int id, int id_day, int id_exercise, int progress, int status) {
        this.id = id;
        this.id_day = id_day;
        this.id_exercise = id_exercise;
        this.progress = progress;
        this.status = status;
    }

    @Ignore
    public TrainingDayExercise(int id, int id_day, int id_exercise, int status) {
        this.id = id;
        this.id_day = id_day;
        this.id_exercise = id_exercise;
        this.status = status;
    }

    @Ignore
    public TrainingDayExercise(int id_day, int id_exercise) {
        this.id_day = id_day;
        this.id_exercise = id_exercise;
    }

    @Ignore
    public TrainingDayExercise(int id, int id_day, int id_exercise) {
        this.id = id;
        this.id_day = id_day;
        this.id_exercise = id_exercise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_day() {
        return id_day;
    }

    public void setId_day(int id_day) {
        this.id_day = id_day;
    }

    public int getId_exercise() {
        return id_exercise;
    }

    public void setId_exercise(int id_exercise) {
        this.id_exercise = id_exercise;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
