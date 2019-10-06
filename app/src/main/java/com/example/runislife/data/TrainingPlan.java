package com.example.runislife.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TrainingPlan {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_training")
    private int id_training;
    private String title;
    private String form_training;
    private int count_training;
    private String Description;
    private boolean added=false;
    private boolean custom=false;

    public TrainingPlan(int id_training, String title, String form_training, int count_training, String Description, boolean added, boolean custom) {
        this.id_training = id_training;
        this.title = title;
        this.form_training = form_training;
        this.count_training = count_training;
        this.Description = Description;
        this.added = added;
        this.custom = custom;
    }

    @Ignore
    public TrainingPlan(String title, String form_training, int count_training, String description) {
        this.title = title;
        this.form_training = form_training;
        this.count_training = count_training;
        Description = description;
    }

    @Ignore
    public TrainingPlan(String title, String form_training, int count_training, String description, boolean added, boolean custom) {
        this.title = title;
        this.form_training = form_training;
        this.count_training = count_training;
        Description = description;
        this.added = added;
        this.custom = custom;
    }

    public int getId_training() {
        return id_training;
    }

    public void setId_training(int id_training) {
        this.id_training = id_training;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForm_training() {
        return form_training;
    }

    public void setForm_training(String form_training) {
        this.form_training = form_training;
    }

    public int getCount_training() {
        return count_training;
    }

    public void setCount_training(int count_training) {
        this.count_training = count_training;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
