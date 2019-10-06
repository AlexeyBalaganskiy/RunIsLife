package com.example.runislife.data;

public class TrainingDayItem {

    public static final int TYPE_DAY = 0;
    public static final int TYPE_EXERCISE = 1;

    public enum TrainingStatus{
        SCHEDULED,
        ACTIVE,
        FAILED,
        FINISHED
    }

    private int id;
    private int parent_id;
    private String title="";
    private String description="";
    private int value=0;
    private TrainingStatus status;
    private int type;
    private int exercise_type;
    private int progress=0;

    public TrainingDayItem(int id, String title, String description, TrainingStatus status, int type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public TrainingDayItem(int id, String title, String description, TrainingStatus status, int type, int value, int progress) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.value = value;
        this.progress = progress;
    }

    public TrainingDayItem(int id, String title, String description, int value, int type, int exercise_type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.value = value;
        this.type = type;
        this.exercise_type = exercise_type;
    }

    public TrainingDayItem(int id, int parent_id, String title, String description, int value, TrainingStatus status, int type, int exercise_type, int progress) {
        this.id = id;
        this.parent_id = parent_id;
        this.title = title;
        this.description = description;
        this.value = value;
        this.status = status;
        this.type = type;
        this.exercise_type = exercise_type;
        this.progress = progress;
    }

    public TrainingDayItem(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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

    public TrainingStatus getStatus() {
        return status;
    }

    public void setStatus(TrainingStatus status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getExercise_type() {
        return exercise_type;
    }

    public void setExercise_type(int exercise_type) {
        this.exercise_type = exercise_type;
    }
}
