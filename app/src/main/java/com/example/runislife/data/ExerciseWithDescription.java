package com.example.runislife.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ExerciseWithDescription {

    @Embedded
    public TrainingDayExercise trainingDayExercise;

    @Relation(parentColumn = "id_exercise", entityColumn = "id_exercise")
    public List<Exercise> exercises;

}
