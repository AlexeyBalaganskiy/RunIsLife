package com.example.runislife.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Time;

@Entity
public class Exercise {

    @Ignore
    public static final int DISTANCE = 0;
    @Ignore
    public static final int TIME = 1;
    @Ignore
    public static final int ACTION = 2;

    @PrimaryKey(autoGenerate = true)
    private int id_exercise;
    private String title;
    private String description;
    private int type;
    private int value;

    public Exercise(int id_exercise, String title, String description, int type, int value) {
        this.id_exercise = id_exercise;
        this.title = title;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    @Ignore
    public Exercise(String title, String description, int type, int value) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    public int getId_exercise() {
        return id_exercise;
    }

    public void setId_exercise(int id_exercise) {
        this.id_exercise = id_exercise;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
